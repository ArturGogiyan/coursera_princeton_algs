package week3;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private final Point[] points;
    private int numberOfSegments = 0;
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {    // finds all line segments containing 4 points
        if (points == null)
            throw new IllegalArgumentException();
        this.points = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                    throw new IllegalArgumentException();
            this.points[i] = points[i];
        }
        calculate();
    }
    public int numberOfSegments() {        // the number of line segments
        return numberOfSegments;
    }
    public LineSegment[] segments() {                // the line segments
        LineSegment[] result = new LineSegment[segments.length];
        for (int i = 0; i < segments.length; i++)
            result[i] = segments[i];
        return result;
    }

    private void calculate() {
        int length = points.length;
        ArrayList<LineSegment> arrayList = new ArrayList<>();
        Arrays.sort(points);
        for (int i = 1; i < points.length; i++) {
            if (points[i - 1].compareTo(points[i]) == 0)
                throw new IllegalArgumentException();
        }
        for (int p = 0; p < length; p++) {
            for (int q = p + 1; q < length; q++) {
                for (int r = q + 1; r < length; r++) {
                    for (int s = r + 1; s < length; s++) {
                        double pqSlope = points[p].slopeTo(points[q]);
                        if (pqSlope == Double.NEGATIVE_INFINITY)
                            throw new IllegalArgumentException();
                        if (pqSlope == points[p].slopeTo(points[r]) && pqSlope == points[p].slopeTo(points[s])) {
                            numberOfSegments++;
                            arrayList.add(new LineSegment(points[p], points[s]));
                        }
                    }
                }
            }
        }
        segments = new LineSegment[arrayList.size()];
        segments = arrayList.toArray(segments);
    }


    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}