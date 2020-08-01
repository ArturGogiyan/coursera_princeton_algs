package part2.week1;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;

public class SAP {

    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
        this.G = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || w < 0)
            throw new IllegalArgumentException();
        HashSet<Integer> pathA = new HashSet<>();
        getPathToRoot(pathA, v);
        Deque<BFSNode> queue = new ArrayDeque<>();
        queue.add(new BFSNode(0, w));
        HashSet<Integer> usedNodes = new HashSet<>();
        int foundedResult = -1;
        while (!queue.isEmpty()) {
            BFSNode curVertex = queue.removeFirst();
            if (usedNodes.contains(curVertex.getId()))
                continue;
            usedNodes.add(curVertex.getId());
            if (pathA.contains(curVertex.getId())) {
                if (foundedResult == -1)
                    foundedResult = distanceToAncestor(v, curVertex.getId()) + distanceToAncestor(w, curVertex.getId());
                else {
                    int newResult = distanceToAncestor(v, curVertex.getId()) + distanceToAncestor(w, curVertex.getId());
                    foundedResult = Math.min(foundedResult, newResult);
                }
            }
            if (foundedResult != -1 && foundedResult <= curVertex.getDepth())
                break;
            for (int id : G.adj(curVertex.getId()))
                queue.add(new BFSNode(curVertex.getDepth() + 1, id));
        }
        return foundedResult;
    }

    private int distanceToAncestor(int descendant, int ancestor) {
        if (descendant == ancestor)
            return 0;
        Deque<BFSNode> queue = new ArrayDeque<>();
        queue.add(new BFSNode(0, descendant));
        while (!queue.isEmpty()) {
            BFSNode curVertex = queue.removeFirst();
            if (curVertex.getId() == ancestor){
                return curVertex.getDepth();
            }
            for (int id : G.adj(curVertex.getId()))
                queue.add(new BFSNode(curVertex.getDepth() + 1, id));
        }
        return  -1;
    }

    private void getPathToRoot(HashSet<Integer> set, int start) {
        Deque<Integer> queue = new ArrayDeque<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            int curVertex = queue.removeFirst();
            if (set.contains(curVertex))
                continue;
            set.add(curVertex);
            for (int id : G.adj(curVertex))
                queue.add(id);
        }
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        HashSet<Integer> pathA = new HashSet<>();
        getPathToRoot(pathA, v);
        Deque<BFSNode> queue = new ArrayDeque<>();
        queue.add(new BFSNode(0, w));
        int foundedResult = -1;
        int foundedAncestor = -1;
        HashSet<Integer> usedNodes = new HashSet<>();
        while (!queue.isEmpty()) {
            BFSNode curVertex = queue.removeFirst();
            if (usedNodes.contains(curVertex.getId()))
                continue;
            usedNodes.add(curVertex.getId());
            if (pathA.contains(curVertex.getId())) {
                if (foundedResult == -1){
                    foundedResult = distanceToAncestor(v, curVertex.getId()) + distanceToAncestor(w, curVertex.getId());
                    foundedAncestor = curVertex.getId();
                }
                else {
                    int newResult = distanceToAncestor(v, curVertex.getId()) + distanceToAncestor(w, curVertex.getId());
                    if (foundedResult > newResult) {
                        foundedResult = newResult;
                        foundedAncestor = curVertex.getId();
                    }
                }
            }
            if (foundedResult != -1 && foundedResult <= curVertex.getDepth())
                break;
            for (int id : G.adj(curVertex.getId()))
                queue.add(new BFSNode(curVertex.getDepth() + 1, id));
        }
        return foundedAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();
        int minLength = Integer.MAX_VALUE;
        for (int curV : v) {
            for (int curW : w) {
                int curLength = length(curV, curW);
                if (minLength > curLength && curLength != -1)
                    minLength = curLength;
            }
        }
        return minLength == Integer.MAX_VALUE ? -1 : minLength;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();
        int minLength = Integer.MAX_VALUE;
        int minV = -1, minW = -1;
        for (int curV : v) {
            for (int curW : w) {
                int curLength = length(curV, curW);
                if (minLength > curLength) {
                    minW = curW;
                    minV = curV;
                    curLength = minLength;
                }
            }
        }
        return ancestor(minV, minW);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("C:\\Users\\gogiyan.artur\\IdeaProjects\\alg1week1\\src\\test1.txt");
        Digraph g = new Digraph(in.readInt());
        int count =  in.readInt();
        for (int i = 0; i < count; i++) {
            int first = in.readInt();
            int second = in.readInt();
            g.addEdge(first, second);
        }
        SAP s = new SAP(g);
        int k = s.length(64451, 25327);
        int an = s.ancestor(2, 5);
    }
}

class BFSNode {
    private final int depth;
    private final int id;

    public BFSNode(int depth, int id) {
        this.depth = depth;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getDepth() {
        return depth;
    }
}
