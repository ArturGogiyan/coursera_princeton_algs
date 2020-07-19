package part1.week2;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private class Node {
        Item item;
        Node next;
    }
    private Node tail = null;
    private Node head = null;

    private int size;
    // construct an empty randomized queue
    public RandomizedQueue() {

    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return tail == null;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        Node oldHead = head;
        head = new Node();
        head.item = item;
        head.next = null;
        if (isEmpty())
            tail = head;
        else
            oldHead.next = head;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0)
            throw new NoSuchElementException();
        int itemId = StdRandom.uniform(size);
        if (itemId == 0) {
            Node oldTail = tail;
            tail = tail.next;
            return oldTail.item;
        }
        Node cur = tail;
        for (int i = 0; i < itemId - 1; i++)
            cur = cur.next;
        Item result = cur.next.item;
        if (cur.next == head)
            head = cur;
        cur.next = cur.next.next;
        size--;
        return result;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0)
            throw new NoSuchElementException();
        return getItemByIndex(StdRandom.uniform(size));
    }

    private Item getItemByIndex(int index) {
        return getNodeByIndex(index).item;
    }

    private Node getNodeByIndex(int index) {
        Node cur = tail;
        for (int i = 0; i < index; i++)
            cur = cur.next;
        return cur;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        System.out.println("isEmpty Check - " + (queue.isEmpty() ? "OK" : "FAILED"));
        queue.enqueue(5);
        queue.enqueue(10);
        int size = queue.size();
        System.out.println("size Check - " + (size == 2 ? "OK" : "FAILED"));
        int sample = queue.sample();
        System.out.println("sample Check - " + (sample == 5 || sample == 10 ? "OK" : "FAILED"));
        int first = queue.dequeue();
        System.out.println("enqueue Check - " + (first == 5 || first == 10 ? "OK" : "FAILED"));
        System.out.println("dequeue Check - " + (first == 5 || first == 10 ? "OK" : "FAILED"));
        for (int i : queue) {
            System.out.println("iterator Check - " + (i == 10 || i == 5 ? "OK" : "FAILED"));
        }
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        int[] indices;
        int current;
        public RandomizedQueueIterator() {
            indices = StdRandom.permutation(size);
        }

        @Override
        public boolean hasNext() {
            return current != size;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return getItemByIndex(indices[current++]);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
