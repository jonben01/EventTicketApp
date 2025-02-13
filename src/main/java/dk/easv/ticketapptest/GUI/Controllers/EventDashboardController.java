package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EventDashboardController {
    @FXML
    private VBox sidebarVBox;
    @FXML
    private HBox topbarHBox;

    @FXML
    public void initialize() {
        sidebarVBox.getStyleClass().add("sidebar");
        topbarHBox.getStyleClass().add("topbar");
    }
}
