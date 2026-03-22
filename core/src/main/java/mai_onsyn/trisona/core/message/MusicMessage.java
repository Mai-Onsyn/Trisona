package mai_onsyn.trisona.core.message;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicMessage extends Message {
    public UniversalPath audioPath;
    public String title;
    public String album;
    public List<String> otherNames;
    public List<Artist> artists;
    public UniversalPath coverPath;
    public int duration;
    public AudioMessage audioMessage;

    public MusicMessage() {
        this.audioPath = new UniversalPath();
        this.otherNames = new ArrayList<>();
        this.artists = new ArrayList<>();
        this.coverPath = new UniversalPath();
        this.audioMessage = new AudioMessage();
    }

    public MusicMessage(UniversalPath audioPath, String title, String album, List<String> otherNames, List<Artist> artists, UniversalPath coverPath, AudioMessage audioMessage) {
        this.audioPath = audioPath;
        this.title = title;
        this.album = album;
        this.otherNames = otherNames;
        this.artists = artists;
        this.coverPath = coverPath;
        this.audioMessage = audioMessage;
    }

    @Override
    public String toString() {
        return String.format("MusicMessage{audioPath=%s, name=%s, album=%s, otherNames=%s, artists=%s, coverPath=%s, audioMessage=%s}", audioPath, title, album, Arrays.toString(otherNames.toArray()), Arrays.toString(artists.toArray()), coverPath, audioMessage);
    }
}