package mai_onsyn.trisona.core.play;

import mai_onsyn.trisona.core.message.Music;
import mai_onsyn.trisona.core.utils.HistoryBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static mai_onsyn.trisona.Global.RANDOM;

public class PlayQueue extends PlayList {

    private static final Logger log = LoggerFactory.getLogger(PlayQueue.class);
    private final List<Integer> shuffleIndexList;
    private final HistoryBuffer<Integer> historyBuffer;
    private PlayMode playMode = PlayMode.SEQUENCE;

    public enum PlayMode {
//         0       1      2        3       4
        SEQUENCE, LOOP, REPEAT, SHUFFLE, RANDOM
    }

    public PlayQueue() {
        super(null);
        historyBuffer = new HistoryBuffer<>(32);
        shuffleIndexList = new ArrayList<>();
    }

    public void setPlayList(PlayList playList) {
        super.clear();
        super.addAll(playList);
        shuffleIndexList.clear();

        if (playList.playingIndex == -1)
            super.playingIndex = 0;
        else super.playingIndex = playList.playingIndex;

        historyBuffer.record(playingIndex);
    }

    public Music next() { //自动播放和用户调用
        Integer next = historyBuffer.readNext();
        if (next == null) {
            int nextIndex = nextIndex();
            if (nextIndex == -1) return null;

            historyBuffer.record(nextIndex);
            return play(nextIndex);
        } else return play(next);
    }

    public Music previous() { //only用户调用
        Integer previous = historyBuffer.readPrevious();
        if (previous == null) {
            if (playingIndex == 0) return null;
            return play(playingIndex - 1);
        } else return play(previous);
    }

    private int nextIndex() {
        return switch (playMode) {
            case SEQUENCE, REPEAT -> {
                if (playingIndex == size() - 1) {
                    yield -1;
                } else yield playingIndex + 1;
            }
            case LOOP -> (playingIndex + 1) % size();
            case SHUFFLE -> {
                if (shuffleIndexList.isEmpty())
                    shuffle();
                yield shuffleIndexList.removeFirst();
            }
            case RANDOM -> RANDOM.nextInt(size());
            case null -> -1;
        };
    }

    public Music specify(int index) { //只由用户调用
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException("Specify index out of bound: " + index + " in [0, " + size() + "]");

        historyBuffer.record(index);
        return play(index);
    }

    public void setPlayMode(PlayMode playMode) {
        this.playMode = playMode;
//        log.debug("Play mode set to {}", playMode);
    }

    public PlayMode getPlayMode() {
        return playMode;
    }

    private void shuffle() {
        int size = size();
        shuffleIndexList.clear();
        for (int i = 0; i < size; i++)
            shuffleIndexList.add(i);

        for (int i = 0; i < size * 4; i++) {
            int idx1 = RANDOM.nextInt(size);
            int idx2 = RANDOM.nextInt(size);
            if (idx1 != idx2) {
                int tmp = shuffleIndexList.get(idx1);
                shuffleIndexList.set(idx1, shuffleIndexList.get(idx2));
                shuffleIndexList.set(idx2, tmp);
            }
        }
    }
}
