package com.ys.tasks.PoolCallable19;

import java.util.ArrayList;
import java.util.concurrent.*;



class Task implements Callable<Integer>{
    int i;
    public Task(int t){
        i=t;
    }
    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " is calculating: " + (i+1) + " seconds");
        Thread.sleep(1000 * (i + 1));
        return i;
    }
}

class ThreadPool<T> {
    private ArrayBlockingQueue<Pair<Callable<T>, Future<T>>> queue = new ArrayBlockingQueue<>(100);
    private ArrayList<Executor> executor = new ArrayList<>();

    public ThreadPool(int s) {
        for (int i = 0; i < s; i++) {
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
                    task = pair.getLeft();
                    future = pair.getRight();
                    try {
                        T result = task.call();
                        ((Result) future).setResult(result);
                        System.out.println(Thread.currentThread().getName() + " finished");
                    } catch (Exception e) {}
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {}
                }
            }
        }

        public void shutdown() {
            finish = true;
        }
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

    class Pair<L, R> {

        private final L left;
        private final R right;

        public Pair(L left, R right) {
            this.left = left;
            this.right = right;
        }

        public L getLeft() {
            return left;
        }

        public R getRight() {
            return right;
        }

        @Override
        public int hashCode() {
            return left.hashCode() ^ right.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Pair)) return false;
            Pair pairo = (Pair) o;
            return this.left.equals(pairo.getLeft()) &&
                    this.right.equals(pairo.getRight());
        }

    }
}

public class ThreadPoolHandler {
    static ArrayList<Future<Integer>> result=new ArrayList<>();
    public static void main(String[] args) throws ExecutionException, InterruptedException{
        ThreadPool<Integer> threadPool=new ThreadPool<>(4);

        for(int t=0; t<8; t++) {
            result.add(threadPool.submit(new Task(t)));
        }

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                for (int t = 0; t < result.size(); t++) {
                    try {
                        System.out.println("Returned value: " + result.get(t).get() + '\n');
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