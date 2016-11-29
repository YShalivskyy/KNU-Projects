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

    public void set(E el) throws InterruptedException {
        rwl.writeLock();
        try {
            list.add(el);
            System.out.println("Adding element by thread" + Thread.currentThread().getName());
        } finally {
            rwl.writeUnlock();
        }
    }

    public E get(int index) throws InterruptedException {
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

    public static void main(String[] args) throws InterruptedException {
        final ThreadSafeArrayList<Integer> arr = new ThreadSafeArrayList<>();
        arr.set(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    arr.get(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    arr.set(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        arr.set(3);
        arr.get(0);

    }
}
