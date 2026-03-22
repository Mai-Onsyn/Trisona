package mai_onsyn.trisona.core.play;

import mai_onsyn.trisona.core.decoder.DecodeHandler;
import mai_onsyn.trisona.core.decoder.RingBuffer;
import mai_onsyn.trisona.core.message.AudioMessage;
import mai_onsyn.trisona.core.message.MusicMessage;

public abstract class AudioPlayer {
    protected final RingBuffer ringBuffer;
    protected final DecodeHandler decoder;
    protected final byte[] chunkBuffer;

    protected MusicMessage musicMessage;
    protected double progressMillis; // in milliseconds
    protected boolean isPlaying;
    protected Runnable onMusicEnd;
    protected int volume = 50;

    protected AudioPlayer() {
        this.ringBuffer = new RingBuffer(32, 4096);
        this.chunkBuffer = new byte[4096];
        this.decoder = new DecodeHandler(ringBuffer);

        this.decoder.setOnDecodingError(e -> this.isPlaying = false);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public MusicMessage getMusicMessage() throws AudioPlayException {
        return musicMessage;
    }

    public double getProgressMillis() throws AudioPlayException {
        return progressMillis;
    }

    public void setOnMusicEnd(Runnable onMusicEnd) {
        this.onMusicEnd = onMusicEnd;
    }

    public abstract void play() throws AudioPlayException;

    public abstract void pause() throws AudioPlayException;

    public abstract void setMusic(MusicMessage musicMessage) throws AudioPlayException;

    public abstract void seek(int positionMillis) throws AudioPlayException;

    public void setVolume(int value) {
        if (volume > 100 || volume < 0)
            throw new IllegalArgumentException("Volume must be between 0 and 100");
        this.volume = value;
    }

    public int getVolume() {
        return volume;
    }

    public AudioMessage getPlayingAudioMessage() {
        return decoder.getAudioMessage();
    }
}
