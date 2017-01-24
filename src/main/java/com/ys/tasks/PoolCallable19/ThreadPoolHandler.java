package com.ys.tasks.PoolCallable19;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.*;



class Task implements Callable<Integer>{
    private int sleepTime;
    public Task(int task){
        sleepTime = task;
    }
    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " sleeping: " + (sleepTime +1) + " seconds");
        Thread.sleep(1000 * (sleepTime + 1));
        return sleepTime;
    }
}

class ThreadPool<T> {
    private ArrayBlockingQueue<Pair<Callable<T>, Future<T>>> queue = new ArrayBlockingQueue<>(100);
    private ArrayList<Executor> executor = new ArrayList<>();

    public ThreadPool(int size) {
        for (int i = 0; i < size; i++) {
            executor.add(new Executor(queue));
            executor.get(i).start();
        }
    }

    public Future<T> submit(Callable<T> task) {
        Future<T> future = new Result();
        synchronized (queue) {
            queue.add(new Pair(task, future));
            queue.notify();
        }
        return future;
    }

    public void shutdown() {
        for (Executor e : executor)
            e.shutdown();
        for (Executor e : executor)
            try {
                e.join();
            } catch (Exception ex) {}
    }

    class Result implements Future {
        T result = null;
        boolean done = false;
        Object flag = new Object();

        void setResult(T r) {
            result = r;
            synchronized (flag) {
                done = true;
                flag.notify();
            }
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return done;
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            synchronized (flag) {
                while (!isDone()) {
                    flag.wait();
                }
            }
            return result;
        }

        @Override
        public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }
    }

    class Executor extends Thread {
        private ArrayBlockingQueue<Pair<Callable<T>, Future<T>>> queue;
        private boolean finish = false;
        Future<T> future;

        public Executor(ArrayBlockingQueue<Pair<Callable<T>, Future<T>>> q) {
            queue = q;
        }

        @Override
        public void run() {
            while (!finish || queue.size() > 0) {
                Callable<T> task;
                Pair<Callable<T>, Future<T>> pair = queue.poll();
                if(pair != null) {
                    task = pair.getKey();
                    future = pair.getValue();
                    try {
                        T result = task.call();
                        ((Result) future).setResult(result);
                        System.out.println(Thread.currentThread().getName() + " thread finished" + " Value: " + result);
                    } catch (Exception e) {}
                } else {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {}
                }
            }
        }

        public void shutdown() {
            finish = true;
        }
    }
}

public class ThreadPoolHandler {
    public static ArrayList<Future<Integer>> result=new ArrayList<>();
    public static int iterationsCounter;
    public static void main(String[] args) throws ExecutionException, InterruptedException{
        ThreadPool<Integer> threadPool=new ThreadPool<>(4);

        for(int t=0; t<10; t++) {
            result.add(threadPool.submit(new Task(t)));
        }

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                for (int t = 0; t < result.size(); t++) {
                    try {
                        System.out.println("Calculated value: " + result.get(t).get() + '\n');
                        iterationsCounter++;
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        threadPool.shutdown();
    }
}