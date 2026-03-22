package mai_onsyn.trisona.core.play;

import mai_onsyn.trisona.core.data.Music;
import mai_onsyn.trisona.core.message.AudioMessage;
import mai_onsyn.trisona.core.utils.MusicCallback;

public class TrisonaPlayer {

    private final AudioPlayer player;
    private final PlayQueue queue;

    private MusicCallback onQueueEnd;
    private MusicCallback onMusicEnd;
    private MusicCallback onMusicStart;

    private Music currentMusic;

    public TrisonaPlayer(String platform) {
        switch (platform) {
            case "desktop" -> player = new JVMAudioPlayer();
            case "android" -> player = new AndroidAudioPlayer();
            default -> throw new IllegalArgumentException("Platform not supported");
        }
        queue = new PlayQueue();

        player.setOnMusicEnd(() -> {
            if (onMusicEnd != null) onMusicEnd.run(currentMusic);
            this.next();
        });
    }

    public void setPlayList(PlayList list) {
        list.playingIndex = 0;
        queue.setPlayList(list);
        specifyMusic(0);
    }

    public void next() {
        Music next = queue.next();
        if (next == null) {
            if (onQueueEnd != null) onQueueEnd.run(currentMusic);
            return;
        }

        currentMusic = next;
        player.setMusic(next.getInfo());
        if (onMusicStart != null) onMusicStart.run(currentMusic);
    }

    public void previous() {
        Music previous = queue.previous();
        if (previous == null) {
            return;
        }

        currentMusic = previous;
        player.setMusic(previous.getInfo());
        if (onMusicStart != null) onMusicStart.run(currentMusic);
    }

    public void play() {
        player.play();
    }

    public void pause() {
        player.pause();
    }

    public void seek(int ms) {
        player.seek(ms);
    }

    public void specifyMusic(int index) {
        Music music = queue.specify(index);
        if (music == null) {
            return;
        }
        player.setMusic(music.getInfo());
        if (onMusicStart != null) onMusicStart.run(music);
    }

    public void setVolume(int volume) {
        player.setVolume(volume);
    }

    public int getVolume() {
        return player.getVolume();
    }

    public float getPlayingDuration() {
        AudioMessage msg = player.getPlayingAudioMessage();
        if (msg == null) {
            return 1f;
        }

        return (float) msg.pcmByteLength / msg.getBPS() * 1000f;
    }

    public float getPlayingPosition() {
        return (float) player.getProgressMillis();
    }

    public void setOnQueueEnd(MusicCallback onQueueEnd) {
        this.onQueueEnd = onQueueEnd;
    }
    public void setOnMusicEnd(MusicCallback onMusicEnd) {
        this.onMusicEnd = onMusicEnd;
    }
    public void setOnMusicStart(MusicCallback onMusicStart) {
        this.onMusicStart = onMusicStart;
    }
}
