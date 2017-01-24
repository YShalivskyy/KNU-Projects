package com.ys.tasks.DisruptorPattern15;

/**
 * This is more "essential" implementation
 * but only 1 producers allowed and no
 * wait() and notify()
 */
public class RingBufferBusy <E>{
    private final int indexMask;
    private final Object[] entries;
    private final Monitor monitor;

    class Monitor {
        private volatile Integer producerCursor = new Integer(0);
        private volatile Integer consumerCursor = new Integer(0);
        private volatile Integer consumerCurCursor = new Integer(0);

        /*
         * This method is used by consumers
         */
        public  synchronized E pop() throws InterruptedException{
            while (consumerCurCursor == consumerCursor) {
                //busy waiting
            }
            E val = (E)entries[consumerCurCursor & indexMask];
            consumerCurCursor++;
            return val;
        }

        /*
         * This method is used by producers
         */
        public void put(E val) throws InterruptedException {

            while (producerCursor == (consumerCurCursor + entries.length)) {
                //busy waiting
            }
            producerCursor++;
            entries[producerCursor & indexMask] = val;
            consumerCursor++;
        }
    }

    public RingBufferBusy(int bufferSize) {
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
