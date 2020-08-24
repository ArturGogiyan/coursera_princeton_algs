package part2.week4;

public class Trie {
    private static final int R = 26;
    private static final int A_ASCII_CODE = 65;
    private TrieNode root  = new TrieNode();
    private static class TrieNode {
        private final TrieNode[] nodes = new TrieNode[R];
        private boolean hasValue;
    }

    public void put(String key) {
        root = put(root, key, 0);
    }

    private TrieNode put(TrieNode curNode, String key, int depth) {
        if (curNode == null) curNode = new TrieNode();
        if (depth == key.length()) { curNode.hasValue = true; return curNode; }
        char c = (char)(key.charAt(depth) - A_ASCII_CODE);
        curNode.nodes[c] = put(curNode.nodes[c], key, depth + 1);
        return curNode;
    }

    public boolean containsKey(String key) {
        key = key.replace("QU", "Q");
        var curNode = root;
        int i = 0;
        while (curNode != null) {
            if (i == key.length())
                return curNode.hasValue;
            char c = (char)(key.charAt(i) - A_ASCII_CODE);
            curNode = curNode.nodes[c];
            i++;
        }
        return false;
    }

    public boolean isValidRoot(String key) {
        key = key.replace("QU", "Q");
        var curNode = root;
        int i = 0;
        while (curNode != null) {
            if (i == key.length())
                return true;
            char c = (char)(key.charAt(i) - A_ASCII_CODE);
            curNode = curNode.nodes[c];
            i++;
        }
        return false;
    }
}

