package mai_onsyn.trisona.core.decoder;

import javazoom.jl.decoder.*;
import mai_onsyn.trisona.core.message.AudioMessage;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static mai_onsyn.trisona.Global.SYSTEM_AUDIO_FORMAT;

public class Mp3Decoder extends AudioDecoder {
    private static final Logger log = LoggerFactory.getLogger(Mp3Decoder.class);

    private final ID3TagSizeRef ref = new ID3TagSizeRef();
    @Override
    AudioMessage readyAudioStream(DataInputStream is, long streamSize, StreamReadyRef readyRef) throws IOException {
        AudioMessage info;
        BufferedInputStream bis = skipID3Tag(is, ref);

        bis.mark(10240);

        Bitstream bitstream = new Bitstream(bis);
        try {
            Header frameHeader = bitstream.readFrame();
            if (frameHeader != null) {
                info = new AudioMessage();
                SampleBuffer sampleBuffer = (SampleBuffer) new Decoder().decodeFrame(frameHeader, bitstream);
                info.sampleRate = sampleBuffer.getSampleFrequency();
                info.channels = frameHeader.mode() == Header.SINGLE_CHANNEL ? 1 : 2;
                info.encoding = AudioMessage.Encoding.MP3;
                info.bitDepth = 16;
                info.signed = true;
                info.pcmByteLength = (long) (info.sampleRate * info.channels * 2 * frameHeader.total_ms((int) (streamSize - ref.value)) / 1000);
            } else info = null;
        } catch (BitstreamException | DecoderException e) {
            throw new IOException(e);
        }
//        System.out.println(info);

        bis.reset();
        readyRef.invoke(new DataInputStream(bis));
        return info;
    }

    @Override
    DataInputStream decode(DataInputStream is, AudioMessage sourceInfo) throws IOException {
        Mp3ToPcmInputStream convertStream = new Mp3ToPcmInputStream(is);
        return DecodeUtil.wavRedecode(new DataInputStream(convertStream), sourceInfo, SYSTEM_AUDIO_FORMAT);
    }

//    private static class Mp3ToPcmInputStream extends InputStream {
//
//        private final Bitstream bitstream;
//        private final Decoder decoder;
//        private byte[] buffer;
//        private int bytePointer;
//        private int bufferLimit;
//
//        public Mp3ToPcmInputStream(InputStream is) throws IOException {
//            bitstream = new Bitstream(is);
//            decoder = new Decoder();
//            buffer = new byte[4096];
//            bytePointer = 0;
//        }
//
//        @Override
//        public int read() throws IOException {
//            System.out.println("a");
//            if (bytePointer >= bufferLimit) {
//                if (readNextFrame() == -1) return -1;
//            }
//            return buffer[bytePointer++] & 0xFF;
//        }
//
//        @Override
//        public int read(byte @NonNull [] b, int off, int len) throws IOException {
//            if (len == 0) return 0;
//
//            int totalRead = 0;
//
//            while (len > 0) {
//                if (bytePointer >= bufferLimit) {
//                    if (readNextFrame() == -1) {
//                        return totalRead == 0 ? -1 : totalRead;
//                    }
//                }
//
//                int available = bufferLimit - bytePointer;
//                int toCopy = Math.min(len, available);
//
//                System.arraycopy(buffer, bytePointer, b, off, toCopy);
//
//                bytePointer += toCopy;
//                off += toCopy;
//                len -= toCopy;
//                totalRead += toCopy;
//                System.out.println("b");
//            }
//
//            return totalRead;
//        }
//
//        private int readNextFrame() {
//            try {
//                Header frameHeader = bitstream.readFrame();
//                if (frameHeader == null) return -1;
//
//                SampleBuffer sampleBuffer = (SampleBuffer) decoder.decodeFrame(frameHeader, bitstream);
//
//                int samples = sampleBuffer.getBufferLength();
//                int pcmLength = samples * 2;
//
//                if (pcmLength > buffer.length) {
//                    buffer = new byte[pcmLength];
//                }
//
//                convertSamplesToBytes(sampleBuffer.getBuffer(), samples);
//
//                bytePointer = 0;
//                bufferLimit = pcmLength;
//
//                return pcmLength;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return -1;
//            }
//        }
//
//        // 将 int 型采样数组转为小端序 byte[]
//        private void convertSamplesToBytes(short[] samples, int len) {
//            int byteIndex = 0;
//
//            for (int i = 0; i < len; i++) {
//                short sample = samples[i];
//                // 小端序：低位在前
//                buffer[byteIndex++] = (byte) (sample & 0xFF);        // 低字节
//                buffer[byteIndex++] = (byte) ((sample >> 8) & 0xFF); // 高字节
//            }
//        }
//    }

