package mai_onsyn.trisona.core.decoder;

import mai_onsyn.trisona.core.message.AudioMessage;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.DataInputStream;

public class DecodeUtil {
    public static DataInputStream wavRedecode(
            DataInputStream sourcePCMStream,
            AudioMessage sourceMsg,
            AudioFormat targetFormat
    ) {

        AudioFormat originalFormat = new AudioFormat(
                sourceMsg.sampleRate,
                sourceMsg.bitDepth,
                sourceMsg.channels,
                sourceMsg.signed,
                false
        );

        AudioInputStream originalStream = new AudioInputStream(
                sourcePCMStream,
                originalFormat,
                sourceMsg.pcmByteLength / originalFormat.getFrameSize()
        );
        AudioInputStream convertedStream = AudioSystem.getAudioInputStream(
                targetFormat,
                originalStream
        );

        return new DataInputStream(convertedStream);
    }
}
