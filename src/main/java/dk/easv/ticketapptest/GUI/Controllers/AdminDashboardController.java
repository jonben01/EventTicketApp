package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BLL.SessionManager;
import dk.easv.ticketapptest.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardController {
    @FXML public BorderPane rootPaneAdmin;
    @FXML public Button btnUserManagement;
    @FXML public Button btnEventHistory;


    public void handleUserManagement(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/user-management-view.fxml"));
        Parent adminDashboard = fxmlLoader.load();

        btnUserManagement.setStyle("-fx-background-color: #EFF6FF; -fx-text-fill: #1D4ED8;");
        btnEventHistory.setStyle("");

        rootPaneAdmin.setCenter(adminDashboard);

    }


    public void handleEventHistory(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/admin-event-view.fxml"));
        Parent adminDashboard = fxmlLoader.load();

        btnEventHistory.setStyle("-fx-background-color: #EFF6FF; -fx-text-fill: #1D4ED8;");
        btnUserManagement.setStyle("");

        rootPaneAdmin.setCenter(adminDashboard);
    }

    @FXML
    private void onLogOut(ActionEvent actionEvent) throws IOException {
        SessionManager.getInstance().logout();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1152, 768);
        ((Stage) rootPaneAdmin.getScene().getWindow()).setTitle("Login");
        ((Stage) rootPaneAdmin.getScene().getWindow()).setScene(scene);
    }
}
