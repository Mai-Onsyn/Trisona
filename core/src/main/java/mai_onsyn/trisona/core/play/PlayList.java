package mai_onsyn.trisona.core.play;

import mai_onsyn.trisona.core.message.MusicMessage;
import mai_onsyn.trisona.core.message.PlayListMessage;

import java.util.ArrayList;

public class PlayList extends ArrayList<MusicMessage> {
    private final PlayListMessage info;
    protected int playingIndex = -1;

    public PlayList(PlayListMessage info) {
        this.info = info;
    }

    public MusicMessage play(int index) {
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

    public PlayListMessage getInfo() {
        return info;
    }
}
