package part2.week4;

import edu.princeton.cs.algs4.In;
import java.util.Random;

public class BoggleBoard {
    // Initializes a random 4-by-4 Boggle board.
    // (by rolling the Hasbro dice)

    private final char[][] board;
    private static final int ALPHABET_SIZE = 26;
    private static final int A_ASCII_CODE = 65;


    public BoggleBoard() {
        Random r = new Random();
        int size = 4;
        board = new char[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                board[i][j] = (char) ((r.nextInt() % ALPHABET_SIZE) + A_ASCII_CODE);
    }

    // Initializes a random m-by-n Boggle board.
    // (using the frequency of letters in the English language)
    public BoggleBoard(int m, int n) {
        Random r = new Random();
        board = new char[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                board[i][j] = (char) ((r.nextInt() % ALPHABET_SIZE) + A_ASCII_CODE);
    }

    // Initializes a Boggle board from the specified filename.
    public BoggleBoard(String filename) {
        In in = new In(filename);
        int sizeX = in.readInt();
        int sizeY = in.readInt();
        board = new char[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++)
            for (int j = 0; j < sizeY; j++) {
                char c = in.readChar();
                while (c < A_ASCII_CODE || c > A_ASCII_CODE+ALPHABET_SIZE) // for Qu case
                    c = in.readChar();
                board[i][j] = c;
            }
    }

    // Initializes a Boggle board from the 2d char array.
    // (with 'Q' representing the two-letter sequence "Qu")
    public BoggleBoard(char[][] a) {
        board = new char[a.length][a[0].length];
        for (int i = 0; i < a.length; i++)
            for (int j = 0; i < a[0].length; j++)
                board[i][j] = a[i][j];
    }

    // Returns the number of rows.
    public int rows() {
        return board[0].length;
    }

    // Returns the number of columns.
    public int cols() {
        return board.length;
    }

    // Returns the letter in row i and column j.
    // (with 'Q' representing the two-letter sequence "Qu")
    public char getLetter(int i, int j) {
        return board[i][j];
    }

    // Returns a string representation of the board.
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (char[] chars : board)
            for (int j = 0; j < board[0].length; j++)
                builder.append(chars[j]);
        return builder.toString();
    }
}
