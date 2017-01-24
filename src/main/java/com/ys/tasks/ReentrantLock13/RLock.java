package com.ys.tasks.ReentrantLock13;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by PC on 12/15/2016.
 */
public class RLock {
    public static float mainCounter;

    public static void main(String[] args) throws InterruptedException {
        Counter cc = new Counter(500);
        System.out.println(cc);

        Thread decreaser = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                cc.decrease(i);
                System.out.println("decrease by " + i + " " + cc);
                //System.out.println(cc);
            }
        });
        Thread increaser = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                cc.increase(i);
                System.out.println("increase by " + i + " " + cc);
                //System.out.println(cc);
            }
        });

        decreaser.start();
        increaser.start();

        decreaser.join();
        increaser.join();

        System.out.println(cc);

        mainCounter = cc.getCount();
        System.out.println(mainCounter);
    }
}

/////////////////////////////////////////////////

class Counter {

    public Counter(float count) {
        this.count = count;
    }

    public void decrease(float value) {
        try {
            lock.lock();
            this.count -= value;
        } finally {
            lock.unlock();
        }
    }

    public void increase(float value) {
        try {
            lock.lock();
            this.count += value;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "Counter: " + count;
    }

    private final Lock lock = new ReentrantLock();

    public float getCount() {
        return count;
    }

    private float count;
}

//////////////////////////////////////////////////////////////

class ReentrantLock implements Lock {

    public ReentrantLock() {
        sync = new Sync();
    }

    @Override
    public void lock() {
        sync.lock();
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

    private final Sync sync;

    static final class Sync extends AbstractQueuedSynchronizer {

        final void lock() {
            acquire(1);
        }

        @Override
        protected final boolean tryAcquire(int toAcquire) {
            final Thread curThread = Thread.currentThread();
            int state = getState();
            if (state == 0) {
                if (compareAndSetState(0, toAcquire)) {
                    setExclusiveOwnerThread(curThread);
                    return true;
                }
            } else if (curThread == getExclusiveOwnerThread()) {
                int nextState = state + toAcquire;
                if (nextState < 0) {
                    throw new Error("To many threads!");
                }
                setState(nextState);
                return true;
            }
            return false;
        }

        @Override
        protected final boolean tryRelease(int toRelease) {
            int state = getState() - toRelease;
            boolean free = false;
            if (state == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(state);
            return free;
        }

        final Condition newCondition() {
            return new AbstractQueuedSynchronizer.ConditionObject();
        }
    }
}
