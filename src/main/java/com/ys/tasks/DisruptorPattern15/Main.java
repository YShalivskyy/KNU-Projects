package com.ys.tasks.DisruptorPattern15;

/**
 * Created by Ігор on 06.11.16.
 */

public class Main {

    public static void main(String[] args) throws InterruptedException{
        final int n = 10;
        final RingBuffer<Integer> queue = new RingBuffer<>(1);
        Runnable putRunnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < n; i++) {
                    try {
                        queue.put(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("push " + i);
                }
            }
        };
        Runnable popRunnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < n; i++) {
                    try {
                        System.out.println("pop" + queue.pop());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread prod1Thread = new Thread(putRunnable);
        Thread prod2Thread = new Thread(putRunnable);
        Thread con1Thread = new Thread(popRunnable);
        Thread con2Thread = new Thread(popRunnable);
        prod1Thread.start();
        prod2Thread.start();
        con1Thread.start();
        con2Thread.start();
        prod1Thread.join();
        prod2Thread.join();
        con1Thread.join();
        con2Thread.join();
    }
}