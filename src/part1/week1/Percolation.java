package part1.week1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {

    private final boolean[] isOpened;
    private final int size;
    private final WeightedQuickUnionUF a;
    private int openSizes = 0;
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("Size <= 0 is not allowed");
        a = new WeightedQuickUnionUF(n*n+2);
        isOpened = new boolean[n*n+2];
        size = n;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size)
            throw new IllegalArgumentException();
        int arrayCell = (row - 1) * size + col;
        if (isOpened[arrayCell])
            return;
        isOpened[arrayCell] = true;
        openSizes++;

        if (row == 1)
            a.union(arrayCell, 0);
        if (row == size)
            a.union(arrayCell, size * size + 1);
        if (row != 1 && isOpened[(row-2)*size+col])
            a.union((row-2)*size+col, arrayCell);
        if (row != size && isOpened[row*size+col])
            a.union(row*size+col, arrayCell);
        if (col != 1 && isOpened[(row-1)*size+col - 1])
            a.union((row-1)*size+col - 1, arrayCell);
        if (col != size && isOpened[(row-1)*size+col+1])
            a.union((row-1)*size+col+1, arrayCell);
    }

    private int mapTo1D(){
        return 0;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size)
            throw new IllegalArgumentException();
        return isOpened[(row - 1) * size + col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size)
            throw new IllegalArgumentException();
        return a.find((row - 1) * size + col) == a.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSizes;
    }

    // does the system percolate?
    public boolean percolates() {
        return a.find(size * size + 1) == a.find(0);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation kek = new Percolation(100);
        while (!kek.percolates()) {
           kek.open(StdRandom.uniform(100) + 1, StdRandom.uniform(100) + 1);
        }
        System.out.println(kek.openSizes);

    }
}

