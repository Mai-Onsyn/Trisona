package mai_onsyn.trisona.core.message;

import javax.sound.sampled.AudioFormat;

public class AudioMessage extends Message {
    public enum Encoding {
        MP3, WAV, FLAC, OGG, M4A, UNKNOWN
    }

    public long pcmByteLength = -1;
    public long fileByteLength = -1;
    public Encoding encoding = Encoding.UNKNOWN;
    public int channels = -1;
    public int sampleRate = -1;
    public int bitDepth = -1;
    public int bitRate = -1; // for MP3, OGG, FLAC, etc.
    public double gain = 0;
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

    public int getBPS() {
        return channels * sampleRate * bitDepth / 8;
    }

    public AudioMessage merge(AudioMessage other) {
        if (other == null) return this;
        AudioMessage merged = new AudioMessage();

        merged.pcmByteLength = this.pcmByteLength != -1 ? this.pcmByteLength : other.pcmByteLength;
        merged.fileByteLength = this.fileByteLength != -1 ? this.fileByteLength : other.fileByteLength;
        merged.channels = this.channels != -1 ? this.channels : other.channels;
        merged.sampleRate = this.sampleRate != -1 ? this.sampleRate : other.sampleRate;
        merged.bitDepth = this.bitDepth != -1 ? this.bitDepth : other.bitDepth;
        merged.bitRate = this.bitRate != -1 ? this.bitRate : other.bitRate;
        merged.encoding = this.encoding != Encoding.UNKNOWN ? this.encoding : other.encoding;
        merged.signed = this.signed != true ? this.signed : other.signed;

        merged.gain = this.gain != 0 ? this.gain : other.gain;

        return merged;
    }

    @Override
    public String toString() {
        return String.format(
                "AudioMessage{pcmByteLength=%d, fileByteLength=%d, channels=%d, sampleRate=%d, bitDepth=%d, bitRate=%d, encoding=%s, signed=%b, gain=%f}",
                pcmByteLength, fileByteLength, channels, sampleRate, bitDepth, bitRate, encoding, signed, gain
        );
    }
}
