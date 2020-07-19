package part1.week2;

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int dequeueCount = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty())
            queue.enqueue(StdIn.readString());
        for (int i = 0; i < dequeueCount; i++)
            System.out.println(queue.dequeue());
    }
}
