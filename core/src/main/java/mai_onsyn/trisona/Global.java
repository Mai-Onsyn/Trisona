package mai_onsyn.trisona;

import mai_onsyn.trisona.core.TrisonaKotlinInterface;
import mai_onsyn.trisona.core.message.AudioMessage;

import javax.sound.sampled.AudioFormat;
import java.util.Random;

public class Global {

    public static volatile boolean APPLICATION_EXITED = false;

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36 Edg/144.0.0.0";

    public static final AudioFormat SYSTEM_AUDIO_FORMAT = new AudioFormat(48000, 16, 2, true, false);
    public static final AudioMessage SYSTEM_AUDIO_MESSAGE = new AudioMessage(SYSTEM_AUDIO_FORMAT);

    public static final String COVER_CACHE_PATH = System.getProperty("user.dir") + "/cache/covers";

    public static final Random RANDOM = new Random();
}
