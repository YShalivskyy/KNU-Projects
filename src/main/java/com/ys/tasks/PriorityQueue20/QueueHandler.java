package com.ys.tasks.PriorityQueue20;

import javafx.util.Pair;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class QueueHandler {
    public static void main(String[] args) throws InterruptedException {
        final PriorityQueue q = new PriorityQueue(120000);
        final int n = 500;
        Runnable producer1 = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < n; i++) {
                    Pair<Integer, Integer> p = new Pair<>(i, n - i);
                    System.out.println("Producer " + ThreadId.get() + " puts " + p);
                    q.put(p);
                }
            }
        };
        Runnable producer2 = new Runnable() {
            @Override
            public void run() {
                for (int i = n; i < 2*n; i++) {
                    Pair<Integer, Integer> p = new Pair<>(i, n - i);
                    System.out.println("Producer " + ThreadId.get() + " puts " + p);
                    q.put(p);
                }
            }
        };

        Runnable consumer = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < n; i++) {
                    System.out.println("Consumer " + ThreadId.get() + " pops " + q.pop());
                    Thread.yield();
                }
            }
        };
        Thread prod1 = new Thread(producer1);
        Thread prod2 = new Thread(producer2);
        Thread prod3 = new Thread(producer1);
        Thread cons1 = new Thread(consumer);
        Thread cons2 = new Thread(consumer);
        prod1.start();
        prod2.start();
        prod1.join();
        prod3.start();
        cons1.start();
        prod2.join();
        cons2.start();
        cons1.join();
        cons2.join();
        prod3.join();
        System.out.println(q);
    }
}
