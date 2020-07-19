package week3;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private final Point[] points;
    private int numberOfSegments = 0;
    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {     // finds all line segments containing 4 or more points
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

    public LineSegment[] segments() {               // the line segments
        LineSegment[] result = new LineSegment[segments.length];
        for (int i = 0; i < segments.length; i++)
            result[i] = segments[i];
        return result;
    }

    private void calculate() {
        int length = points.length;
        Arrays.sort(points);
        Point[] aux = new Point[length];
        for (int i = 1; i < length; i++) {
            aux[i] = points[i];
            if (points[i - 1].compareTo(points[i]) == 0)
                throw new IllegalArgumentException();
        }
        aux[0] = points[0];
        Point maxPoint = null;
        ArrayList<LineSegment> arrayList = new ArrayList<>();
        for (int p = 0; p < length; p++) {
            var comparator = points[p].slopeOrder();
            Arrays.sort(aux, comparator);
            double currentSlope = Double.NEGATIVE_INFINITY;
            int currentStreak = 0;
            for (int i = 1; i < length; i++) {
                double slope = points[p].slopeTo(aux[i]);
                if (points[p].compareTo(aux[i]) > 0) {
                    while (i < length && slope == points[p].slopeTo(aux[i]))
                        i++;
                    if (i == length)
                        continue;
                    maxPoint = aux[i];
                    currentSlope = points[p].slopeTo(aux[i]);
                    currentStreak = 1;
                }
                if (slope == currentSlope) {
                    if (points[p].compareTo(aux[i]) < 0) {
                        if (maxPoint == null || maxPoint.compareTo(aux[i]) < 0)
                            maxPoint = aux[i];
                        currentStreak++;
                        if (i == length - 1 && currentStreak > 2) {
                            arrayList.add(new LineSegment(points[p], aux[i]));
                            numberOfSegments++;
                        }
                    } else {
                        currentStreak = 0;
                        currentSlope = Double.NEGATIVE_INFINITY;
                    }
                }
                else {
                    currentSlope = slope;
                    if (currentStreak > 2) {
                        arrayList.add(new LineSegment(points[p], maxPoint));
                        numberOfSegments++;
                    }
                    maxPoint = aux[i];
                    currentStreak = 1;
                }
            }
        }
        segments = new LineSegment[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++)
            segments[i] = arrayList.get(i);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

