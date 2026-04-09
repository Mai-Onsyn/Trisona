package mai_onsyn.trisona.core.message;

import com.alibaba.fastjson2.JSONObject;
import mai_onsyn.trisona.core.data.MusicQuality;

import java.util.*;

public class Music extends Message {
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
    private final Map<MusicQuality, AudioMessagePackage> audios;

    public Music() {
        this.audioPath = new UniversalPath();
        this.alias = new ArrayList<>();
        this.artists = new ArrayList<>();
//        this.coverPath = new UniversalPath();
        this.audios = new EnumMap<>(MusicQuality.class);
        this.supportsQualities = new ArrayList<>();
        this.currentQuality = MusicQuality.STANDARD;
//        aMsg.put(Music.MusicQuality.NATIVE, new AudioMessagePackage());
    }

    public void enableQuality(MusicQuality quality) {
        audios.put(quality, new AudioMessagePackage());
        supportsQualities.add(quality);
        currentQuality = quality;
    }

    public AudioMessagePackage getAudioMessage(MusicQuality quality) {
        return audios.getOrDefault(quality, audios.getOrDefault(supportsQualities.getFirst(), null));
    }

    public Audio getCompleteAudioInfo() {
//        aMsg.forEach((key, value) -> {
//            System.out.println(key);
//            System.out.println(value);
//        });
        if (!audios.containsKey(currentQuality)) return null;
        return audios.get(currentQuality).getCompleteAudioInfo();
    }

    public void setaAudioPlaying(Audio aMsgPlaying) {
        if (audios.containsKey(currentQuality))
            this.audios.get(currentQuality).setPlaying(aMsgPlaying);
    }

    public Audio getAudioPlaying() {
        if (audios.containsKey(currentQuality))
            return this.audios.get(currentQuality).getPlaying();
        return null;
    }

    public List<MusicQuality> getSupportsQualities() {
        return supportsQualities;
    }

    public void setAudioNetwork(Audio aMsgNetwork) {
        if (audios.containsKey(currentQuality))
            this.audios.get(currentQuality).setNetwork(aMsgNetwork);
    }

    public void setaAudioHeader(Audio aMsgHeader) {
        if (audios.containsKey(currentQuality))
            this.audios.get(currentQuality).setHeader(aMsgHeader);
    }

    public Map<MusicQuality, AudioMessagePackage> getAllAudioMessages() {
        return audios;
    }

    public static class AudioMessagePackage {
        private Audio header, network, playing, sql;

        public AudioMessagePackage() {}

        public AudioMessagePackage(Audio sql) {
            this.sql = sql;
        }

        public Audio getCompleteAudioInfo() {
            if (sql != null) return sql.merge(header).merge(network).merge(playing);
            else if (header != null) return header.merge(network).merge(playing);
            else if (network != null) return network.merge(playing);
            return playing;
        }

        public Audio getHeader() {return header;}
        public Audio getNetwork() {return network;}
        public Audio getPlaying() {return playing;}
        public Audio getSql() {return sql;}
        public void setHeader(Audio header) {this.header = header;}
        public void setNetwork(Audio network) {this.network = network;}
        public void setPlaying(Audio playing) {this.playing = playing;}

        @Override
        public String toString() {
            Audio completeAudio = getCompleteAudioInfo();
            return completeAudio != null ? completeAudio.toString() : "Uninitialized_AudioPackage";
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
                "Music{id=%d, audioPath=%s, title=%s, alias=%s, artists=%s, albumID=%s, popularity=%.2f, duration=%d, fee=%b, currentQuality=%s, supportsQualities=%s, allAudios=%s}",
                id, audioPath.toString(), title, Arrays.toString(alias.toArray()),
                Arrays.toString(artists.toArray()), albumID, popularity, duration, fee, currentQuality, Arrays.toString(supportsQualities.toArray()), new JSONObject(audios)
        );
    }
}