package mai_onsyn.trisona.core.data;

import mai_onsyn.trisona.core.message.MusicMessage;

public class Music {
    private long neID = -1;
    private MusicMessage info;

    public long getNeID() {
        return neID;
    }

    public void setNeID(long neID) {
        this.neID = neID;
    }

    public Music(MusicMessage info) {
        this.info = info;
    }

    public MusicMessage getInfo() {
        return info;
    }

    public void setInfo(MusicMessage info) {
        this.info = info;
    }

    public enum MusicQuality {
        STANDARD("standard", 70),
        HIGHER("higher", 80),
        EX_HIGH("exhigh", 90),
        LOSSLESS("lossless", 100),
        HI_RES("hires", 60),
        JY_EFFECT("jyeffect", 50),
        SKY("sky", 40),
        DOLBY("dolby", 30),
        JY_MASTER("jymaster", 20),
        NATIVE("native", 114514);

        private final String id;
        private final int priority;

        MusicQuality(String id, int priority) {
            this.id = id;
            this.priority = priority;
        }

        public String getId() {
            return id;
        }

        public int getPriority() {
            return priority;
        }
    }
}