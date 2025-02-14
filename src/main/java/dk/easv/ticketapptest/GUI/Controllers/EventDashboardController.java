package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

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
        topbarHBox.getStyleClass().add("topbar");
    }

    @FXML
    public void handleEventManagement(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/event-Dashboard-event-management.fxml"));
        Parent eventDashboard = fxmlLoader.load();

        rootPaneEvent.setCenter(eventDashboard);

    }
    }


