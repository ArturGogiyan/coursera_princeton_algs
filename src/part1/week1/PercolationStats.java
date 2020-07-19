package part1.week1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] openSizes;
    private final int trials;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        openSizes = new double[trials];
        this.trials = trials;
        for (int i = 0; i < trials; i++) {
            Percolation kek = new Percolation(n);
            while (!kek.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int column = StdRandom.uniform(n) + 1;
                if (!kek.isOpen(row, column)) {
                    kek.open(row, column);
                }
            }
            openSizes[i] = (double) kek.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(openSizes);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(openSizes);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev() / trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev() / trials);
    }

    private void printResults() {
        System.out.println("mean = " + mean());
        System.out.println("stddev = " + stddev());
        System.out.println("95% confidence interval = [" + confidenceLo() + ", " + confidenceHi() +"]");
    }

    // test client (see below)
    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        int experimentsCount = Integer.parseInt(args[1]);
        if (size < 1 || experimentsCount < 1)
            throw new IllegalArgumentException();
        PercolationStats stats = new PercolationStats(size, experimentsCount);
        stats.printResults();
    }


}