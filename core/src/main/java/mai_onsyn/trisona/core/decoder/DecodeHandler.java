package mai_onsyn.trisona.core.decoder;

import mai_onsyn.trisona.Global;
import mai_onsyn.trisona.core.message.AudioMessage;
import mai_onsyn.trisona.core.message.MusicMessage;
import mai_onsyn.trisona.core.message.UniversalPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class DecodeHandler {

    private static final Logger log = LoggerFactory.getLogger(DecodeHandler.class);
    private final Map<AudioMessage.Encoding, AudioDecoder> decoders;
    private final RingBuffer ringBuffer;
    private final byte[] buffer;
    private volatile MusicMessage mMsg;
//    private AudioMessage audioMessage;
    private DataInputStream dataStream;
    private long dataStreamSize;

    private volatile boolean streamReady = false;
    private volatile boolean touchedEndOfStream = false;

    private final Object SEEK_LOCK = new Object();
    private volatile int pendingSeekMs = -1;
    private volatile boolean isPaused = false;

    private volatile Runnable seekCallback;
    private OnError onDecodingError;

    public DecodeHandler(RingBuffer ringBuffer) {
        this.ringBuffer = ringBuffer;
        this.buffer = new byte[ringBuffer.getChunkSize()];

        this.decoders = new EnumMap<>(AudioMessage.Encoding.class);
        this.decoders.put(AudioMessage.Encoding.WAV, new WavDecoder());
        this.decoders.put(AudioMessage.Encoding.MP3, new Mp3Decoder());
        this.decoders.put(AudioMessage.Encoding.FLAC, new FlacDecoder());
        this.decoders.put(AudioMessage.Encoding.OGG, new OggDecoder());

        new Thread(() -> {
            while (!Global.APPLICATION_EXITED) {
                decodeLoop();
            }
        }, "DecoderThread").start();
    }

    private void decodeLoop() {
        try {
            Thread.sleep(10);

            if (seekCallback != null) {
                seekCallback.run();
                seekCallback = null;
            }

            if (mMsg == null && isPaused) return;

            if (touchedEndOfStream) dataStream.close();

            if (!streamReady) {
                synchronized (SEEK_LOCK) {
                    if (dataStream != null) dataStream.close();
                    if (buildInputStream()) {
                        AudioDecoder decoder = decoders.get(mMsg.getCompleteAMsg().encoding);
                        this.mMsg.setaMsgPlaying(decoder.readyAudioStream(dataStream, dataStreamSize, is -> this.dataStream = is));
                        this.dataStream = decoder.decode(dataStream, mMsg.getCompleteAMsg());

                        boolean seekOverStream = false;
                        if (pendingSeekMs != -1) {
                            var ref = decoder.seek(dataStream, pendingSeekMs);
                            switch (ref) {
                                case SUCCESS -> {}
                                case FAILED -> {}
                                case OVER_STREAM -> seekOverStream = true;
                            }
                            pendingSeekMs = -1;
                        }

                        touchedEndOfStream = seekOverStream;
                        streamReady = true;
                        SEEK_LOCK.notifyAll();
                    }
                }
            }

            if (streamReady && !touchedEndOfStream) {
                AudioDecoder decoder = decoders.get(mMsg.getCompleteAMsg().encoding);
                var streamStateRef = decoder.readStream(dataStream, buffer);
                switch (streamStateRef) {
                    case SUCCESS -> {
                        var ringStateRef = ringBuffer.write(buffer);
                        switch (ringStateRef) {
                            case SUCCESS -> {}
                            case FAILED -> {}
                            case FLUSH_REQUIRED -> {
                                streamReady = false;
                                touchedEndOfStream = false;
                            }
                            case WAITING_INTERRUPTED -> {}
                        }
                    }
                    case FAILED -> {}
                    case OVER_STREAM -> touchedEndOfStream = true;
                }
            }
        } catch (NullPointerException ignored) {
        } catch (Exception e) {
            onDecodingError.run(e);
            log.error("Error while decoding audio stream: {}", e.getMessage());
        }
    }

    private boolean buildInputStream() throws AudioDecodeException {
        UniversalPath.UrlPath path = mMsg.audioPath.getPath();

        try {
            switch (path.type()) {
                case NATIVE -> {
                    FileInputStream fis = new FileInputStream(path.url());
                    this.dataStreamSize = fis.getChannel().size();
                    var detectionResult = AudioDetector.detectFormatInStream(fis);
                    if (mMsg.getCompleteAMsg() == null) this.mMsg.setaMsgPlaying(new AudioMessage());
                    this.mMsg.getaMsgPlaying().encoding = detectionResult.encoding();
//                    this.mMsg.aMsgPlaying = this.audioMessage;
                    this.dataStream = new DataInputStream(detectionResult.rewindableStream());
                    return true;
                }
                case NETWORK -> throw new RuntimeException("Network path is not supported yet");
                default -> throw new AudioDecodeException("Unsupported path type: " + path.type());
            }
        } catch (IOException e) {
            return false;
//            throw new AudioDecodeException(e.getMessage());
        }
    }

    public void skipMillis(int millis) {
        seekCallback = () -> {
            streamReady = false;
            pendingSeekMs = millis;
        };
    }

    public AudioMessage getAudioMessage() {
        return mMsg.getCompleteAMsg();
    }

    public void setMusicMessage(MusicMessage mMsg) {
        synchronized (SEEK_LOCK) {
            this.mMsg = mMsg;
            streamReady = false;
            touchedEndOfStream = false;
        }
    }

    public interface OnError {
        void run(Exception e);
    }

    public void setOnDecodingError(OnError onError) {
        this.onDecodingError = onError;
    }

    public boolean isDecodingFinished() {
        return touchedEndOfStream && ringBuffer.getAvailableDataSize() == 0;
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }
}
