package mai_onsyn.trisona.core.decoder;

import mai_onsyn.trisona.core.message.AudioMessage;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

import static mai_onsyn.trisona.Global.SYSTEM_AUDIO_MESSAGE;

public abstract class AudioDecoder {

    enum DecodeRefState {
        SUCCESS, FAILED, OVER_STREAM
    }

    interface StreamReadyRef {
        void invoke(DataInputStream is);
    }

    abstract AudioMessage readyAudioStream(DataInputStream is, long streamSize, StreamReadyRef readyRef) throws IOException; //读掉音频信息剩下音频流
    abstract DataInputStream decode(DataInputStream is, AudioMessage sourceInfo) throws IOException; //解码音频流 返回解码后的PCM音频流

    DecodeRefState seek(DataInputStream is, int millisAmount) { //在解码后的PCM上进行进度跳转，跳过millAmount毫秒的音字节频流
        long bytePosition = (long) millisAmount * SYSTEM_AUDIO_MESSAGE.getBPS() / 1000;
        try {
            is.skipNBytes(bytePosition);
        } catch (EOFException e) {
            return DecodeRefState.OVER_STREAM;
        } catch (IOException e) {
            return DecodeRefState.FAILED;
        }
        return DecodeRefState.SUCCESS;
    }

//    int b = 0;
    DecodeRefState readStream(DataInputStream is, byte[] target) throws IOException { //已解码的PCM音频流读到target里
        try {
            int size = is.read(target);
            if (size == -1) return DecodeRefState.OVER_STREAM;

            if (size < target.length) {
                Arrays.fill(target, size, target.length, (byte) 0);
            }
//            for (byte b1 : target) {
//                b += b1;
//            }
//            System.out.println(b);
            return DecodeRefState.SUCCESS;
        } catch (Exception e) {
            return DecodeRefState.FAILED;
        }
    }
}
