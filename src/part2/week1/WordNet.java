package part2.week1;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;


public class WordNet {

    private final HashMap<Integer, Synset> intSynsetMap;
    private final HashMap<String, Synset> stringSynsetMap;
    private final Digraph digraph;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();
        In in = new In(synsets);
        intSynsetMap = new HashMap<Integer, Synset>();
        stringSynsetMap = new HashMap<String, Synset>();
        while (in.hasNextLine()) {
            var synset =  parseLineAsSynset(in.readLine());
            intSynsetMap.put(synset.getId(), synset);
            for (String synonym : synset.getSynonyms())
                stringSynsetMap.put(synonym, synset);
        }
        digraph = new Digraph(intSynsetMap.size());
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            var hypernym = parseLineAsHypernum(in.readLine());
            if (hypernym == null)
                continue;
            for (int hyp : hypernym.getHypernyms())
                digraph.addEdge(hypernym.getSynsetId(), hyp);
        }
        sap = new SAP(digraph);
    }

    private Synset parseLineAsSynset(String line) {
        int nextIndex = line.indexOf(',');
        String idSubstring = line.substring(0, nextIndex);
        int id = Integer.parseInt(idSubstring);
        line = line.substring(nextIndex + 1);
        nextIndex = line.indexOf(',');
        String subsetMerged = line.substring(0, nextIndex);
        String[] subset = subsetMerged.split("\\s+");
        line = line.substring(nextIndex + 1);
        return new Synset(id, subset, line);
    }

    private Hypernym parseLineAsHypernum(String line) {
        int nextIndex = line.indexOf(',');
        if (nextIndex == -1)
            return null;
        String idSubstring = line.substring(0, nextIndex);
        int id = Integer.parseInt(idSubstring);
        line = line.substring(nextIndex + 1);
        String[] hypernymsString = line.split(",");
        int[] hypernyms = new int[hypernymsString.length];
        for (int i = 0; i < hypernymsString.length; i++)
            hypernyms[i] = Integer.parseInt(hypernymsString[i]);
        return new Hypernym(id, hypernyms);
    }

    // returns all part2.week1.WordNet nouns
    public Iterable<String> nouns() {
        ArrayList<String> nouns = new ArrayList<>();
        for (var entry : stringSynsetMap.entrySet())
            nouns.add(entry.getKey());
        return nouns;
    }

    // is the word a part2.week1.WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();
        return stringSynsetMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        if (stringSynsetMap.get(nounA) == stringSynsetMap.get(nounB))
            return 0;
        return sap.length(stringSynsetMap.get(nounA).getId(), stringSynsetMap.get(nounB).getId());
    }


    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        if (stringSynsetMap.get(nounA) == stringSynsetMap.get(nounB))
            return nounA;
        return intSynsetMap.get(sap.ancestor(stringSynsetMap.get(nounA).getId(), stringSynsetMap.get(nounB).getId())).getSynonyms()[0];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet net = new WordNet("C:\\Users\\gogiyan.artur\\IdeaProjects\\alg1week1\\src\\synsets.txt","C:\\Users\\gogiyan.artur\\IdeaProjects\\alg1week1\\src\\hypernyms.txt");
        var ans = net.sap("cat","dog");
    }
}

class Synset {
    private final int id;
    private final String[] synonyms;
    private final String definition;

    public Synset (int id, String[] synonyms, String definition) {
        this.id = id;
        this.synonyms = new String[synonyms.length];
        this.definition = definition;
        System.arraycopy(synonyms, 0, this.synonyms, 0, synonyms.length);
    }

    public int getId() {
        return id;
    }

    public String getDefinition() {
        return definition;
    }

    public String[] getSynonyms() {
        return synonyms;
    }
}

class Hypernym {
    private final int synsetId;
    private final int[] hypernyms;

    public Hypernym(int synsetId, int[] hypernyms) {
        this.synsetId = synsetId;
        this.hypernyms = new int[hypernyms.length];
        System.arraycopy(hypernyms,0,this.hypernyms,0, hypernyms.length);
    }

    public int getSynsetId() {
        return synsetId;
    }

    public int[] getHypernyms() {
        return hypernyms;
    }
}

