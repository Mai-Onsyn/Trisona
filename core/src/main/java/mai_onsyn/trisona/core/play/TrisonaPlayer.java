package mai_onsyn.trisona.core.play;

import mai_onsyn.trisona.core.message.Audio;
import mai_onsyn.trisona.core.message.Music;

import java.util.function.Consumer;

public class TrisonaPlayer {

    private final AudioPlayer player;
    private final PlayQueue queue = new PlayQueue();

    private Consumer<Music> onQueueEnd;
    private Consumer<Music> onMusicEnd;
    private Consumer<Music> onMusicStart;
    private Consumer<Music> onMusicChanged;
    private Consumer<Boolean> onPlayStateChanged = b -> {};

    private Music currentMusic;

    public TrisonaPlayer(String platform) {
        this.player = createPlayer(platform);

        player.setOnMusicEnd(this::handleMusicEnd);
    }

    /* -------------------- 初始化 -------------------- */

    private AudioPlayer createPlayer(String platform) {
        return switch (platform) {
            case "desktop" -> new JVMAudioPlayer();
            case "android" -> new AndroidAudioPlayer();
            default -> throw new IllegalArgumentException("Unsupported platform: " + platform);
        };
    }

    /* -------------------- 核心逻辑 -------------------- */

    private void handleMusicEnd() {
        safeCall(onMusicEnd, currentMusic);

        if (queue.getPlayMode() == PlayQueue.PlayMode.REPEAT) {
            seek(0);
            return;
        }

        next();
    }

    private void switchTo(Music music) {
        if (music == null) return;

        currentMusic = music;
        player.setMusic(music);
        safeCall(onMusicStart, music);
        safeCall(onMusicChanged, music);
    }

    private void safeCall(Consumer<Music> consumer, Music music) {
        if (consumer != null) consumer.accept(music);
    }

    /* -------------------- 播放控制 -------------------- */

    public void play() {
        player.play();
        onPlayStateChanged.accept(true);
    }

    public void pause() {
        player.pause();
        onPlayStateChanged.accept(false);
    }

    public void seek(int ms) {
        player.seek(ms);
    }

    /* -------------------- 队列控制 -------------------- */

    public void setPlayList(PlayList list) {
        list.playingIndex = 0;
        queue.setPlayList(list);
        switchTo(queue.specify(0));
    }

    public void next() {
        Music next = queue.next();
        if (next == null) {
            safeCall(onQueueEnd, currentMusic);
            return;
        }
        switchTo(next);
    }

    public void previous() {
        switchTo(queue.previous());
    }

    public void specifyMusic(int index) {
        switchTo(queue.specify(index));
    }

    public void specifyMusic(Music music) {
        if (music == null || !queue.contains(music)) {
            throw new IllegalArgumentException("Music not in queue");
        }
        switchTo(music);
    }

    public void setPlayMode(int mode) {
        queue.setPlayMode(PlayQueue.PlayMode.values()[mode]);
    }

    /* -------------------- 状态查询 -------------------- */

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public Music getCurrentMusic() {
        return currentMusic;
    }

    public float getPlayingPosition() {
        return (float) player.getProgressMillis();
    }

    public float getPlayingDuration() {
        Audio msg = player.getPlayingAudioMessage();
        if (msg == null) return 1f;
        return (float) msg.pcmByteLength / (4 * msg.sampleRate) * 1000f;
    }

    public int getVolume() {
        return player.getVolume();
    }

    /* -------------------- 设置 -------------------- */

    public void setVolume(int volume) {
        player.setVolume(volume);
    }

    public void setOnPlayStateChanged(Consumer<Boolean> listener) {
        this.onPlayStateChanged = listener != null ? listener : b -> {};
    }

    public void setOnQueueEnd(Consumer<Music> listener) {
        this.onQueueEnd = listener;
    }

    public void setOnMusicChanged(Consumer<Music> listener) {
        this.onMusicChanged = listener;
    }

    public void setOnMusicEnd(Consumer<Music> listener) {
        this.onMusicEnd = listener;
    }

