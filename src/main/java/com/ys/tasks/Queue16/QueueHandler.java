package com.ys.tasks.Queue16;

/**
 * Created by PC on 11/24/2016.
 */
public class QueueHandler {
    public static Queue queue;
    QueueHandler(){
        queue = new Queue();
    }
    public static void main(String[] args) throws InterruptedException{
        QueueHandler handler = new QueueHandler();
        Thread pushThread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    queue.enqueue(1);
                    System.out.println("pushThread1");
                }
            }
        });
        Thread pushThread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    queue.enqueue(2);
                    System.out.println("pushThread2");
                }
            }
        });
        Thread popThread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    System.out.println("pop " + queue.dequeue());
                }
            }
        });
        Thread popThread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    System.out.println("pop " + queue.dequeue());
                }
            }
        });
        pushThread1.start();
        pushThread2.start();
        Thread.sleep(3000);



        popThread1.start();
        popThread2.start();
        Thread.sleep(2000);

        pushThread1.join();
        pushThread2.join();
        popThread1.join();
        popThread2.join();
    }
}
