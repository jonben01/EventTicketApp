package dk.easv.ticketapptest.GUI;

import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1152, 768);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/usermanagementstyle.css")).toExternalForm());
        stage.show();

        CSSFX.start();

    }

    public static void main(String[] args) {
        launch();
    }
}