package dk.easv.ticketapptest.GUI.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardController {

    public BorderPane rootPaneAdmin;

    public void handleUserManagement(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/user-management-view.fxml"));
        Parent adminDashboard = fxmlLoader.load();

        rootPaneAdmin.setCenter(adminDashboard);


    }

    public void handleEventHistory(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/admin-event-view.fxml"));
        Parent adminDashboard = fxmlLoader.load();

        rootPaneAdmin.setCenter(adminDashboard);
    }

    public void handleTicketManagement(ActionEvent actionEvent) {
    }

    public void handleEventManagement(ActionEvent actionEvent) {
    }
}
