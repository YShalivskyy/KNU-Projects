package com.ys.tasks.PriorityQueue20;
import javafx.util.Pair;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityQueue {

    static final long EMPTY = -1;
    static final long AVAILABLE = -2;
    class Item {
        volatile ReentrantLock lock;
        volatile Long tag;
        volatile Pair<Integer, Integer> priority;
        Item() {
            lock = new ReentrantLock();
            tag = EMPTY;
        }
    }

    Lock sizeLock;
    int size;
    volatile Item[] items;
    int MAX_SIZE;

    public PriorityQueue(int maxSize) {
        MAX_SIZE = maxSize;
        items = new Item[maxSize];
        for (int i = 0; i < maxSize; i++) {
            items[i] = new Item();
        }
        sizeLock = new ReentrantLock();
    }

    private final void LOCK(int i) { items[i].lock.lock(); }
    private final void UNLOCK(int i) { items[i].lock.unlock(); }
    private final Integer getPriority(int i) { return items[i].priority.getKey(); }
    private void swap_items(int i, int j) {
        Item temp = items[i];
        items[i] = items[j];
        items[j] = temp;
    }

    void put(Pair<Integer, Integer> record) {
        //Insert new item at bottom of the heap
        sizeLock.lock();
        int i = size;
        size++;
        LOCK(i);
        sizeLock.unlock();
        items[i].priority = record;
        items[i].tag = Thread.currentThread().getId();
        UNLOCK(i);

        //Move item towards of heap while it has higher
        //priority than parent
        while (i > 0) {
            int parent = (i - 1) / 2;

            Lock parentLock = items[parent].lock;
            Lock iLock = items[parent].lock;
            parentLock.lock();
            iLock.lock();
            if ((items[parent].tag == AVAILABLE) &&
                    (items[i].tag == Thread.currentThread().getId())) {
                if (getPriority(i).compareTo(getPriority(parent)) > 0) {
                    swap_items(i, parent);
                    i = parent;
                } else {
                    items[i].tag = AVAILABLE;
                    i = -1;
                }
            } else if (items[parent].tag == EMPTY) {
                i = -1;
            } else if (items[i].tag != Thread.currentThread().getId()) {
                i = parent;
            }
            iLock.unlock();
            parentLock.unlock();
        }
        if (i == 0) {
            LOCK(i);
            if (items[i].tag == Thread.currentThread().getId()) {
                items[i].tag = AVAILABLE;
            }
            UNLOCK(i);
        }
    }

    Pair<Integer, Integer> pop() {
        // Grab item from bottom of heap
        // to replace to-be-deleted top item
        sizeLock.lock();
        size--;
        int bottom = size;

        LOCK(bottom);
        sizeLock.unlock();
        Pair<Integer, Integer> priority = items[bottom].priority;
        items[bottom].tag = EMPTY;
        UNLOCK(bottom);

        // Lock first item. Stop if it was the only item in the heap
        ReentrantLock lastLock = items[0].lock;

        LOCK(0);
        if (items[0].tag == EMPTY) {
            UNLOCK(0);
            return priority;
        }

        // Replace the top item with the tem stored
        // from the bottom
        Pair<Integer, Integer> temp = items[0].priority;
        items[0].priority = priority;
        priority = temp;
        items[0].tag = AVAILABLE;

        // Adjust heap starting at top. Always hold lock
        // on item being adjusted
        int i = 0;
        while (i < MAX_SIZE / 2) {
            int left = i * 2 + 1;
            int right = i * 2 + 2;
            Lock leftLock = items[left].lock;
            Lock rightLock = items[right].lock;
            int child;
            Lock childLock;
            leftLock.lock();
            rightLock.lock();
            if (items[left].tag == EMPTY) {
                rightLock.unlock();
                leftLock.unlock();
                break;
            } else if ((items[right].tag == EMPTY) ||
                    (items[left].priority.getKey().compareTo(items[right].priority.getKey()) > 0)) {
                rightLock.unlock();
                child = left;
                childLock = leftLock;
            } else {
                leftLock.unlock();
                child = right;
                childLock = rightLock;
            }
            // If child has higher priority than parent then swap
            // If not, stop
            if (items[child].priority.getKey().compareTo(items[i].priority.getKey()) > 0) {
                swap_items(child, i);
                childLock.unlock();
                i = child;
            } else {
                childLock.unlock();
                break;
            }
        }
        lastLock.unlock();
        return priority;
    }


    /*
     * !!! Attention !!!
     * use it ONLY after ALL changes
     * in all threads are complete.
     * This method is only for demonstrating
     * final result and shouldn't be a part
     * of this class at all
     */
    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < size; i++) {
            res += items[i].priority + " ";
        }
        return res;
    }
}
