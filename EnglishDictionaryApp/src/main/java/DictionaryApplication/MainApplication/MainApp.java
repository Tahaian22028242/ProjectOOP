package DictionaryApplication.MainApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class MainApp extends Application {

    private double offsetX = 0;
    private double offsetY = 0;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Graphic management of the app.
     */
    @Override
    public void start(final Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                "/Views/DictionaryGui.fxml")));
        stage.setTitle("DictionaryApplication");
        stage.initStyle(StageStyle.TRANSPARENT);

        root.setOnMousePressed(mouseEvent -> {
            offsetX = mouseEvent.getSceneX();
            offsetY = mouseEvent.getSceneY();
        });

        root.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - offsetX);
            stage.setY(mouseEvent.getScreenY() - offsetY);
        });

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }
}
