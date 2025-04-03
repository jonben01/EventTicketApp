package dk.easv.ticketapptest;

//project imports
import dk.easv.ticketapptest.GUI.AlertClass;
//library imports
import dk.easv.ticketapptest.GUI.Controllers.LoginController;
import fr.brouillard.oss.cssfx.CSSFX;
//java imports -- i mean technically javafx should not be here but this is a javafx project
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            stage.setTitle("Ticket Management");
            stage.setResizable(false);
            LoginController loginController = fxmlLoader.getController();

            stage.setScene(scene);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/usermanagementstyle.css")).toExternalForm());
            stage.show();

            CSSFX.start();
        } catch (IOException e) {
            e.printStackTrace();
            AlertClass.alertError("program broke", "the program died");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}