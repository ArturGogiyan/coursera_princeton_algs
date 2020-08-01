package part1.week4;

import java.util.ArrayList;

public class Board {

    private final int[][] tiles;
    private  final int length;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        length = tiles.length;
        this.tiles = new int[length][length];
        for (int i = 0; i < length; i++)
            System.arraycopy(tiles[i], 0, this.tiles[i], 0, length);
    }

    // string representation of this board
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(length).append("\n");
        for (int[] tile : tiles) {
            for (int j = 0; j < length; j++)
                result.append(tile[j]).append(" ");
            result.append("\n");
        }
        return result.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        int pointer = 0;
        int hammingDistance = 0;
        for (int[] tile : tiles) {
            for (int j = 0; j < length; j++) {
                pointer++;
                if (tile[j] == 0 || tile[j] == pointer)
                    continue;
                hammingDistance++;
            }
        }
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int pointer = 0;
        int manhattanDistance = 0;
        for (int[] tile : tiles) {
            for (int j = 0; j < length; j++) {
                pointer++;
                if (tile[j] == 0 || tile[j] == pointer + 1)
                    continue;
                manhattanDistance += calculateManhattanDistance(pointer, tile[j]);
            }
        }
        return manhattanDistance;
    }

    private int calculateManhattanDistance(int expected, int actual) {
        int result = 0;
        while (actual != expected) {
            result++;
            if (actual - expected > 2)
                actual -= 3;
            else if (actual - expected < -2)
                actual += 3;
            else if (actual < expected)
                actual++;
            else
                actual--;

        }
        return result;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y)
            return true;
        if (y == null)
            return false;
        if (getClass() != y.getClass())
            return false;
        Board that = (Board) y;
        if (that.tiles.length != tiles.length)
            return false;

        for (int i = 0; i < length; i++)
            for (int j = 0; j < length; j++)
                if (this.tiles[i][j] != that.tiles[i][j])
                    return false;
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int[][] aux = new int[length][length];
        int zeroX = -1;
        int zeroY = -1;
        for (int i = 0; i < length; i++)
            for (int j = 0; j < length; j++) {
                aux[i][j] = tiles[i][j];
                if (aux[i][j] == 0) {
                    zeroX = j;
                    zeroY = i;
                }
            }
        ArrayList<Board> boards = new ArrayList<>();
        if (zeroX > 0) {
            swap(aux, zeroX, zeroY, zeroX - 1, zeroY);
            boards.add(new Board(aux));
            swap(aux, zeroX, zeroY, zeroX - 1, zeroY);
        }
        if (zeroX < length - 1) {
            swap(aux, zeroX, zeroY, zeroX + 1, zeroY);
            boards.add(new Board(aux));
            swap(aux, zeroX, zeroY, zeroX + 1, zeroY);
        }
        if (zeroY > 0) {
            swap(aux, zeroX, zeroY, zeroX, zeroY - 1);
            boards.add(new Board(aux));
            swap(aux, zeroX, zeroY, zeroX, zeroY - 1);
        }
        if (zeroY < length - 1) {
            swap(aux, zeroX, zeroY, zeroX, zeroY + 1);
            boards.add(new Board(aux));
            swap(aux, zeroX, zeroY, zeroX, zeroY + 1);
        }
        return boards;
    }

    private void swap(int[][] arr, int x1, int y1, int x2, int y2) {
        int buff = arr[y1][x1];
        arr[y1][x1] = arr[y2][x2];
        arr[y2][x2] = buff;
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twin = new int[length][length];
        boolean changed = false;
        int valueI = -1;
        int valueJ = -1;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (tiles[i][j] == 0 || changed) {
                    twin[i][j] = tiles[i][j];
                }
                else if (valueI == -1) {
                    valueI = i;
                    valueJ = j;
                }
                else {
                    changed = true;
                    twin[i][j] = tiles[valueI][valueJ];
                    twin[valueI][valueJ] = tiles[i][j];
                }
            }
        }
        return new Board(twin);
    }

    // unit testing (not graded)
    //public static void main(String[] args) {
    //}
}