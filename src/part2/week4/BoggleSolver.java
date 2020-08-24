package part2.week4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

public class BoggleSolver {

    private final Trie trie = new Trie();

    // Initializes the data structure using the given array of strings as the dictionary.txt.
    // (You can assume each word in the dictionary.txt contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (var word : dictionary)
            trie.put(word.replace("QU", "Q"));
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> result = new HashSet<>();
        var usedCells = new HashSet<Long>();
        var builder = new StringBuilder();
        for (int i = 0; i < board.rows(); i++)
            for (int j = 0; j < board.cols(); j++) {
                usedCells.add(parseIntsToLong(i, j));
                appendValidWords(j, i, board, result, usedCells, builder);
                usedCells.remove(parseIntsToLong(i, j));
            }

        return result;
    }

    private void appendValidWords(int column, int row, BoggleBoard board, HashSet<String> words, HashSet<Long> usedCells, StringBuilder key) {
        key.append(board.getLetter(row, column));
        if (!trie.isValidRoot(key.toString())) {
            key.deleteCharAt(key.length() - 1);
            return;
        }
        if (trie.containsKey(key.toString())) {
            var exactWord = key.toString().replace("Q", "QU");
            if (exactWord.length() > 2)
                words.add(exactWord);
        }
        processCell(column - 1, row - 1, board, words, usedCells, key);
        processCell(column - 1, row, board, words, usedCells, key);
        processCell(column - 1, row + 1, board, words, usedCells, key);
        processCell(column, row - 1, board, words, usedCells, key);
        processCell(column, row + 1, board, words, usedCells, key);
        processCell(column + 1, row - 1, board, words, usedCells, key);
        processCell(column + 1, row, board, words, usedCells, key);
        processCell(column + 1, row + 1, board, words, usedCells, key);
        key.deleteCharAt(key.length() - 1);
    }

    private void processCell(int column, int row, BoggleBoard board, HashSet<String> words, HashSet<Long> usedCells, StringBuilder key) {
        long hash = parseIntsToLong(row, column);
        if (column >= 0 && column < board.cols() && row >= 0 && row < board.rows() && !usedCells.contains(hash)) {
            usedCells.add(hash);
            appendValidWords(column, row, board, words, usedCells, key);
            usedCells.remove(hash);
        }
    }

    private long parseIntsToLong(int a, int b) {
        return a * 0x100000000L + b;
    }


    // Returns the score of the given word if it is in the dictionary.txt, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        return trie.containsKey(word.replace("QU", "Q")) && word.length() > 2 ? getScore(word) : 0;
    }

    private int getScore(String word) {
        if (word.length() < 5) return 1;
        if (word.length() < 6) return 2;
        if (word.length() < 7) return 3;
        if (word.length() < 8) return 5;
        return 11;
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        StdOut.println("Score = " + solver.scoreOf("QUIS"));
    }
}
