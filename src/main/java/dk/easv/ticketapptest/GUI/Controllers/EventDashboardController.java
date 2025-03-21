package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BLL.SessionManager;
import dk.easv.ticketapptest.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class EventDashboardController {
    @FXML
    private VBox sidebarVBox;
    @FXML
    private HBox topbarHBox;
    @FXML
    public BorderPane rootPaneEvent;

    @FXML
    public void initialize() {
        sidebarVBox.getStyleClass().add("sidebar");
    }

    @FXML
    public void handleEventManagement(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/event-Dashboard-event-management.fxml"));
        Parent eventDashboard = fxmlLoader.load();
        EventEventManagementController controller = fxmlLoader.getController();
        controller.setPanel(rootPaneEvent);
        rootPaneEvent.setCenter(eventDashboard);
    }

    @FXML
    private void handleTicketManagement(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ticket-management-view.fxml"));
        Parent eventDashboard = fxmlLoader.load();
        TicketManagementController controller = fxmlLoader.getController();
        controller.setPanel(rootPaneEvent);
        rootPaneEvent.setCenter(eventDashboard);
    }

    @FXML
    private void onLogOut(ActionEvent actionEvent) throws IOException {
        SessionManager.getInstance().logout();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1152, 768);
        ((Stage) rootPaneEvent.getScene().getWindow()).setTitle("Login");
        ((Stage) rootPaneEvent.getScene().getWindow()).setScene(scene);
    }
}


