package dk.easv.ticketapptest.GUI.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public BorderPane rootPane;

    public void handleAdminDash(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/admin-dashboard-view.fxml"));
        Parent adminDashboard = fxmlLoader.load();

        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setScene(new Scene(adminDashboard));


    }

    public void handleEventDash(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/event-dashboard-view.fxml"));
        Parent eventDashboard = fxmlLoader.load();

        Stage stage = (Stage) rootPane.getScene().getWindow();
        Scene scene = new Scene(eventDashboard);
        scene.getStylesheets().add(getClass().getResource("/styles/Base-stylesheet.css").toExternalForm());
        stage.setScene(scene);
    }
}
