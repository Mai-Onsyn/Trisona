package mai_onsyn.trisona.core.decoder;

import mai_onsyn.trisona.core.message.Audio;

import java.io.DataInputStream;
import java.io.IOException;

public class OggDecoder extends AudioDecoder {
    @Override
    Audio readyAudioStream(DataInputStream is, long streamSize, StreamReadyRef readyRef) throws IOException {
        return null;
    }

    @Override
    DecodeRefState readStream(DataInputStream is, byte[] target) throws IOException {
        return null;
    }

    @Override
    DataInputStream decode(DataInputStream is, Audio sourceInfo) throws IOException {
        return null;
    }

    @Override
    DecodeRefState seek(DataInputStream is, int millisAmount) {
        return null;
    }
}
