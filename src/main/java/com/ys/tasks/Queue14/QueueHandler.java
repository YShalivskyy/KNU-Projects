package com.ys.tasks.Queue14;

/**
 * Created by PC on 11/24/2016.
 */
public class QueueHandler {

    public static void main(String[] args) throws InterruptedException{
        final Queue queue = new Queue();
        Thread en1Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    queue.enqueue(1);
                    System.out.println("push 1");
                }
            }
        });
        Thread en2Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    queue.enqueue(2);
                    System.out.println("push 2");
                }
            }
        });
        Thread de1Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println("pop" + queue.dequeue());
                }
            }
        });
        Thread de2Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println("pop" + queue.dequeue());
                }
            }
        });
        en1Thread.start();
        en2Thread.start();
        Thread.sleep(3000);
        de1Thread.start();
        de2Thread.start();
        Thread.sleep(1000);
        en1Thread.join();
        en2Thread.join();
        de1Thread.join();
        de2Thread.join();
        int i = 0;
    }
}
