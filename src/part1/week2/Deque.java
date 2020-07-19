package part1.week2;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Item[] arr;
    private int arraySize = 2;
    private int size;
    private int head;
    private int tail;
    // construct an empty deque
    public Deque() {
        arr = (Item[]) new Object[arraySize];
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (size == arraySize)
            resize(size * 2);
        arr[head++] = item;
        if (head == arraySize)
            head = 0;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (size == arraySize)
            resize(size * 2);
        tail = tail == 0 ? arraySize - 1 : tail - 1;
        arr[tail] = item;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0)
            throw new NoSuchElementException();
        head = head == 0 ? arraySize - 1 : head - 1;
        Item result = arr[head];
        arr[head] = null;
        size--;
        if (arraySize >= size*4 && size != 0)
            resize(size*2);
        return result;

    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size == 0)
            throw new NoSuchElementException();

        Item result = arr[tail];
        arr[tail] = null;
        tail = tail == arraySize - 1 ? 0 : tail + 1;
        size--;
        if (arraySize >= size*4 && size != 0)
            resize(size*2);
        return result;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private void resize(int newSize) {
        Item[] newArr = (Item[]) new Object[newSize];
        int i = tail;
        int j = 0;
        do {
            newArr[j] = arr[i];
            if (i == arraySize - 1)
                i = -1;
            i++;
            j++;
        } while (i != head);

        arr = newArr;
        arraySize = newSize;
        head = size;
        tail = 0;

    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> kek = new Deque<>();
        System.out.println("isEmpty Check - " + (kek.isEmpty() ? "OK" : "FAILED"));
        kek.addLast(10);
        kek.addFirst(5);

        int first = kek.removeFirst();
        System.out.println("addFirst Check - " + (first == 5 ? "OK" : "FAILED"));
        System.out.println("removeFirst Check - " + (first == 5 ? "OK" : "FAILED"));
        for (int i : kek) {
            System.out.println("iterator Check - " + (i == 10 ? "OK" : "FAILED"));
        }
        int last = kek.removeLast();
        System.out.println("addLast Check - " + (last == 10 ? "OK" : "FAILED"));
        System.out.println("removeLast Check - " + (last == 10 ? "OK" : "FAILED"));

    }

    private class DequeIterator implements Iterator<Item> {

        private int i = head;

        @Override
        public boolean hasNext() {
            return i != tail;
        }

        @Override
        public Item next() {
            if (i == tail)
                throw new NoSuchElementException();
            i = i == 0 ? arraySize - 1 : i - 1;
            Item result = arr[i];
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
