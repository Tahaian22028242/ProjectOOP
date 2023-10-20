package DictionaryApplication.Operations;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import DictionaryApplication.DictionaryAlerts.DictionaryAlerts;
import DictionaryApplication.DictionaryCommandline.Dictionary;
import DictionaryApplication.DictionaryCommandline.DictionaryManagement;
import DictionaryApplication.DictionaryCommandline.Word;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdditionController implements Initializable {
    private final Dictionary dictionary = new Dictionary();
    private final DictionaryManagement dictionaryManagement = new DictionaryManagement();
    private final DictionaryAlerts alerts = new DictionaryAlerts();

    @FXML
    private TextField wordTargetInput;
    @FXML
    private TextArea wordExplainInput;
    @FXML
    private Label successAlert;
    @FXML
    private Button addButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String path = "src/main/resources/Utils/dictionaries.txt";
        dictionaryManagement.insertFromFile(dictionary, path);
        if (wordExplainInput.getText().isEmpty() || wordTargetInput.getText().isEmpty()) {
            addButton.setDisable(true);
        }

        wordTargetInput.setOnKeyTyped(keyEvent -> addButton.setDisable(wordExplainInput.getText().isEmpty() || wordTargetInput.getText().isEmpty()));

        wordExplainInput.setOnKeyTyped(keyEvent -> addButton.setDisable(wordExplainInput.getText().isEmpty() || wordTargetInput.getText().isEmpty()));

        successAlert.setVisible(false);
    }

    /**
     * Handle all types of clicking buttons.
     */
    @FXML
    private void handleClickToAddButton() {
        Alert alertConfirmation = alerts.alertConfirmation("Add this word?", "Bạn xác nhận thêm từ này?");
        Optional<ButtonType> option = alertConfirmation.showAndWait();
        String englishWord = wordTargetInput.getText().trim();
        String meaning = wordExplainInput.getText().trim();

        if (option.isPresent() && option.get()== ButtonType.OK) {
            Word word = new Word(englishWord, meaning);
            if (dictionary.contains(word)) {
                int indexOfWord =  dictionaryManagement.dictionarySearcher(dictionary, englishWord);
                Alert selectionAlert = alerts.alertConfirmation("This word has already existed",
                        "Từ này đã tồn tại.\nBạn hãy thay thế hoặc bổ sung nghĩa vừa nhập cho từ này.");
                selectionAlert.getButtonTypes().clear();

                ButtonType replaceButton = new ButtonType("Replace(Thay thế)");
                ButtonType addButton = new ButtonType("Add(Bổ sung)");
                selectionAlert.getButtonTypes().addAll(replaceButton, addButton, ButtonType.CANCEL);

                Optional<ButtonType> selection = selectionAlert.showAndWait();

                if (selection.get() == replaceButton) {
                    dictionary.get(indexOfWord).setWordExplain(meaning);
                    dictionaryManagement.dictionaryExportToFile(dictionary, );
                }
            }
        }
    }
}

