package DictionaryApplication.DictionaryCommandline;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.DictionaryApplication.Trie.Trie;

import java.io.*;
import java.util.List;

public class DictionaryManagement {
    private final Trie trie = new Trie();

    /**
     * Import data from dictionary.
     */
    public void insertFromFile(Dictionary dictionary, String path) {
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String englishWord = bufferedReader.readLine();
            englishWord = englishWord.replace("|", "");
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Word word = new Word();
                word.setWordTarget(englishWord.trim());
                String meaning = line + '\n';
                while ((line = bufferedReader.readLine()) != null) {
                    if (!line.startsWith("|")) {
                        meaning += line + '\n';
                    } else {
                        englishWord = line.replace("|", "");
                        break;
                    }
                }
                word.setWordExplain(englishWord.trim());
                dictionary.add(word);
            }
            bufferedReader.close();
        } catch (IOException ioException) {
            System.out.println("An I/O exception of some sort has occurred: " + ioException);
        } catch (Exception exception) {
            System.out.println("Something went wrong with inserting: " + exception);
        }
    }

    /**
     * Export data from dictionary to file.
     */
    public void dictionaryExportToFile(Dictionary dictionary, String path) {
        try {
            FileWriter fileWriter = new FileWriter(path);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (Word word : dictionary) {
                bufferedWriter.write("|" + word.getWordTarget() + "\n" + word.getWordExplain());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (Exception exception) {
            System.out.println("Something went wrong with exporting from dictionary: " + exception);
        }
    }

    /**
     * Look up words by commands.
     */
    public ObservableList<String> dictionaryLookUp(Dictionary dictionary, String key) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            List<String> results = trie.autoComplete(key);
            if (results != null) {
                int length = Math.min(results.size(), 15);
                for (int i = 0; i < length; ++i) {
                    list.add(results.get(i));
                }
            }
        } catch (Exception exception) {
            System.out.println("Something went wrong with dictionary looking up: " + exception);
        }
        return list;
    }

    /**
     * Searching tool.
     */
    public int dictionarySearcher(Dictionary dictionary, String keyWordToShowResults) {
        try {
            dictionary.sort(new ArrangeDictionaryByWords());
            int left = 0;
            int right = dictionary.size() - 1;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                int res = dictionary.get(mid).getWordTarget().compareTo(keyWordToShowResults);
                if (res == 0) {
                    return mid;
                }
                if (res <= 0) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        } catch (NullPointerException nullPointerException) {
            System.out.println("Searching Null Exception: " + nullPointerException);
        }
        return -1;
    }

    /**
     * Update word meaning by its index.
     */
    public void updateWord(Dictionary dictionary, int index, String meaning, String path) {
        try {
            dictionary.get(index).setWordExplain(meaning);
            dictionaryExportToFile(dictionary, path);
        } catch (NullPointerException nullPointerException) {
            System.out.println("Updating Null Exception: " + nullPointerException);
        }
    }

    /**
     * Delete a word by its index.
     */
    public void deleteWord(Dictionary dictionary, int index, String path) {
        try {
            dictionary.remove(index);
            setTrie(dictionary);
            dictionaryExportToFile(dictionary, path);
        } catch (NullPointerException nullPointerException) {
            System.out.println("Deleting Null Exception: " + nullPointerException);
        }
    }

    /**
     * Add a word to file.
     */
    public void addWord(Word word, String path) {
        try {
            FileWriter fileWriter = new FileWriter(path, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("|" + word.getWordTarget() + "\n" + word.getWordExplain());
            bufferedWriter.newLine();
        } catch (IOException ioException) {
            System.out.println("Adding I/O Exception: " + ioException);
        } catch (NullPointerException nullPointerException) {
            System.out.println("Adding Null Exception: " + nullPointerException);
        }
    }

    /**
     * Set timeout for app.
     */
    public void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception exception) {
                System.err.println("Time out exception!");
            }
        }).start();
    }

    /**
     * Set a trie from dictionary.
     */
    public void setTrie(Dictionary dictionary) {
        try {
            for (Word word : dictionary) {
                trie.insert(word.getWordTarget());
            }
        } catch (NullPointerException nullPointerException) {
            System.out.println("Set Trie Null Exception: " + nullPointerException);
        }
    }
}
