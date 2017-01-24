package com.ys.tasks.DisruptorPattern15;

/**
 * Straightforward implementation
 * with all conditions satisfied
 */
public class RingBuffer <E>{
    private final int indexMask;
    private final Object[] entries;
    private final Monitor monitor;

    class Monitor {
        private volatile Integer producerCursor = new Integer(0);
        private volatile Integer consumerCursor = new Integer(0);

        /*
         * This method is used by consumers
         */
        public synchronized E pop() throws InterruptedException{
            while (producerCursor == consumerCursor) {
                wait();
            }
            E val = (E)entries[consumerCursor & indexMask];
            consumerCursor++;
            notifyAll();
            return val;
        }

        /*
         * This method is used by producers
         */
        public synchronized void put(E val) throws InterruptedException {
            while (producerCursor == (consumerCursor + entries.length)) {
                wait();
            }
            entries[producerCursor & indexMask] = val;
            producerCursor++;
            notifyAll();
        }
    }

    public RingBuffer(int bufferSize) {
        if (bufferSize < 1) {
            throw new IllegalArgumentException("bufferSize must not be less than 1");
        }
        if (Integer.bitCount(bufferSize) != 1) {
            throw new IllegalArgumentException("bufferSize must be a power of 2");
        }
        indexMask = bufferSize - 1; //cursor & indexMask == cursor mod bufferSize
        entries = new Object[bufferSize];
        monitor = new Monitor();
    }

    public void put(E val) throws InterruptedException {
        monitor.put(val);
    }

    public E pop() throws InterruptedException {
        return monitor.pop();
    }
}
