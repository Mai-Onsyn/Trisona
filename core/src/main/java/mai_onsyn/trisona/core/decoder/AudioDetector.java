package mai_onsyn.trisona.core.decoder;

import mai_onsyn.trisona.Global;
import mai_onsyn.trisona.core.message.Artist;
import mai_onsyn.trisona.core.message.AudioMessage;
import mai_onsyn.trisona.core.message.MusicMessage;
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
import java.util.logging.Level;

public class AudioDetector {
    static {
        java.util.logging.Logger.getLogger("org.jaudiotagger").setLevel(Level.SEVERE);
    }
    private static final Logger log = LoggerFactory.getLogger(AudioDetector.class);

    public record DetectionResult(AudioMessage.Encoding encoding, InputStream rewindableStream) {}

    public static DetectionResult detectFormatInStream(InputStream originalIn) throws IOException {
        InputStream stream = originalIn.markSupported() ?
                originalIn : new BufferedInputStream(originalIn);

        stream.mark(128);

        byte[] header = new byte[12];
        int read = stream.read(header);

        AudioMessage.Encoding type = AudioMessage.Encoding.UNKNOWN;
        if (read >= 4) {
            type = performHeaderCheck(header, read);
        }

        stream.reset();

        return new DetectionResult(type, stream);
    }

    public static MusicMessage detectMusic(File file) {
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            AudioHeader audioHeader = audioFile.getAudioHeader();

            MusicMessage mmsg = new MusicMessage();
            mmsg.title = tag.getFirst(FieldKey.TITLE);
            mmsg.artists.add(new Artist(tag.getFirst(FieldKey.ARTIST)));
            mmsg.album = tag.getFirst(FieldKey.ALBUM);
            mmsg.duration = audioHeader.getTrackLength();
            mmsg.audioPath.setNativePath(file.getAbsolutePath());

            Artwork artwork = tag.getFirstArtwork();
            if (artwork != null) {
                String mimeType = artwork.getMimeType();
                byte[] imageData = artwork.getBinaryData();
                log.debug("Detected MIME type: {}, size: {}", mimeType, imageData.length);

                File coverPath = new File(Global.COVER_CACHE_PATH);
                if (!coverPath.exists()) {
                    coverPath.mkdirs();
                }
                File coverFile = new File(coverPath.getAbsolutePath(), file.getName() + ".png");
                if (!coverFile.exists()) {
                    coverFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(coverFile);
                    fos.write(imageData);
                    fos.close();
                }
                mmsg.coverPath.setNativePath(coverFile.getAbsolutePath());
            }

            AudioMessage amsg = new AudioMessage();

            amsg.sampleRate = audioHeader.getSampleRateAsNumber();
            amsg.bitRate = (int) audioHeader.getBitRateAsNumber();
            amsg.bitDepth = audioHeader.getBitsPerSample();

            mmsg.audioMessage = amsg;
            log.debug("Detected: {}", mmsg);
            return mmsg;
        } catch (CannotReadException | TagException | InvalidAudioFrameException | ReadOnlyFileException | IOException e) {
            log.error("Error reading file: {}", file.getAbsolutePath(), e);
            return null;
        }
    }

    private static AudioMessage.Encoding performHeaderCheck(byte[] header, int read) {
        // FLAC
        if (header[0] == 0x66 && header[1] == 0x4C && header[2] == 0x61 && header[3] == 0x43) return AudioMessage.Encoding.FLAC;
        // OGG
        if (header[0] == 0x4F && header[1] == 0x67 && header[2] == 0x67 && header[3] == 0x53) return AudioMessage.Encoding.OGG;
        // WAV/PCM
        if (header[0] == 0x52 && header[1] == 0x49 && header[2] == 0x46 && header[3] == 0x46) return AudioMessage.Encoding.WAV;
        // M4A
        if (read >= 8 && header[4] == 0x66 && header[5] == 0x74 && header[6] == 0x79 && header[7] == 0x70) return AudioMessage.Encoding.M4A;
        // MP3 (ID3 or Frame)
        if (header[0] == 0x49 && header[1] == 0x44 && header[2] == 0x33) return AudioMessage.Encoding.MP3;
        if ((header[0] & 0xFF) == 0xFF && (header[1] & 0xE0) == 0xE0) return AudioMessage.Encoding.MP3;

        return AudioMessage.Encoding.UNKNOWN;
    }
}