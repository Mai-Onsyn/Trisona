package mai_onsyn.trisona.core.message;

import javax.sound.sampled.AudioFormat;

public class AudioMessage extends Message {
    public enum Encoding {
        MP3, WAV, FLAC, OGG, M4A, UNKNOWN
    }

    public long pcmByteLength;
    public long fileByteLength;
    public Encoding encoding;
    public int channels;
    public int sampleRate;
    public int bitDepth;
    public int bitRate = -1; // for MP3, OGG, FLAC, etc.
    public boolean signed = true;

    public AudioMessage() {}

    public AudioMessage(AudioFormat format) {
        AudioFormat.Encoding formatEncoding = format.getEncoding();
        if (formatEncoding == javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED) {
            encoding = Encoding.WAV;
            signed = true;
        } else if (formatEncoding == javax.sound.sampled.AudioFormat.Encoding.PCM_UNSIGNED) {
            encoding = Encoding.WAV;
            signed = false;
        } else encoding = Encoding.UNKNOWN;

        channels = format.getChannels();
        sampleRate = (int) format.getSampleRate();
        bitDepth = format.getSampleSizeInBits();
    }

    @Override
    public String toString() {
        return String.format("AudioMessage{byteLength=%d, encoding=%s, channels=%d, sampleRate=%d, bitDepth=%d, signed=%b}", pcmByteLength, encoding, channels, sampleRate, bitDepth, signed);
    }

    public int getBPS() {
        return channels * sampleRate * bitDepth / 8;
    }
}
