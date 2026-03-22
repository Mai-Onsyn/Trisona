package mai_onsyn.trisona.core.data;

import mai_onsyn.trisona.core.message.MusicMessage;

public class Music {
    private MusicMessage info;

    public Music(MusicMessage info) {
        this.info = info;
    }

    public MusicMessage getInfo() {
        return info;
    }

    public void setInfo(MusicMessage info) {
        this.info = info;
    }
}