    public void setOnMusicStart(Consumer<Music> listener) {
        this.onMusicStart = listener;
    }
}






//
//public class TrisonaPlayer {
//
//    private final AudioPlayer player;
//    private final PlayQueue queue;
//
//    private Consumer<Music> onQueueEnd;
//    private Consumer<Music> onMusicEnd;
//    private Consumer<Music> onMusicStart;
//
//    private Consumer<Boolean> onPlayStateChange;
//
//    private Music currentMusic;
//
//    public TrisonaPlayer(String platform) {
//        switch (platform) {
//            case "desktop" -> player = new JVMAudioPlayer();
//            case "android" -> player = new AndroidAudioPlayer();
//            default -> throw new IllegalArgumentException("Platform not supported");
//        }
//        queue = new PlayQueue();
//
//        player.setOnMusicEnd(() -> {
//            if (onMusicEnd != null) onMusicEnd.accept(currentMusic);
//            if (queue.getPlayMode() == PlayQueue.PlayMode.REPEAT) {
//                seek(0);
//                return;
//            }
//            this.next();
//        });
//    }
//
//    public void setPlayList(PlayList list) {
//        list.playingIndex = 0;
//        queue.setPlayList(list);
//        specifyMusic(0);
//    }
//
//    public void next() {
//        Music next = queue.next();
//        if (next == null) {
//            if (onQueueEnd != null) onQueueEnd.accept(currentMusic);
//            return;
//        }
//
//        currentMusic = next;
//        player.setMusic(next);
//        if (onMusicStart != null) onMusicStart.accept(currentMusic);
//    }
//
//    public void previous() {
//        Music previous = queue.previous();
//        if (previous == null) {
//            return;
//        }
//
//        currentMusic = previous;
//        player.setMusic(previous);
//        if (onMusicStart != null) onMusicStart.accept(currentMusic);
//    }
//
//    public void play() {
//        player.play();
//        onPlayStateChange.accept(true);
//    }
//
//    public void pause() {
//        player.pause();
//        onPlayStateChange.accept(false);
//    }
//
//    public void setOnPlayStateChange(Consumer<Boolean> listener) {
//        onPlayStateChange = listener;
//    }
//
//    public void seek(int ms) {
//        player.seek(ms);
//    }
//
//    public void specifyMusic(int index) {
//        Music music = queue.specify(index);
//        if (music == null) {
//            return;
//        }
//        player.setMusic(music);
//        currentMusic = music;
//        if (onMusicStart != null) onMusicStart.accept(music);
//    }
//
//    public void specifyMusic(Music music) {
//        if (music != null && queue.contains(music)) {
//            player.setMusic(music);
//            currentMusic = music;
//            if (onMusicStart != null) onMusicStart.accept(music);
//        } else throw new IllegalArgumentException("Music is not in the queue");
//    }
//
//    public void setVolume(int volume) {
//        player.setVolume(volume);
//    }
//
//    public int getVolume() {
//        return player.getVolume();
//    }
//
//    public float getPlayingDuration() {
//        Audio msg = player.getPlayingAudioMessage();
//        if (msg == null) {
//            return 1f;
//        }
//        return (float) msg.pcmByteLength / (4*msg.sampleRate) * 1000f;
//    }
//
//    public boolean isPlaying() {
//        return player.isPlaying();
//    }
//
//    public Music getCurrentMusic() {
//        return currentMusic;
//    }
//
//    public void setPlayMode(int mode) {
//        queue.setPlayMode(PlayQueue.PlayMode.values()[mode]);
//    }
//
//    public float getPlayingPosition() {
//        return (float) player.getProgressMillis();
//    }
//
//    public void setOnQueueEnd(Consumer<Music> onQueueEnd) {
//        this.onQueueEnd = onQueueEnd;
//    }
//    public void setOnMusicEnd(Consumer<Music> onMusicEnd) {
//        this.onMusicEnd = onMusicEnd;
//    }
//    public void setOnMusicStart(Consumer<Music> onMusicStart) {
//        this.onMusicStart = onMusicStart;
//    }
//}