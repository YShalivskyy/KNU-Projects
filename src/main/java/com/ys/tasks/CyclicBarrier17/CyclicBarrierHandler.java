package com.ys.tasks.CyclicBarrier17;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class CyclicBarrier {
    private final int threadAmount;
    private Runnable barrierAction;
    private int arrived;

    CyclicBarrier(int threadAmount, Runnable barrierAction) {
        this.threadAmount = threadAmount;
        this.barrierAction = barrierAction;
        this.arrived = 0;
    }

    public synchronized void waitAllThreads() throws InterruptedException {
        arrived++;
        if(arrived >= this.threadAmount) {
            arrived = 0;
            barrierAction.run();
            notifyAll();
        } else {
            this.wait();
        }
    }

}

public class CyclicBarrierHandler {
    public static List<Integer> list;
    private static int iterations;

    CyclicBarrierHandler(int iterations){
        list = Collections.synchronizedList(new ArrayList<Integer>());
        this.iterations = iterations;
    }

    public static Runnable task(final int value, final CyclicBarrier barrier){
        return new Runnable() {

            synchronized private int fib(int x) {
                if (x == 0) return 0;
                else if (x == 1) return 1;
                else {
                    return fib(x-1) + fib(x-2);
                }
            }

            @Override
            public void run() {
                try {
                    int count = value;
                    for (int i = 0; i < iterations; i++) {
                        count++;
                        int f = fib(count);
                        System.out.println(Thread.currentThread().getName() + " calulated value: " + f);
                        list.add(f);
                        //System.out.println("Que: " + queue.take());
                        barrier.waitAllThreads();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static Runnable barrierAction() {
        return new Runnable() {
            @Override
            public void run() {
                System.out.println("Barrier triggered");
            }
        };
    }

    public static void main(String[] args) throws InterruptedException {
        CyclicBarrierHandler handler = new CyclicBarrierHandler(25);
        Runnable action = barrierAction();
        CyclicBarrier barrier = new CyclicBarrier(3, action);
        Runnable task1 = task(10, barrier);
        Runnable task2 = task(14, barrier);
        Runnable task3 = task(12, barrier);

        Thread thread1 = new Thread(task1, "FirstThread");
        Thread thread2 = new Thread(task2, "SecondThread");
        Thread thread3 = new Thread(task3, "ThirdThread3");

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();
    }
}
