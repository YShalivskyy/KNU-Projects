package com.ys.tasks.Queue14;


import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

public class Queue {
    private AtomicReference<Node> head;
    private AtomicReference<Node> tail;

    class Node {
        int value;
        AtomicReference<Node> nextNode;
        public Node(int val) {
            value = val;
            nextNode = new AtomicReference<>(null);
        }
    }

    public Queue() {
        Node dummy = new Node(0);
        head = new AtomicReference<>(dummy);
        tail = new AtomicReference<>(dummy);
    }

    public void enqueue(int value) {
        Node newNode = new Node(value);
        Node tailCopy;
        
        while (true) {
            tailCopy = tail.get();
            Node nextNode = tailCopy.nextNode.get();
            if (tailCopy.equals(tail.get())) {
                if (nextNode == null) {
                    if (tailCopy.nextNode.compareAndSet(nextNode, newNode));
                    break;
                }
                else {
                    tail.compareAndSet(tailCopy, nextNode);
                }
            }
        }
        tail.compareAndSet(tailCopy, newNode);
    }

    public int dequeue() {
        int value;

        while (true) {
            Node headCopy = head.get();
            Node tailCopy = tail.get();
            Node nextNode = headCopy.nextNode.get();
            
            if (headCopy.equals(head.get())) {
                if (headCopy == tailCopy) {
                    if (nextNode == null) {
                        throw new NoSuchElementException();
                    }
                    tail.compareAndSet(tailCopy, nextNode);
                }
                else {
                    value = nextNode.value;
                    if (head.compareAndSet(headCopy, nextNode))
                        break;
                }
            }
        }
        return value;
    }
}
