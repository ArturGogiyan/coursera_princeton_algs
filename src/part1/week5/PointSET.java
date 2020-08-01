package part1.week5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> set;

    public PointSET() { // construct an empty set of points
        set = new TreeSet<>();
    }

    public boolean isEmpty() { // is the set empty?
       return set.isEmpty();
    }

    public int size() { // number of points in the set
        return set.size();
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new IllegalArgumentException();
        set.add(p);
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        if (p == null)
            throw new IllegalArgumentException();
        return set.contains(p);
    }

    public void draw() { // draw all points to standard draw
        for (Point2D p : set)
            p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        if (rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> result = new ArrayList<>();
        for (Point2D p : set)
           if (p.x() <= rect.xmax() && p.x() >= rect.xmin() && p.y() <= rect.ymax() && p.y() >= rect.ymin())
               result.add(p);
        return result;
    }

    public Point2D nearest(Point2D p) {  // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return null;
        double distance = Double.POSITIVE_INFINITY;
        Point2D minPoint = null;
        for (var currentPoint : set) {
            double xDelta = currentPoint.x() - p.x();
            double yDelta = currentPoint.y() - p.y();
            double currentDistance = Math.abs(xDelta * xDelta) + Math.abs(yDelta * yDelta);
            if (currentDistance < distance) {
                distance = currentDistance;
                minPoint = currentPoint;
            }
        }
        return minPoint;
    }

    // public static void main(String[] args) { }
}