package com.ys.tasks.Phaser2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class Phaser {
    private int phase;
    private int parties;
    private int arrived;
    private int unarrived;
    public boolean terminated;
    private static Map<Integer,Integer> map;

    public Phaser(int parties) {
        this.parties = parties;
        this.arrived = 0;
        this.unarrived = parties;
        this.phase = 0;
        terminated = false;
        map = Collections.synchronizedMap(new HashMap<Integer,Integer>());
    }

    public static Map<Integer, Integer> getMap() {
        return map;
    }

    public synchronized void register() throws InterruptedException {
        if(terminated)
            return;
        parties++;
        unarrived--;
        putDown();
        if(parties == 0) {
            terminated = true;
        }
    }

    private synchronized boolean putDown() throws InterruptedException {
        if(unarrived == 0) {
            arrived = 0;
            unarrived = parties;
            phase++;
            wait(500);
            notifyAll();
            System.out.println("Barrier triggered. Phase: " + phase + "\n");
            return true;
        }
        return false;
    }

    public synchronized void arriveAndAwaitAdvance() throws InterruptedException {
        if(terminated)
            return;
        arrived++;
        unarrived--;
        if (map.containsKey(phase)) {
            map.put(phase,map.get(phase)+1);
        } else {
            map.put(phase,1);
        }
        if(!putDown()) {
            this.wait();
        }
    }

    public synchronized void arriveAndDeregister() throws InterruptedException {
        if(terminated) return;
        parties--;
        unarrived--;
        putDown();
        if(parties == 0) {
            terminated = true;
        }
    }

    public boolean isTerminated() {
        return terminated;
    }

    public int getParties() {
        return parties;
    }

    public int getArrived() {
        return arrived;
    }

    public int getUnarrived() {
        return unarrived;
    }

    public int getPhase() {
        return phase;
    }
}

public class PhaserHandler {
    public static Phaser phaser;

    public PhaserHandler(Phaser _phaser){
        phaser = _phaser;
    }

    public static Runnable taskDereg(final int value){
        return new Runnable() {
            @Override
            public void run() {
                try {
                    int count = value;
                    for (int i = 0; i < 40; i++) {
                        count++;
                        if(i == 30) {
                            phaser.arriveAndDeregister();
                        }

                        System.out.println(Thread.currentThread().getName() + " calculated value: " + count);
                        phaser.arriveAndAwaitAdvance();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

    }
    public static Runnable taskReg(final int value) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    int count = value;
                    for (int i = 0; i < 40; i++) {
                        count++;
                        if (i == 15) {
                            phaser.register();
                        }

                        System.out.println(Thread.currentThread().getName() + " calculated value: " + count);
                        phaser.arriveAndAwaitAdvance();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static void main(String[] args) throws InterruptedException {

        Phaser phaser = new Phaser(3);
        PhaserHandler handler = new PhaserHandler(phaser);
        Thread thread1 = new Thread(taskDereg(1), "Thread 1: ");
        Thread thread2 = new Thread(taskDereg(1), "Thread 2: ");
        Thread thread3 = new Thread(taskDereg(1), "Thread 3: ");

        Thread thread5 = new Thread(taskReg(1), "Thread 5: ");

        thread1.start();
        thread2.start();
        thread3.start();

        thread5.start();

        thread1.join();
        thread2.join();
        thread3.join();

        thread5.join();

    }
}
