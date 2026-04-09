package mai_onsyn.trisona.core.message;

import com.alibaba.fastjson2.JSONObject;
import mai_onsyn.trisona.core.data.MusicQuality;

import java.util.*;

public class MusicMessage extends Message {
    public long id = -1;
    public UniversalPath audioPath;
    public String title;
    public List<String> alias;
    public List<Artist> artists;
    public long albumID = -1;
    public double popularity = -1;
    public int duration = -1;
    public boolean fee = false;
    private MusicQuality currentQuality;
    private final List<MusicQuality> supportsQualities;
    private final Map<MusicQuality, AudioMessagePackage> aMsg;

    public MusicMessage() {
        this.audioPath = new UniversalPath();
        this.alias = new ArrayList<>();
        this.artists = new ArrayList<>();
//        this.coverPath = new UniversalPath();
        this.aMsg = new EnumMap<>(MusicQuality.class);
        this.supportsQualities = new ArrayList<>();
        this.currentQuality = MusicQuality.STANDARD;
//        aMsg.put(Music.MusicQuality.NATIVE, new AudioMessagePackage());
    }

    public void enableQuality(MusicQuality quality) {
        aMsg.put(quality, new AudioMessagePackage());
        supportsQualities.add(quality);
        currentQuality = quality;
    }

    public AudioMessagePackage getAudioMessage(MusicQuality quality) {
        return aMsg.getOrDefault(quality, aMsg.getOrDefault(supportsQualities.getFirst(), null));
    }

    public AudioMessage getCompleteAMsg() {
        aMsg.entrySet().forEach(System.out::println);
        if (!aMsg.containsKey(currentQuality)) return null;
        return aMsg.get(currentQuality).getCompleteAMsg();
    }

    public void setaMsgPlaying(AudioMessage aMsgPlaying) {
        if (aMsg.containsKey(currentQuality))
            this.aMsg.get(currentQuality).setPlaying(aMsgPlaying);
    }

    public AudioMessage getaMsgPlaying() {
        if (aMsg.containsKey(currentQuality))
            return this.aMsg.get(currentQuality).getPlaying();
        return null;
    }

    public List<MusicQuality> getSupportsQualities() {
        return supportsQualities;
    }

    public void setaMsgNetwork(AudioMessage aMsgNetwork) {
        if (aMsg.containsKey(currentQuality))
            this.aMsg.get(currentQuality).setNetwork(aMsgNetwork);
    }

    public void setaMsgHeader(AudioMessage aMsgHeader) {
//        aMsg.entrySet().forEach(
//                System.out::println
//        );
//        supportsQualities.forEach(System.out::println);
        if (aMsg.containsKey(currentQuality))
            this.aMsg.get(currentQuality).setHeader(aMsgHeader);
    }

    public Map<MusicQuality, AudioMessagePackage> getAllAudioMessages() {
        return aMsg;
    }

    public static class AudioMessagePackage {
        private AudioMessage header, network, playing, sql;

        public AudioMessagePackage() {}

        public AudioMessagePackage(AudioMessage sql) {
            this.sql = sql;
        }

        public AudioMessage getCompleteAMsg() {
//            if (network == null) return playing;
//            if (header == null) return network.merge(playing);
//            if (sql == null) return header.merge(network).merge(playing);
//            return sql.merge(header).merge(network).merge(playing);
            if (sql != null) return sql.merge(header).merge(network).merge(playing);
            else if (header != null) return header.merge(network).merge(playing);
            else if (network != null) return network.merge(playing);
            return playing;
        }

        public AudioMessage getHeader() {return header;}
        public AudioMessage getNetwork() {return network;}
        public AudioMessage getPlaying() {return playing;}
        public AudioMessage getSql() {return sql;}
        public void setHeader(AudioMessage header) {this.header = header;}
        public void setNetwork(AudioMessage network) {this.network = network;}
        public void setPlaying(AudioMessage playing) {this.playing = playing;}

        @Override
        public String toString() {
            return getCompleteAMsg().toString();
        }
    }

    public MusicQuality getCurrentQuality() {
        return currentQuality;
    }

    public void setCurrentQuality(MusicQuality currentQuality) {
        this.currentQuality = currentQuality;
    }

    @Override
    public String toString() {
        return String.format(
                "MusicMessage{id=%d, audioPath=%s, title=%s, alias=%s, artists=%s, albumID=%s, popularity=%.2f, duration=%d, fee=%b, currentQuality=%s, supportsQualities=%s, allAudioMessages=%s}",
                id, audioPath.toString(), title, Arrays.toString(alias.toArray()),
                Arrays.toString(artists.toArray()), albumID, popularity, duration, fee, currentQuality, Arrays.toString(supportsQualities.toArray()), new JSONObject(aMsg)
        );
    }
}