package mai_onsyn.trisona.core.decoder;

import mai_onsyn.trisona.core.message.AudioMessage;
import org.jspecify.annotations.NonNull;
import org.kc7bfi.jflac.FLACDecoder;
import org.kc7bfi.jflac.PCMProcessor;
import org.kc7bfi.jflac.metadata.StreamInfo;
import org.kc7bfi.jflac.util.ByteData;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static mai_onsyn.trisona.Global.SYSTEM_AUDIO_FORMAT;

public class FlacDecoder extends AudioDecoder {
    @Override
    AudioMessage readyAudioStream(DataInputStream is, long streamSize, StreamReadyRef readyRef) throws IOException {

        FlacToPcmInputStream pcmStream = new FlacToPcmInputStream(is);

        try {
            AudioMessage info = pcmStream.waitForInfo();

            readyRef.invoke(new DataInputStream(pcmStream));

            return info;

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    DataInputStream decode(DataInputStream is, AudioMessage sourceInfo) throws IOException {
        return DecodeUtil.wavRedecode(is, sourceInfo, SYSTEM_AUDIO_FORMAT);
    }

    private static class FlacToPcmInputStream extends InputStream {
        private AudioMessage info;
        private final Object lock = new Object(); // 唯一的通信锁

        private byte[] currentBuffer;
        private int bufferLen;
        private int bufferPointer;
        private boolean isEnded = false;
        private boolean dataReady = false; // 状态标记

        private final CompletableFuture<AudioMessage> infoFuture = new CompletableFuture<>();

        public AudioMessage waitForInfo() throws ExecutionException, InterruptedException {
            return infoFuture.get();
        }

        public FlacToPcmInputStream(InputStream in) {
            FLACDecoder decoder = new FLACDecoder(in);
            decoder.addPCMProcessor(new PCMProcessor() {
                @Override
                public void processStreamInfo(StreamInfo streamInfo) {
                    if (info == null) info = new AudioMessage();
                    info.sampleRate = streamInfo.getSampleRate();
                    info.bitDepth = streamInfo.getBitsPerSample();
                    info.channels = streamInfo.getChannels();
                    // 既然你固定了播放格式，这里用 4L 是没问题的
                    info.pcmByteLength = streamInfo.getTotalSamples() * 4L;
                    info.encoding = AudioMessage.Encoding.FLAC;
                    info.signed = true;
                    infoFuture.complete(info);
                }

                @Override
                public void processPCM(ByteData byteData) {
                    synchronized (lock) {
                        // 1. 如果消费者还没读完旧数据，生产者先在这里等
                        while (dataReady && !isEnded) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                return;
                            }
                        }

                        if (isEnded) return;

                        // 2. 塞入新数据
                        currentBuffer = byteData.getData();
                        bufferLen = byteData.getLen();
                        bufferPointer = 0;
                        dataReady = true;

                        // 3. 【关键】显式调用 lock.notifyAll() 唤醒正在 read 的消费者
                        lock.notifyAll();
                    }
                }
            });

            // 使用虚拟线程启动解码
            Thread.ofVirtual().name("FLAC-Decoder-Worker").start(() -> {
                try {
                    decoder.decode();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    synchronized (lock) {
                        isEnded = true;
                        lock.notifyAll(); // 结束时也要唤醒，防止消费者死等
                    }
                }
            });
        }

        @Override
        public int read(byte @NonNull [] b, int off, int len) throws IOException {
            synchronized (lock) {
                // 4. 如果没有准备好的数据，消费者就在这里等
                while (!dataReady && !isEnded) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new InterruptedIOException();
                    }
                }

                if (isEnded && !dataReady) return -1;

                // 5. 搬运数据
                int bytesToCopy = Math.min(bufferLen - bufferPointer, len);
                System.arraycopy(currentBuffer, bufferPointer, b, off, bytesToCopy);
                bufferPointer += bytesToCopy;

                // 6. 如果这一帧搬完了，标志位置为 false，唤醒生产者去解码下一帧
                if (bufferPointer >= bufferLen) {
                    dataReady = false;
                    lock.notifyAll();
                }

                return bytesToCopy;
            }
        }

        private final byte[] b = new byte[1];
        @Override
        public int read() throws IOException {
            int res = read(b, 0, 1);
            return (res == -1) ? -1 : (b[0] & 0xFF);
        }
    }
}
