package com.ys.tasks.CyclicBarrier17;


class CyclicBarrier {
    private final int threadAmount;
    private Runnable barrierAction;
    private int arrived;

    CyclicBarrier(int threadAmount) {
        this.threadAmount = threadAmount;
        this.arrived = 0;
    }

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
                    for (int i = 1; i < 60; i++) {
                        count++;
                        System.out.println(Thread.currentThread().getName() + " calulated value: " + fib(count));
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

    public static void main(String[] args) {
        Runnable action = barrierAction();
        CyclicBarrier barrier = new CyclicBarrier(3, action);
        Runnable task1 = task(5, barrier);
        Runnable task2 = task(7, barrier);
        Runnable task3 = task(3, barrier);

        new Thread(task1, "FirstThread").start();
        new Thread(task2, "SecondThread").start();
        new Thread(task3, "ThirdThread3").start();
    }
}
