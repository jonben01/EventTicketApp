package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BLL.SessionManager;
import dk.easv.ticketapptest.GUI.AlertClass;
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


    public void handleUserManagement(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/user-management-view.fxml"));
            Parent adminDashboard = fxmlLoader.load();
            btnUserManagement.setStyle("-fx-background-color: #EFF6FF; -fx-text-fill: #1D4ED8;");
            btnEventHistory.setStyle("");
            rootPaneAdmin.setCenter(adminDashboard);
        } catch (IOException e) {
            e.printStackTrace();
            AlertClass.alertError("Failed to load", "An error occurred while loading the user management window");
        }

    }


    public void handleEventHistory(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/admin-event-view.fxml"));
            Parent adminDashboard = fxmlLoader.load();
            btnEventHistory.setStyle("-fx-background-color: #EFF6FF; -fx-text-fill: #1D4ED8;");
            btnUserManagement.setStyle("");
            rootPaneAdmin.setCenter(adminDashboard);
        } catch (IOException e) {
            e.printStackTrace();
            AlertClass.alertError("Failed to load", "An error occurred while opening the event window");
        }
    }

    @FXML
    private void onLogOut(ActionEvent actionEvent) {
        try {
            SessionManager.getInstance().logout();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            Stage stage = (Stage) rootPaneAdmin.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            AlertClass.alertError("Failed to load", "An error occurred while logging out");
        }
    }
}
