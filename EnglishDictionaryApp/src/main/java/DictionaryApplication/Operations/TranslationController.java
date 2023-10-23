package DictionaryApplication.Operations;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class TranslationController implements Initializable {

    public Button switchToggle;
    private String translatingFromLanguage = "eng";
    private String translatingToLanguage = "vie";
    private boolean isToVietnamese = true;
    
    @FXML
    private TextArea translatingFromLanguageField, translatingToLanguageField;
    
    @FXML
    private Button translateButton;
    
    @FXML
    private Label englishLabel, vietnameseLabel;

    /**
     * Override initialize method.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        translateButton.setOnAction(actionEvent -> {
            try {
                handleClickingTranslateButton();
            } catch (IOException | URISyntaxException ioException) {
                ioException.printStackTrace();
            }
        });

        translatingFromLanguageField.setOnKeyTyped(keyEvent -> translateButton.setDisable(translatingFromLanguageField.getText().trim().isEmpty()));

        translateButton.setDisable(true);
        translatingFromLanguageField.setEditable(false);
    }

    /**
     * Handle clicking to switch two languages between translated-from and translated-to.
     */
    @FXML
    private void handleClickingSwitchToggle() {
        translatingFromLanguageField.clear();
        translatingToLanguageField.clear();
        if (isToVietnamese) {
            englishLabel.setLayoutX(426);
            vietnameseLabel.setLayoutX(104);
            translatingFromLanguage = "vi";
            translatingToLanguage = "en";
        } else {
            englishLabel.setLayoutX(104);
            vietnameseLabel.setLayoutX(426);
            translatingFromLanguage = "en";
            translatingToLanguage = "vi";
        }
        isToVietnamese = !isToVietnamese;
    }

    /**
     * Handle translating function when clicking.
     */
    @FXML
    private void handleClickingTranslateButton() throws IOException, URISyntaxException {
        HttpURLConnection httpURLConnection = getHttpURLConnection();
        BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String line;
        StringBuilder content = new StringBuilder();
        while ((line = inputStreamReader.readLine()) != null) {
            content.append(line);
        }

        inputStreamReader.close();
        httpURLConnection.disconnect();
        
        JsonObject data = new JsonObject();
        data.put(content.toString(), content.toString());
        try {
            data = (JsonObject) data.get(content.toString());
            JsonArray sentences = (JsonArray) data.get("sentences");
            JsonObject object = (JsonObject) sentences.get(0);
            String translation = (String) object.get("translation");
            translatingToLanguageField.setText(translation);
        } catch (Exception exception) {
            System.out.println("Error: Something wrong with translation happened!");
        }
    }

    /**
     * Get HttpURLConnection.
     */
    private HttpURLConnection getHttpURLConnection() throws URISyntaxException, IOException {
        String rootAPI = "https://clients5.google.com/translate_a/t?client=dict-chrome-ex&sl=" +
                translatingFromLanguage + "&tl=" + translatingToLanguage + "&dt=t&q=";
        String translatingFromText = translatingFromLanguageField.getText();
        String urlString = rootAPI + translatingFromText;
        urlString = urlString.replace(" ", "%20");

        URI url = new URI(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.toURL().openConnection();
        httpURLConnection.addRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
        httpURLConnection.setRequestMethod("GET");
        return httpURLConnection;
    }
}
