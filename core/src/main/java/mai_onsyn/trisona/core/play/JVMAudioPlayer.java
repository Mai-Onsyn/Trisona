package mai_onsyn.trisona.core.play;

import mai_onsyn.trisona.core.decoder.RingBuffer;
import mai_onsyn.trisona.core.message.AudioMessage;
import mai_onsyn.trisona.core.message.MusicMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import static mai_onsyn.trisona.Global.APPLICATION_EXITED;
import static mai_onsyn.trisona.Global.SYSTEM_AUDIO_FORMAT;

public class JVMAudioPlayer extends AudioPlayer {
    private static final Logger log = LoggerFactory.getLogger(JVMAudioPlayer.class);
    private static final double MILLIS_PER_BUFFER = 23.333333333333333333333333333333333333333333333333;
    private SourceDataLine dataLine;
    private boolean isDataLineReady = false;

    private volatile boolean isSeeking = false;
    private volatile boolean shouldFlush = false;

    private FloatControl volumeControl;

    public JVMAudioPlayer() {
        super();
        startDataLine();

        Thread.ofVirtual().name("JVMAudioPlayer").start(() -> {
            while (!APPLICATION_EXITED) {
                playLoop();
            }
            dataLine.stop();
            dataLine.close();
        });
    }

    private void playLoop() {
        try {
            if (!isDataLineReady) {
                Thread.sleep(10);
                return;
            }

            if (shouldFlush) {
                dataLine.flush();
                shouldFlush = false;
            }

            if (volumeControl != null) {
                volumeControl.setValue((float) (20 * Math.log10(volume / 100.0)));
            }

            if (isPlaying) {
                RingBuffer.RingBufferRefState state = ringBuffer.read(chunkBuffer);
                switch (state) {
                    case SUCCESS -> {
                        dataLine.write(chunkBuffer, 0, chunkBuffer.length);
                        progressMillis += MILLIS_PER_BUFFER;
                        if (decoder.isDecodingFinished() && onMusicEnd != null) onMusicEnd.run();
                    }
                    case FAILED -> {}
                    case FLUSH_REQUIRED -> {}
                    case WAITING_INTERRUPTED -> {}
                }

                if (!dataLine.isRunning() && !isSeeking) dataLine.start();
            } else {
                dataLine.stop();
                Thread.sleep(10);
            }

        } catch (Exception e) {
            log.error("Error while playing audio: {}", e.getMessage());
        }
    }

    private void startDataLine() throws AudioPlayException {
        if (dataLine != null && dataLine.isActive()) throw new AudioPlayException("Data line is already active");
        try {
            dataLine = AudioSystem.getSourceDataLine(SYSTEM_AUDIO_FORMAT);
            dataLine.open(SYSTEM_AUDIO_FORMAT, chunkBuffer.length * 4);
            if (dataLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) dataLine.getControl(FloatControl.Type.MASTER_GAIN);
            }
            isDataLineReady = true;
        } catch (LineUnavailableException e) {
            isDataLineReady = false;
            throw new AudioPlayException(e.getMessage());
        }
    }

    @Override
    public void play() throws AudioPlayException {
        this.isPlaying = true;
    }

    @Override
    public void pause() throws AudioPlayException {
        this.isPlaying = false;
    }

    @Override
    public void setMusic(MusicMessage musicMessage) throws AudioPlayException {
        this.progressMillis = 0;

        decoder.pause();
        isSeeking = true;
        dataLine.stop();
        dataLine.flush();

        this.musicMessage = musicMessage;
        ringBuffer.flushStart();
        decoder.setMusicMessage(musicMessage);
        ringBuffer.flushComplete();

        isSeeking = false;
        decoder.resume();
    }

    @Override
    public void seek(int positionMillis) throws AudioPlayException {
        this.progressMillis = positionMillis;

//        long s = System.nanoTime();
        decoder.pause();
        isSeeking = true;

        dataLine.stop();
//        dataLine.flush();

        ringBuffer.flushStart();
        decoder.skipMillis(positionMillis);
        ringBuffer.flushComplete();

        isSeeking = false;
        decoder.resume();

        shouldFlush = true;
//        System.out.println("seek time: " + (System.nanoTime() - s));
        AudioMessage msg = decoder.getAudioMessage();
        if (msg != null) {
//            System.out.println(1000.0 * msg.byteLength / msg.getBPS());
            this.progressMillis = Math.min((double) msg.pcmByteLength / msg.getBPS() * 1000.0, positionMillis);
        }
    }
}
