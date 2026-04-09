package mai_onsyn.trisona.core.decoder;

import mai_onsyn.trisona.Global;
import mai_onsyn.trisona.core.data.Album;
import mai_onsyn.trisona.core.data.MusicQuality;
import mai_onsyn.trisona.core.message.Artist;
import mai_onsyn.trisona.core.message.Audio;
import mai_onsyn.trisona.core.message.Music;
import mai_onsyn.trisona.core.sql.AlbumSQL;
import mai_onsyn.trisona.core.sql.AudioSQL;
import mai_onsyn.trisona.core.sql.MusicSQL;
import mai_onsyn.trisona.core.sql.SQLPackage;
import mai_onsyn.trisona.core.utils.HashUtil;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class AudioDetector {
    static {
        java.util.logging.Logger.getLogger("org.jaudiotagger").setLevel(Level.SEVERE);
    }
    private static final Logger log = LoggerFactory.getLogger(AudioDetector.class);

    public record DetectionResult(Audio.Encoding encoding, InputStream rewindableStream) {}

    public static DetectionResult detectFormatInStream(InputStream originalIn) throws IOException {
        InputStream stream = originalIn.markSupported() ?
                originalIn : new BufferedInputStream(originalIn);

        stream.mark(128);

        byte[] header = new byte[12];
        int read = stream.read(header);

        Audio.Encoding type = Audio.Encoding.UNKNOWN;
        if (read >= 4) {
            type = performHeaderCheck(header, read);
        }

        stream.reset();

        return new DetectionResult(type, stream);
    }

    public static Music detectMusic(File file, Album album) {
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            AudioHeader audioHeader = audioFile.getAudioHeader();
            if (tag == null || audioHeader == null) {
                log.error("Cannot read audio file header: {}", file.getAbsolutePath());
                return null;
            }

            Music mmsg = new Music();
            mmsg.enableQuality(MusicQuality.NATIVE);
            mmsg.title = tag.getFirst(FieldKey.TITLE).trim();
            String artistName = tag.getFirst(FieldKey.ARTIST).trim();
            mmsg.artists.add(new Artist(HashUtil.stringToNegativeLong(artistName), artistName));
            String albumName = tag.getFirst(FieldKey.ALBUM).trim();
            mmsg.albumID = HashUtil.stringToNegativeLong(albumName);
            mmsg.duration = (int) (audioHeader.getPreciseTrackLength() * 1000);
            mmsg.audioPath.setNativePath(file.getAbsolutePath());

            if (mmsg.title == null || mmsg.title.isEmpty())
                mmsg.title = file.getName();
            mmsg.id = HashUtil.stringToNegativeLong(mmsg.title);

            album.setId(mmsg.albumID);
            album.setName(albumName);
            album.add(mmsg.id);

            Artwork artwork = tag.getFirstArtwork();
            if (artwork != null) {
                String mimeType = artwork.getMimeType();
                byte[] imageData = artwork.getBinaryData();
                log.debug("Detected MIME type: {}, size: {}", mimeType, imageData.length);

                File coverPath = new File(Global.COVER_CACHE_PATH);
                if (!coverPath.exists()) {
                    coverPath.mkdirs();
                }
                File coverFile = new File(coverPath.getAbsolutePath(), mmsg.albumID + ".png");
                if (!coverFile.exists()) {
                    coverFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(coverFile);
                    fos.write(imageData);
                    fos.close();
                }
                album.setPicUrlNative(coverFile.getAbsolutePath());
            }

            Audio amsg = new Audio();

            amsg.sampleRate = audioHeader.getSampleRateAsNumber();
            amsg.bitRate = (int) audioHeader.getBitRateAsNumber();
            amsg.bitDepth = audioHeader.getBitsPerSample();
            amsg.fileByteLength = file.length();

            mmsg.setaAudioHeader(amsg);
            log.debug("Detected: {}", mmsg);
            return mmsg;
        } catch (CannotReadException | TagException | InvalidAudioFrameException | ReadOnlyFileException | IOException e) {
            log.error("Error reading file: {}", file.getAbsolutePath(), e);
            return null;
        }
    }

    public static List<Music> loadMusics(File path, SQLPackage sqls) {
        if (!path.exists() || !path.isDirectory()) {
            log.warn("Path does not exist: {}", path.getAbsolutePath());
            return List.of();
        }

        File[] audios = path.listFiles(f -> {
            String name = f.getName();
            return f.isFile() && (
                    name.endsWith(".flac") ||
                    name.endsWith(".ogg") ||
                    name.endsWith(".mp3") ||
                    name.endsWith(".wav")
            );
        });

        if (audios == null) return List.of();

        List<Music> musics = new ArrayList<>();
        for (File audio : audios) {
            Album album = new Album();
            Music music = detectMusic(audio, album);
            if (music != null) {
                sqls.getMusicSQL().storage(music, sqls);
                musics.add(music);
                sqls.getAlbumSQL().storage(album);
            }
        }
        log.info("Loaded {} musics in folder {}", musics.size(), path.getAbsolutePath());
        return musics;
    }

    private static Audio.Encoding performHeaderCheck(byte[] header, int read) {
        // FLAC
        if (header[0] == 0x66 && header[1] == 0x4C && header[2] == 0x61 && header[3] == 0x43) return Audio.Encoding.FLAC;
        // OGG
        if (header[0] == 0x4F && header[1] == 0x67 && header[2] == 0x67 && header[3] == 0x53) return Audio.Encoding.OGG;
        // WAV/PCM
        if (header[0] == 0x52 && header[1] == 0x49 && header[2] == 0x46 && header[3] == 0x46) return Audio.Encoding.WAV;
        // M4A
        if (read >= 8 && header[4] == 0x66 && header[5] == 0x74 && header[6] == 0x79 && header[7] == 0x70) return Audio.Encoding.M4A;
        // MP3 (ID3 or Frame)
        if (header[0] == 0x49 && header[1] == 0x44 && header[2] == 0x33) return Audio.Encoding.MP3;
        if ((header[0] & 0xFF) == 0xFF && (header[1] & 0xE0) == 0xE0) return Audio.Encoding.MP3;

        return Audio.Encoding.UNKNOWN;
    }
}