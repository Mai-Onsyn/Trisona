package mai_onsyn.trisona.core.decoder;

public class RingBuffer {

    private final byte[] buffer;
    private final int chunkSize;
    private int writePos;
    private int readPos;
    private int availableDataSize;

    private boolean isFlushing = false;

    public enum RingBufferRefState {
        FAILED, SUCCESS, FLUSH_REQUIRED, WAITING_INTERRUPTED
    }

    public RingBuffer(int chunkCapacity, int chunkSize) {
        this.buffer = new byte[chunkCapacity * chunkSize];
        this.chunkSize = chunkSize;
    }

    public synchronized RingBufferRefState write(byte[] data) {
        if (data.length != chunkSize) throw new IllegalArgumentException("Data length must be equal to chunk size: " + chunkSize);

        while (buffer.length - availableDataSize < chunkSize) {
            if (isFlushing) return RingBufferRefState.FLUSH_REQUIRED;

            try {
                this.wait();
                if (isFlushing) return RingBufferRefState.FLUSH_REQUIRED;
            } catch (InterruptedException e) {
                return RingBufferRefState.WAITING_INTERRUPTED;
            }
        }

        if (isFlushing) return RingBufferRefState.FLUSH_REQUIRED;

        System.arraycopy(data, 0, buffer, writePos, chunkSize);
        writePos = (writePos + chunkSize) % buffer.length;
        availableDataSize += chunkSize;

        this.notifyAll();
        return RingBufferRefState.SUCCESS;
    }

    public synchronized RingBufferRefState read(byte[] target) {
        if (target.length != chunkSize) throw new IllegalArgumentException("Target length must be equal to chunk size: " + chunkSize);

        while (availableDataSize < chunkSize) {
            if (isFlushing) return RingBufferRefState.FLUSH_REQUIRED;

            try {
                this.wait();
                if (isFlushing) return RingBufferRefState.FLUSH_REQUIRED;
            } catch (InterruptedException e) {
                return RingBufferRefState.WAITING_INTERRUPTED;
            }
        }

        if (isFlushing) return RingBufferRefState.FLUSH_REQUIRED;

        System.arraycopy(buffer, readPos, target, 0, chunkSize);
        readPos = (readPos + chunkSize) % buffer.length;
        availableDataSize -= chunkSize;

        this.notifyAll();
        return RingBufferRefState.SUCCESS;
    }

    public synchronized void flushStart() {
        writePos = 0;
        readPos = 0;
        availableDataSize = 0;
        isFlushing = true;
        this.notifyAll();
    }

    public synchronized void flushComplete() {
        isFlushing = false;
        this.notifyAll();
    }

    public int getEmptySpace() {
        return chunkSize - availableDataSize;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public int getAvailableDataSize() {
        return availableDataSize;
    }
}
