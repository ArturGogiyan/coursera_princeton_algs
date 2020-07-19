package week4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Solver {

    private final ArrayList<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null));
        MinPQ<SearchNode> twinPQ = new MinPQ<>();
        twinPQ.insert(new SearchNode(initial.twin(), 0, null));
        while (true) {
            var node = pq.delMin();
            var twinNode = twinPQ.delMin();
            if (twinNode.isGoal()) {
                solution = null;
                break;
            }
            if (node.isGoal()) {
                solution = new ArrayList<>(node.getMoves() + 1);
                for (int i = 0; i < node.getMoves() + 1; i++)
                    solution.add(null);
                int i = node.getMoves();
                for (SearchNode currNode = node; currNode != null ; currNode = currNode.getPreviousMove())
                    solution.set(i--, currNode.getBoard());
                break;
            }
            processNode(pq, node);
            processNode(twinPQ, twinNode);
        }
    }

    private void processNode(MinPQ<SearchNode> pq, SearchNode node) {
        var nodes = node.getBoard().neighbors();
        for (var currNode : nodes)
            if (node.getPreviousMove() == null || !node.getPreviousMove().getBoard().equals(currNode)) {
                pq.insert(new SearchNode(currNode, node.getMoves() + 1, node));
            }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solution == null ? -1 : solution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

class SearchNode  implements Comparable<SearchNode>{
    private final Board board;
    private final int moves;
    private final SearchNode previousMove;
    private final int manhattan;
    private final int hamming;

    public SearchNode(Board board, int moves, SearchNode previousMove) {
        this.board = board;
        this.moves = moves;
        this.previousMove = previousMove;
        manhattan = board.manhattan();
        hamming = board.hamming();
    }

    public Board getBoard() {
        return board;
    }

    public int getMoves() {
        return moves;
    }

    public SearchNode getPreviousMove() {
        return previousMove;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return hamming == 0;
    }

    @Override
    public int compareTo(SearchNode searchNode) {
        int a = moves + manhattan();
        int b = searchNode.moves + searchNode.manhattan();
        return a - b;
    }
}
