package mai_onsyn.trisona.core.decoder;

import mai_onsyn.trisona.Global;
import mai_onsyn.trisona.core.message.Audio;

import java.io.DataInputStream;
import java.io.IOException;

public class WavDecoder extends AudioDecoder {

    private static final String NOT_A_WAV_FILE = "Not a WAV file";
    private static final String WAV_WAS_DESTROYED = "This WAV was destroyed";
    private final byte[] byte4Buffer = new byte[4];
    private final byte[] byte2Buffer = new byte[2];

    @Override
    Audio readyAudioStream(DataInputStream is, long streamSize, StreamReadyRef readyRef) throws IOException {
        if (!(is.read(byte4Buffer) == 4 &&
                new String(byte4Buffer).equals("RIFF"))) {
            throw new AudioDecodeException(NOT_A_WAV_FILE);
        }

        if (is.skip(4) != 4) throw new AudioDecodeException(NOT_A_WAV_FILE);

        if (!(is.read(byte4Buffer) == 4 &&
                new String(byte4Buffer).equals("WAVE"))) {
            throw new AudioDecodeException(NOT_A_WAV_FILE);
        }

        Audio info = new Audio();
        while (is.available() > 0) {
            if (is.read(byte4Buffer) != 4) break;

            String chunkID = new String(byte4Buffer);
            int chunkSize = readIntLE(is);

            if (chunkID.equals("fmt ")) {
                readShortLE(is);
                info.channels = readShortLE(is);
                info.sampleRate = readIntLE(is);
                readIntLE(is); // byte rate
                readShortLE(is); // block align
                info.bitDepth = readShortLE(is);

                if (chunkSize > 16)
                    if (is.skip(chunkSize - 16) != chunkSize - 16)
                        throw new AudioDecodeException(WAV_WAS_DESTROYED);

                info.encoding = Audio.Encoding.WAV;
            } else if (chunkID.equals("data")) {
                info.pcmByteLength = chunkSize / info.channels / (info.bitDepth / 8) * 4L;
                return info;
            } else
            if (is.skip(chunkSize) != chunkSize)
                throw new AudioDecodeException(WAV_WAS_DESTROYED);
        }
        return null;
    }

    @Override
    DataInputStream decode(DataInputStream is, Audio sourceInfo) throws IOException {
        return DecodeUtil.wavRedecode(is, sourceInfo, Global.SYSTEM_AUDIO_FORMAT);
    }

    private int readIntLE(DataInputStream is) throws IOException, AudioDecodeException {
        if (is.read(byte4Buffer) != 4) throw new AudioDecodeException(WAV_WAS_DESTROYED + ": Can't read Int LE");
        return (
                (byte4Buffer[0] & 0xFF) |
                ((byte4Buffer[1] & 0xFF) << 8) |
                ((byte4Buffer[2] & 0xFF) << 16) |
                ((byte4Buffer[3] & 0xFF) << 24)
        );
    }

    private short readShortLE(DataInputStream is) throws IOException {
        if (is.read(byte2Buffer) != 2) throw new AudioDecodeException(WAV_WAS_DESTROYED + ": Can't read Short LE");
        return (short) ((byte2Buffer[0] & 0xFF) | ((byte2Buffer[1] & 0xFF) << 8));
    }
}
