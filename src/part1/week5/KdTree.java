import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.ArrayList;

public class KdTree {

    private TreeNode root;


    public KdTree() { // construct an empty set of points
        root = null;
    }

    public boolean isEmpty() { // is the set empty?
        if (root == null)
            return true;
        return root.getSize() == 0;
    }

    public int size() { // number of points in the set
        if (root == null)
            return 0;
        return root.getSize();
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new IllegalArgumentException();
        root = insert(root, p, 0);
    }

    private TreeNode insert(TreeNode x, Point2D point, int level) {
        if (x == null) {
            var node = new TreeNode(point);
            node.setSize(1);
            return node;
        }
        if (x.getValue().compareTo(point) == 0) {
            x.setValue(point);
            return x;
        }
        if (level % 2 == 0) { // x
            if (x.getValue().x() > point.x())
                x.setLeft(insert(x.getLeft(), point, level + 1));
            else if (x.getValue().x() < point.x())
                x.setRight(insert(x.getRight(), point, level + 1));
            else {
                int cmp = x.getValue().compareTo(point);
                if (cmp < 0)
                    x.setLeft(insert(x.getLeft(), point, level + 1));
                else
                    x.setRight(insert(x.getRight(), point, level + 1));
            }
        } else { // y
            if (x.getValue().y() > point.y())
                x.setLeft(insert(x.getLeft(), point, level + 1));
            else if (x.getValue().y() < point.y())
                x.setRight(insert(x.getRight(), point, level + 1));
            else {
                int cmp = x.getValue().compareTo(point);
                if (cmp < 0)
                    x.setLeft(insert(x.getLeft(), point, level + 1));
                else
                    x.setRight(insert(x.getRight(), point, level + 1));
            }
        }
        int newSize = 1;
        if (x.getLeft() != null)
            newSize += x.getLeft().getSize();
        if (x.getRight() != null)
            newSize += x.getRight().getSize();
        x.setSize(newSize);
        return x;
    }



    public boolean contains(Point2D p) { // does the set contain point p?
        if (p == null)
            throw new IllegalArgumentException();
        return contains(p, root, 0);
    }

    private boolean contains(Point2D p, TreeNode x, int level) {
        if (x == null)
            return false;
        if (p.compareTo(x.getValue()) == 0)
            return true;
        if ((level % 2 == 0 && x.getValue().x() > p.x()) || (level % 2 == 1 && x.getValue().y() > p.y()))
            return contains(p, x.getLeft(), level + 1);
        else if ((level % 2 == 0 && x.getValue().x() < p.x()) || (level % 2 == 1 && x.getValue().y() < p.y()))
            return contains(p, x.getRight(), level + 1);
        else {
            return (contains(p, x.getLeft(), level + 1) || contains(p, x.getRight(), level + 1));
        }
    }

    public void draw() { // draw all points to standard draw
        ArrayList<Point2D> arr = new ArrayList<>();
        iterator(arr, root);
        for (Point2D p : arr)
            p.draw();
    }

    private void iterator(ArrayList<Point2D> array, TreeNode x) {
        if (x == null)
            return;
        iterator(array, x.getLeft());
        array.add(x.getValue());
        iterator(array, x.getRight());

    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        if (rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> result = new ArrayList<>();
        range(rect, result, root, 0);
        return result;
    }

    private void range(RectHV rect, ArrayList<Point2D> array, TreeNode x, int level) {
        if (x == null)
            return;
        if (rect.contains(x.getValue()))
            array.add(x.getValue());
        if ((level % 2 == 0 && rect.xmin() < x.getValue().x()) || (level % 2 == 1 && rect.ymin() < x.getValue().y()))
            range(rect, array, x.getLeft(), level + 1);
        if ((level % 2 == 0 && rect.xmax() > x.getValue().x()) || (level % 2 == 1 && rect.ymax() > x.getValue().y()))
            range(rect, array, x.getRight(), level + 1);
    }


    public Point2D nearest(Point2D p) {  // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null)
            throw new IllegalArgumentException();

        return nearest(p, root, Double.POSITIVE_INFINITY, 0, null);
    }

    private Point2D nearest(Point2D p, TreeNode x, double distance, int level, Point2D nearest) {
        if (x == null)
            return nearest;
        double xDif = p.x() - x.getValue().x();
        double yDif = p.y() - x.getValue().y();
        double currentDistance = xDif * xDif + yDif * yDif;
        if (currentDistance < distance) {
            nearest = x.getValue();
            distance = currentDistance;
        }
        if (level % 2 == 0) {
            if (x.getValue().x() > p.x()) {
                nearest = nearest(p, x.getLeft(), distance, level + 1, nearest);
                double distanceToOpposite = x.getValue().x() - p.x();
                distanceToOpposite = distanceToOpposite * distanceToOpposite;
                if (distanceBetween(nearest, p) > distanceToOpposite)
                    nearest = nearest(p, x.getLeft(), distance, level + 1, nearest);
            }
            else if (x.getValue().x() < p.x()) {
                nearest = nearest(p, x.getRight(), distance, level + 1, nearest);
                double distanceToOpposite = x.getValue().x() - p.x();
                distanceToOpposite = distanceToOpposite * distanceToOpposite;
                if (distanceBetween(nearest, p) > distanceToOpposite)
                    nearest = nearest(p, x.getLeft(), distance, level + 1, nearest);
            }
            else {
                nearest = nearest(p, x.getLeft(), distance, level + 1, nearest);
                nearest = nearest(p, x.getRight(), distance, level + 1, nearest);
            }
        } else {
            if (x.getValue().y() > p.y()) {
                nearest = nearest(p, x.getLeft(), distance, level + 1, nearest);
                double distanceToOpposite = x.getValue().y() - p.y();
                distanceToOpposite = distanceToOpposite * distanceToOpposite;
                if (distanceBetween(nearest, p) > distanceToOpposite)
                    nearest = nearest(p, x.getLeft(), distance, level + 1, nearest);
            }
            else if (x.getValue().y() < p.y()) {
                nearest = nearest(p, x.getRight(), distance, level + 1, nearest);
                double distanceToOpposite = x.getValue().y() - p.y();
                distanceToOpposite = distanceToOpposite * distanceToOpposite;
                if (distanceBetween(nearest, p) > distanceToOpposite)
                    nearest = nearest(p, x.getLeft(), distance, level + 1, nearest);
            }
            else {
                nearest = nearest(p, x.getLeft(), distance, level + 1, nearest);
                nearest = nearest(p, x.getRight(), distance, level + 1, nearest);
            }
        }
        return nearest;
    }

    private double distanceBetween(Point2D a, Point2D b) {
        double xDif = a.x() - b.x();
        double yDif = a.y() - b.y();
        return xDif * xDif + yDif * yDif;
    }


    public static void main(String[] args) {
        var tree = new KdTree();
        tree.insert(new Point2D(0.5, 0.5));
        var x = tree.contains(new Point2D(0.5, 0.79));
         tree.insert(new Point2D(0.2, 0.4));
        tree.insert(new Point2D(0.4, 0.2));
        // var range = tree.range(new RectHV(0.1,0.1,0.5,0.5));
        // var nearest = tree.nearest(new Point2D(0.15,0.3));
    }

}

class TreeNode {
    private TreeNode left;
    private TreeNode right;
    private Point2D value;
    private int size = 0;

    public TreeNode(Point2D value) {
        this.value = value;
    }

    public Point2D getValue() {
        return value;
    }

    public TreeNode getLeft() {
        return left;
    }

    public TreeNode getRight() {
        return right;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public void setValue(Point2D value) {
        this.value = value;
    }
}
