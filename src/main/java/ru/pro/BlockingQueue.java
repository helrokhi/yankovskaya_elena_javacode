package ru.pro;

import lombok.AllArgsConstructor;

public class BlockingQueue<T> {
    @AllArgsConstructor
    private static class Node<E> {
        private final E item;
        private Node<E> next;
    }

    private final int capacity;
    private int count = 0;

    private Node<T> head;
    private Node<T> tail;

    public BlockingQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be > 0");
        this.capacity = capacity;
        this.head = this.tail = new Node<>(null, null);
    }

    public void enqueue(T element) throws InterruptedException {
        if (element == null) throw new NullPointerException();

        synchronized (this) {
            while (count == capacity) wait();
            Node<T> node = new Node<>(element, null);
            tail.next = node;
            tail = node;
            count++;
            notifyAll();
        }
    }

    public T dequeue() throws InterruptedException {
        synchronized (this) {
            while (count == 0) wait();
            Node<T> first = head.next;
            head.next = first.next;
            if (tail == first) tail = head;
            count--;
            notifyAll();
            return first.item;
        }
    }

    public int size() {
        synchronized (this) {
            return count;
        }
    }
}
