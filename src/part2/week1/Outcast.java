package part2.week1;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet net;

    // constructor takes a part2.week1.WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null)
            throw new IllegalArgumentException();

        net = wordnet;
    }

    // given an array of part2.week1.WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
       if (nouns == null)
           throw new IllegalArgumentException();
       String leatsRelatedNoun = null;
       int maxDistance = 0;
       for (int i = 0; i < nouns.length; i++) {
           int sumLength = -1;
           for (int j = 0; j < nouns.length; j++) {
               if (i == j)
                   continue;
               sumLength += net.distance(nouns[i], nouns[j]);
           }
           if (sumLength > maxDistance) {
               maxDistance = sumLength;
               leatsRelatedNoun = nouns[i];
           }
       }
        return leatsRelatedNoun;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        int kek = wordnet.distance("3","3");
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