    // 采样率与源一致 16bit 声道与源一致
    public static class Mp3ToPcmInputStream extends InputStream {

        private final Bitstream bitstream;
        private final Decoder decoder;
        private final DirectPcmBuffer outputBuffer;

        // 音频格式信息
        private int sampleRate = -1;
        private int channels = -1;
        private final int bitDepth = 16; // JLayer 固定解码为 16-bit

        private boolean isFinished = false;

        // 内部缓冲区状态
        private byte[] currentPcmData;
        private int pcmDataLength = 0;
        private int pcmDataPosition = 0;

        public Mp3ToPcmInputStream(InputStream inputStream) throws IOException {
            // 1. 处理 ID3v2 标签 (使用 Pushback 包装以防没有标签时可以退回数据)
            PushbackInputStream pbis = new PushbackInputStream(inputStream, 10);
//            skipId3v2(pbis);

            // 2. 初始化 JLayer 核心组件
            this.bitstream = new Bitstream(inputStream);
            this.decoder = new Decoder();

            // 初始化时不确定通道数，先用默认的2通道创建 Buffer，稍后读取首帧时重置
            this.outputBuffer = new DirectPcmBuffer(2);
            this.decoder.setOutputBuffer(this.outputBuffer);

            // 3. 读取第一帧以获取音频格式信息
            readNextFrame();
        }

        /**
         * 跳过 ID3v2 块
         */
        @Deprecated
        private void skipId3v2(PushbackInputStream in) throws IOException {
            byte[] header = new byte[10];
            int read = in.read(header, 0, 10);
            if (read == 10 && header[0] == 'I' && header[1] == 'D' && header[2] == '3') {
                // 解析 ID3v2 长度 (Sync-safe integer)
                int size = ((header[6] & 0x7F) << 21) |
                        ((header[7] & 0x7F) << 14) |
                        ((header[8] & 0x7F) << 7) |
                        (header[9] & 0x7F);

                // 跳过标签主体
                long skipped = 0;
                while (skipped < size) {
                    long s = in.skip(size - skipped);
                    if (s <= 0) break; // EOF
                    skipped += s;
                }
            } else if (read > 0) {
                // 不是 ID3v2，把读出来的数据塞回去
                in.unread(header, 0, read);
            }
        }

        /**
         * 核心逻辑：读取并解码下一帧 MP3 数据
         */
        private void readNextFrame() {
            if (isFinished) return;

            Header header = null;
            try {
                // 防御点 1：捕获 433 数组越界等 JLayer 解析垃圾数据时的崩溃
                header = bitstream.readFrame();
            } catch (BitstreamException | ArrayIndexOutOfBoundsException | NullPointerException e) {
                isFinished = true;
                return;
            }

            if (header == null) {
                isFinished = true;
                return;
            }

            // 首次读取，提取并锁定格式信息
            if (sampleRate == -1) {
                sampleRate = header.frequency();
                channels = (header.mode() == Header.SINGLE_CHANNEL) ? 1 : 2;
                // 重新配置 Buffer 的通道数
                outputBuffer.setChannels(channels);
            }

            try {
                // 解码当前帧
                outputBuffer.reset();
                decoder.decodeFrame(header, bitstream);

                // 从自定义 Buffer 中获取直接转换好的 byte[] 数据
                currentPcmData = outputBuffer.getBuffer();
                pcmDataLength = outputBuffer.getCurrentBufferSize();
                pcmDataPosition = 0;

            } catch (DecoderException e) {
                // 解码单帧失败（可能遇到损坏帧），忽略并继续，防止整个流崩溃
                pcmDataLength = 0;
            } finally {
                try {
                    // 必须关闭帧，否则无法寻找下一帧同步头
                    bitstream.closeFrame();
                } catch (Exception ignored) {}
            }
        }

        @Override
        public int read() throws IOException {
            byte[] b = new byte[1];
            int res = read(b, 0, 1);
            return (res == -1) ? -1 : (b[0] & 0xFF);
        }

