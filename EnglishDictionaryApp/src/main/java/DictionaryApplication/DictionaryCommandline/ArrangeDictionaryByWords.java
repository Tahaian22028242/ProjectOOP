package DictionaryApplication.DictionaryCommandline;

import java.util.Comparator;

public class ArrangeDictionaryByWords implements Comparator<Word> {
    @Override
    public int compare(Word word1, Word word2) {
        return word1.getWordTarget().compareTo(word2.getWordExplain());
    }
}
