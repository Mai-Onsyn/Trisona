package mai_onsyn.trisona.core.utils;

public class HistoryBuffer<T> {

    private final Object[] buffer;
    private final int capacity;

    private int size = 0;
    private int head = 0;   // 最旧元素位置

    private int cursor = -1; // 当前读取位置（-1表示还没开始读）

    public HistoryBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be > 0");
        }
        this.capacity = capacity;
        this.buffer = new Object[capacity];
    }

    public void record(T item) {
        int tail = (head + size) % capacity;
        buffer[tail] = item;

        if (size < capacity) {
            size++;
        } else {
            // 覆盖最旧
            head = (head + 1) % capacity;
        }

        // 写入新数据后，重置游标（常见行为：新操作后不能redo）
        cursor = -1;
    }

    @SuppressWarnings("unchecked")
    public T readPrevious() {
        if (size == 0) return null;

//        if (cursor == -1) {
//            // 第一次读，从最新开始
//            cursor = (head + size - 1) % capacity;
//            return (T) buffer[cursor];
//        }

        // 已经在最旧
        if (cursor == head) {
            return null;
        }

        cursor = (cursor - 1 + capacity) % capacity;
        return (T) buffer[cursor];
    }

    @SuppressWarnings("unchecked")
    public T readNext() {
        if (size == 0 || cursor == -1) return null;

        int newest = (head + size - 1) % capacity;

        // 已经在最新
        if (cursor == newest) {
            return null;
        }

        cursor = (cursor + 1) % capacity;
        return (T) buffer[cursor];
    }

    public void clear() {
        size = 0;
        head = 0;
        cursor = -1;
    }
}