        @Override
        public int read(byte @NonNull [] b, int off, int len) throws IOException {
            if (off < 0 || len < 0 || len > b.length - off) throw new IndexOutOfBoundsException();
            if (len == 0) return 0;

            int totalBytesRead = 0;

            // 防御点 2：不断循环，直到填满外部请求的 len，或者流彻底结束
            while (totalBytesRead < len) {
                // 如果当前缓冲区的帧数据已被读完，解码下一帧
                if (pcmDataPosition >= pcmDataLength) {
                    readNextFrame();
                    if (isFinished && pcmDataPosition >= pcmDataLength) {
                        break; // 彻底没有数据了
                    }
                    // 如果解码出错但流未结束（空帧），继续下一次循环
                    if (pcmDataLength == 0) continue;
                }

                int bytesAvailable = pcmDataLength - pcmDataPosition;
                int bytesToCopy = Math.min(bytesAvailable, len - totalBytesRead);

                System.arraycopy(currentPcmData, pcmDataPosition, b, off + totalBytesRead, bytesToCopy);

                pcmDataPosition += bytesToCopy;
                totalBytesRead += bytesToCopy;
            }

            return (totalBytesRead == 0 && isFinished) ? -1 : totalBytesRead;
        }

        @Override
        public void close() throws IOException {
            try {
                bitstream.close();
            } catch (BitstreamException ignored) {}
        }

        // --- Getter ---
        public int getSampleRate() { return sampleRate; }
        public int getChannels() { return channels; }
        public int getBitDepth() { return bitDepth; }

        // =================================================================================
        // 内部类：直接 PCM 转换缓冲区 (改良自 MP3SPI 的 DMAISObuffer)
        // 作用：拦截 JLayer 输出的 short，直接在底层转成 Little-Endian 的 byte[]
        // =================================================================================
        private static class DirectPcmBuffer extends Obuffer {
            private int channels;
            private final byte[] buffer;
            private final int[] pointers;

            public DirectPcmBuffer(int channels) {
                this.channels = channels;
                // 默认最大分配 OBUFFERSIZE * 2 (立体声)
                this.buffer = new byte[OBUFFERSIZE * 2];
                this.pointers = new int[2];
            }

            public void setChannels(int channels) {
                this.channels = channels;
            }

            @Override
            public void append(int channel, short value) {
                // Android 和 WAV 标准通常都是小端序 (Little-Endian)
                byte bFirstByte = (byte) (value & 0xFF);
                byte bSecondByte = (byte) ((value >>> 8) & 0xFF);

                int ptr = pointers[channel];
                buffer[ptr] = bFirstByte;
                buffer[ptr + 1] = bSecondByte;

                pointers[channel] += channels * 2;
            }

            public byte[] getBuffer() { return buffer; }

            public int getCurrentBufferSize() { return pointers[0]; }

            public void reset() {
                for (int i = 0; i < channels; i++) {
                    pointers[i] = i * 2; // 交错排列：L R L R
                }
            }

            // --- 必须实现但在此场景无用的方法 ---
            @Override public void close() {}
            @Override public void write_buffer(int val) {}
            @Override public void clear_buffer() {}
            @Override public void set_stop_flag() {}
        }
    }

    private static class ID3TagSizeRef {
        public int value;
    }
    private static BufferedInputStream skipID3Tag(InputStream is, ID3TagSizeRef ref) throws IOException {
        if (!is.markSupported()) {
            is = new BufferedInputStream(is);
        }

        is.mark(10);
        byte[] header = new byte[10];
        int read = is.read(header);

        if (read == 10 && new String(header, 0, 3).equals("ID3")) {
            // SyncSafe Integer (第 7, 8, 9, 10 字节)
            int size = (header[6] & 0x7F) << 21
                    | (header[7] & 0x7F) << 14
                    | (header[8] & 0x7F) << 7
                    | (header[9] & 0x7F);

            log.debug("Found ID3v2 label, skipped: {} bytes", size + 10);
            ref.value = size + 10;

            long skipped = 0;
            while (skipped < size) {
                skipped += is.skip(size - skipped);
            }
        } else {
            ref.value = 0;
            is.reset();
            log.debug("No ID3v2 label found");
        }
        if (is instanceof BufferedInputStream bis) return bis;
        else return new BufferedInputStream(is);
    }
}