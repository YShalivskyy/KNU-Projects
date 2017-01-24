package com.ys.tasks.ReadWriteLock10;


import java.util.ArrayList;

class ReadersWriterLock {
    private final int maxReadersCount;
    private int readersCount = 0;

    private boolean isBlocked = false;

    ReadersWriterLock(int maxReadersCount){
        this.maxReadersCount = maxReadersCount;
    }

    ReadersWriterLock(){
        this(10);
    }

    public synchronized void readLock() throws InterruptedException{
        while(isBlocked && maxReadersCount <= readersCount){
            wait();
        }
        ++readersCount;
    }

    public synchronized void readUnlock(){
//        assert(readersCount > 0);
        --readersCount;
        notifyAll();
    }

    public synchronized  void writeLock() throws InterruptedException{
        isBlocked = true;

        while(readersCount > 0) {
            wait();
        }
    }

    public synchronized void writeUnlock(){
        isBlocked = false;
        notifyAll();
    }

}

class ThreadSafeArrayList<E>  {
    private final ReadersWriterLock rwl = new ReadersWriterLock();
    private ArrayList<E> list = new ArrayList<>();

    public int set(E el) throws InterruptedException {
        rwl.writeLock();
        try {
            list.add(el);
            System.out.println("Adding element by thread" + Thread.currentThread().getName());
        } finally {
            rwl.writeUnlock();
            return 1;
        }
    }

    public E get(int index, int counter) throws InterruptedException {
        rwl.readLock();
        try {
            System.out.println("Printing elements by thread"+Thread.currentThread().getName());
            return list.get(index);
        } finally {
            rwl.readUnlock();
        }
    }
}

public class ReadWriteLockHandler {
    public static int readCounter;
    public static int writeCounter;
    public static void main(String[] args) throws InterruptedException {
        //System.out.println(writeCounter);
        final ThreadSafeArrayList<Integer> array = new ThreadSafeArrayList<>();
        writeCounter += array.set(0);
        //System.out.println(writeCounter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    array.get(0, readCounter);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    writeCounter += array.set(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t1.join();
        //System.out.println(writeCounter);
        writeCounter += array.set(3);
        array.get(0, readCounter);
        //System.out.println(writeCounter);
    }
}
