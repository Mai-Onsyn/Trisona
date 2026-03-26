package mai_onsyn.trisona.core.message;

import mai_onsyn.trisona.core.data.Music;

import java.util.*;

public class MusicMessage extends Message {
    public UniversalPath audioPath;
    public String title;
    public String album;
    public List<String> otherNames;
    public List<Artist> artists;
    public UniversalPath coverPath;
    public int duration;
    public Music.MusicQuality currentQuality;
    private final Map<Music.MusicQuality, AudioMessagePackage> aMsg;

    public MusicMessage() {
        this.audioPath = new UniversalPath();
        this.otherNames = new ArrayList<>();
        this.artists = new ArrayList<>();
        this.coverPath = new UniversalPath();
        this.aMsg = new EnumMap<>(Music.MusicQuality.class);
        aMsg.put(Music.MusicQuality.NATIVE, new AudioMessagePackage());
    }

    public AudioMessage getCompleteAMsg() {
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

    public void setaMsgNetwork(AudioMessage aMsgNetwork) {
        if (aMsg.containsKey(currentQuality))
            this.aMsg.get(currentQuality).setNetwork(aMsgNetwork);
    }

    public void setaMsgHeader(AudioMessage aMsgHeader) {
        if (aMsg.containsKey(currentQuality))
            this.aMsg.get(currentQuality).setHeader(aMsgHeader);
    }

    public static class AudioMessagePackage {
        private AudioMessage header, network, playing;

        public AudioMessage getCompleteAMsg() {
            if (network == null) return playing;
            if (header == null) return network.merge(playing);
            return header.merge(network).merge(playing);
        }

        public AudioMessage getHeader() {
            return header;
        }
        public void setHeader(AudioMessage header) {
            this.header = header;
        }
        public AudioMessage getNetwork() {
            return network;
        }
        public void setNetwork(AudioMessage network) {
            this.network = network;
        }
        public AudioMessage getPlaying() {
            return playing;
        }
        public void setPlaying(AudioMessage playing) {
            this.playing = playing;
        }

        @Override
        public String toString() {
            return getCompleteAMsg().toString();
        }
    }

    @Override
    public String toString() {
        return String.format(
                "MusicMessage{audioPath=%s, name=%s, album=%s, otherNames=%s, artists=%s, coverPath=%s, audioMessage=%s}",
                audioPath, title, album,
                Arrays.toString(otherNames.toArray()),
                Arrays.toString(artists.toArray()),
                coverPath, getCompleteAMsg()
        );
    }
}