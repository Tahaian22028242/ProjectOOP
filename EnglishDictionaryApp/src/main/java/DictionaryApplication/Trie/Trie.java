package main.java.DictionaryApplication.Trie;

import java.util.*;

public class Trie {
    protected final Map<Character, Trie> children;
    protected String content;
    protected boolean terminal = false;

    public Trie() {
        this(null);
    }

    /**
     * Initialize class trie.
     */
    private Trie(String content) {
        this.content = content;
        children = new HashMap<Character, Trie>();
    }

    /**
     * Add a new character to content, then add them to the trie.
     */
    protected void add(char character) {
        String res;
        if (this.content == null) {
            res = Character.toString(character);
        } else {
            res = this.content + character;
        }
        children.put(character, new Trie(res));
    }

    /**
     * Insert diagnosis to the trie.
     */
    public void insert(String diagnosis) {
        if (diagnosis == null) {
            throw new IllegalArgumentException("Null diagnoses that entries are not valid, please retry.");
        }
        Trie node = this;
        for (char c : diagnosis.toCharArray()) {
            if (!node.children.containsKey(c)) {
                node.add(c);
            }
            node = node.children.get(c);
        }
        node.terminal = true;
    }

    public List<String> autoComplete(String prefix) {
        Trie node = this;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return null;
            }
            node = node.children.get(c);
        }
        return node.allPrefixes();
    }

    protected List<String> allPrefixes() {
        List<String> diagnosisResults = new ArrayList<>();
        if (this.terminal) {
            diagnosisResults.add(this.content);
        }
        for (Map.Entry<Character, Trie> entry : children.entrySet()) {
            Trie child = entry.getValue();
            Collection<String> childPrefixes = child.allPrefixes();
            diagnosisResults.addAll(childPrefixes);
        }
        return diagnosisResults;
    }
}
