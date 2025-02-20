package dk.easv.ticketapptest.GUI.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    public BorderPane rootPane;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;


    //todo temp login method. Unused in latest build.
    public void handleAdminDash(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/admin-dashboard-view.fxml"));
        Parent adminDashboard = fxmlLoader.load();

        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setScene(new Scene(adminDashboard));


    }

    //TODO LOGIN SHOULD JUST BE AN IF STATEMENT IF TEXTFIELD CONTAINS ADMIN/PASSWORD - ADMIN SECTION
    // IF TEXTFIELD CONTAINS EVENT/PASSWORD - EVENT SECTION


    //todo temp login method. unused in latest build.
    public void handleEventDash(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/event-dashboard-view.fxml"));
        Parent eventDashboard = fxmlLoader.load();

        Stage stage = (Stage) rootPane.getScene().getWindow();
        Scene scene = new Scene(eventDashboard);
        scene.getStylesheets().add(getClass().getResource("/styles/Base-stylesheet.css").toExternalForm());
        stage.setScene(scene);
    }


    //TODO handle exceptions better, make Alert class, change css in code to being in fxml instead
    public void handleLogin(ActionEvent actionEvent) throws IOException {
        if (Objects.equals(txtUsername.getText(), "admin") && Objects.equals(txtPassword.getText(), "password")) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/admin-dashboard-view.fxml"));
            Parent adminDashboard = fxmlLoader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(adminDashboard));
        } else if (Objects.equals(txtUsername.getText(), "event2") && Objects.equals(txtPassword.getText(), "password")) {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/event-dashboard-view.fxml"));
            Parent eventDashboard = fxmlLoader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(eventDashboard);
            scene.getStylesheets().add(getClass().getResource("/styles/Base-stylesheet.css").toExternalForm());
            stage.setScene(scene);

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password");
            alert.showAndWait();
        }
    }

}
