package mai_onsyn.trisona.core.play;

import mai_onsyn.trisona.core.message.Music;
import mai_onsyn.trisona.core.message.PlayListInfo;

import java.util.ArrayList;

public class PlayList extends ArrayList<Music> {
    private final PlayListInfo info;
    protected int playingIndex = -1;

    public PlayList(PlayListInfo info) {
        this.info = info;
    }

    public Music play(int index) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());

        playingIndex = index;
        return get(index);
    }

    public int getPlayingIndex() {
        return playingIndex;
    }

    public void setPlayingIndex(int index) {
        playingIndex = index;
    }

    public PlayListInfo getInfo() {
        return info;
    }
}