package DictionaryApplication.Operations;

import DictionaryApplication.DictionaryAlerts.DictionaryAlerts;
import DictionaryApplication.DictionaryCommandline.Dictionary;
import DictionaryApplication.DictionaryCommandline.DictionaryManagement;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SearcherController implements Initializable {

    private final String path = "src/main/resources/Utils/dictionaries.txt";
    public AnchorPane searchResult;
    public Pane searchBox;
    public Button volumeButton;
    ObservableList<String> list = FXCollections.observableArrayList();
    
    @FXML
    private TextField searchTerm;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private Label englishWord, listHeader, notAvailableAlert;
    @FXML
    private TextArea explanation;
    @FXML
    private ListView<String> listResults;
    @FXML
    private Pane headerOfExplanation;

    private final Dictionary dictionary = new Dictionary();
    private final DictionaryManagement dictionaryManagement = new DictionaryManagement();
    private final DictionaryAlerts dictionaryAlerts = new DictionaryAlerts();
    private int indexOfSelectedWord;
    private int firstIndexOfListFound;

    /**
     * Set default format of list.
     */
    private void setListDefault(int index) {
        list.clear();
        if (index == 0) {
            listHeader.setText("The first 20 words(20 từ đầu tiên)");
        } else {
            listHeader.setText("Related results(Kết quả liên quan)");
        }
        //Add 20 related results to list.
        for (int i = index; i < index + 20; ++i) {
            list.add(dictionary.get(i).getWordTarget());
        }
        //Bring the searching results from list to result list.
        listResults.setItems(list);
        //Set the text of the word target and explanation of the results.
        englishWord.setText(dictionary.get(index).getWordTarget());
        englishWord.setText(dictionary.get(index).getWordExplain());
    }

    /**
     * Override initialize method.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dictionaryManagement.insertFromFile(dictionary, path);
        System.out.println(dictionary.size());
        dictionaryManagement.setTrie(dictionary);
        setListDefault(0);

        searchTerm.setOnKeyTyped(keyEvent -> {
            if (searchTerm.getText().isEmpty()) {
                cancelButton.setVisible(false);
                setListDefault(0);
            } else {
                cancelButton.setVisible(true);
                handleOnKeyTyped();
            }
        });

        cancelButton.setOnAction(actionEvent -> {
            searchTerm.clear();
            notAvailableAlert.setVisible(false);
            cancelButton.setVisible(false);
            setListDefault(0);
        });

        explanation.setVisible(false);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
        notAvailableAlert.setVisible(false);
    }

    /**
     * Handle typing keywords for searching.
     */
    @FXML
    private void handleOnKeyTyped() {
        list.clear();
        String searchKey = searchTerm.getText().trim();
        //Search results using search key, then add them to list.
        list = dictionaryManagement.dictionaryLookUp(searchKey);
        //If the word searched is not available in dictionary.
        if (list.isEmpty()) {
            notAvailableAlert.setVisible(true);
            setListDefault(firstIndexOfListFound);
        } else /* if available */ {
            notAvailableAlert.setVisible(false);
            listHeader.setText("Result available(Kết quả tìm kiếm đây rồi)!");
            listResults.setItems(list);
            firstIndexOfListFound = dictionaryManagement.dictionarySearcher(dictionary, list.get(0));
        }
    }

    /**
     * Handle showing information of a word when it is clicked.
     */
    @FXML
    private void handleMouseClickAWord() {
        String selectedWord = listResults.getSelectionModel().getSelectedItem();
        if (selectedWord != null) {
            indexOfSelectedWord = dictionaryManagement.dictionarySearcher(dictionary, selectedWord);
            if (indexOfSelectedWord == -1) return;
            englishWord.setText(dictionary.get(indexOfSelectedWord).getWordTarget());
            englishWord.setText(dictionary.get(indexOfSelectedWord).getWordExplain());
            headerOfExplanation.setVisible(true);
            explanation.setVisible(true);
            explanation.setEditable(false);
            saveButton.setVisible(false);
        }
    }

    /**
     * Handle clicking-to-edit-explanation button.
     */
    @FXML
    private void handleClickingEditButton() {
        explanation.setEditable(true);
        saveButton.setVisible(true);
        dictionaryAlerts.showAlertWarning("Warning(Cảnh báo)",
                "Explanation editing allowed\n" +
                        "Bạn đã cho phép chỉnh sửa nghĩa của từ này!");
    }

    /**
     * Handle sound when clicking to select a word.
     */
    @FXML
    private void handleClickingSoundButton() {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");
        if (voice != null) {
            voice.allocate();
            voice.speak(dictionary.get(indexOfSelectedWord).getWordTarget());
        } else {
            throw new IllegalStateException("Error: Cannot find voice: kevin16 !");
        }
    }

    /**
     * Handle word meaning updating function when clicking.
     */
    @FXML
    private void handleClickingUpdateButton() {
        Alert alertConfirmation = dictionaryAlerts.alertConfirmation("Update meaning of this word?",
                "Bạn muốn cập nhật nghĩa của từ này?");
        Optional<ButtonType> option = alertConfirmation.showAndWait();
        if (option.isPresent()) {
            if (option.get() == ButtonType.OK) {
                dictionaryManagement.updateWord(dictionary, indexOfSelectedWord,
                        explanation.getText(), path);
                dictionaryAlerts.showAlertInformation("Successfully updated!",
                        "Cập nhật nghĩa thành công!");
            } else {
                dictionaryAlerts.showAlertWarning("Update failed or cancelled!",
                        "Cập nhật thất bại hoặc đã bị huỷ!");
            }
            saveButton.setVisible(false);
            explanation.setEditable(false);
        }
    }

    /**
     * Renew the list of results after deleting a word.
     */
    private void renewListOfResultsAfterDeleting() {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).equals(englishWord.getText())) {
                list.remove(i);
                break;
            }
        }
        listResults.setItems(list);
        listHeader.setVisible(false);
        explanation.setVisible(false);
    }

    /**
     * Handle word deleting function when clicking.
     */
    @FXML
    private void handleClickingDeleteButton() {
        Alert alertWarning = dictionaryAlerts.alertWarning("Deleting alert",
                "Bạn chắc chắn muốn xoá từ này?");
        alertWarning.getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> option = alertWarning.showAndWait();
        if (option.isPresent()) {
            if (option.get() == ButtonType.OK) {
                dictionaryManagement.deleteWord(dictionary, indexOfSelectedWord, path);
                renewListOfResultsAfterDeleting();
                dictionaryAlerts.showAlertInformation("Announcement(Thông báo)",
                        "Successfully deleted(Xoá từ thành công)!");
            } else {
                dictionaryAlerts.showAlertWarning("Announcement(Thông báo)",
                        "Deletion failed or cancelled(Xoá từ thất bại hoặc đã bị huỷ)!");
            }
        }
    }
}
