package DictionaryApplication.DictionaryCommandline;

import java.util.Objects;

public class Word {
    private String wordTarget;
    private String wordExplain;

    public Word() {
        wordExplain = " ";
        wordTarget = " ";
    }

    public Word(String wordExplain, String wordTarget) {
        this.wordExplain = wordExplain;
        this.wordTarget = wordTarget;
    }

    public String getWordExplain() {
        return wordExplain;
    }

    public void setWordExplain(String wordExplain) {
        this.wordExplain = wordExplain;
    }

    public String getWordTarget() {
        return wordTarget;
    }

    public void setWordTarget(String wordTarget) {
        this.wordTarget = wordTarget;
    }

    /**
     * Check synonyms.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Word word)) return false;
        return Objects.equals(wordTarget, word.wordTarget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wordTarget, wordExplain);
    }

    @Override
    public String toString() {
        return "Word{" + "wordTarget=" + wordTarget + '\'' + ", wordExplain=" + wordExplain + '\'' + '}';
    }
}
