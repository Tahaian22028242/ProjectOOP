package DictionaryApplication.Operations;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class DictionaryController implements Initializable {

    @FXML
    private Button searchWordButton, addButton, translateButton, closeButton;

    @FXML
    private AnchorPane container;

    @FXML
    private Tooltip tooltip1, tooltip2, tooltip3;

    /**
     * Set a children node of container.
     */
    public void setNode(Node node) {
        container.getChildren().clear();
        container.getChildren().add(node);
    }

    /**
     * Show component.
     */
    @FXML
    private void showComponent(String path) {
        try {
            AnchorPane component = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
            setNode(component);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Override initialize method.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchWordButton.setOnAction(actionEvent -> showComponent("/Views/SearchGui.fxml"));
        addButton.setOnAction(actionEvent -> showComponent("/Views/AdditionGui.fxml"));
        translateButton.setOnAction(actionEvent -> showComponent("/Views/TranslationGui.fxml"));

        tooltip1.setShowDelay(Duration.seconds(0.5));
        tooltip2.setShowDelay(Duration.seconds(0.5));
        tooltip3.setShowDelay(Duration.seconds(0.5));

        showComponent("/Views/SearcherGui.fxml");

        closeButton.setOnMouseClicked(e -> System.exit(0));
    }
}
