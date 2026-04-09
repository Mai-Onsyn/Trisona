package mai_onsyn.trisona.core.data;

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

    public static MusicQuality byId(String id) {
        for (MusicQuality quality : values()) {
            if (quality.getId().equals(id)) {
                return quality;
            }
        }
        return null;
    }
}