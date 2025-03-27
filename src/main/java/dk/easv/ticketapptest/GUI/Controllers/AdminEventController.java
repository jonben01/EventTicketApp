package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.GUI.Models.AdminEventModel;
import dk.easv.ticketapptest.GUI.TemporaryDataClass;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminEventController implements Initializable {

    @FXML
    public TableView<Event2> tblEvents;
    @FXML
    public TextField txtEventSearch;
    @FXML
    private TableColumn<Event2, String> clnEventName;
    @FXML
    private TableColumn<Event2, String> clnDateTime;
    @FXML
    private TableColumn<Event2, String> clnLocation;
    @FXML
    private TableColumn<Event2, Void> clnStatus;
    @FXML
    private TableColumn<Event2, Button> clnActions;

    private AdminEventModel adminEventModel;
    private PauseTransition searchDebounce;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //TODO FIX THIS Garbo code
        try {
            adminEventModel = new AdminEventModel();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }

        searchDebounce = new PauseTransition(Duration.millis(200));
        searchDebounce.setOnFinished(event -> {
            searchEvent();
        });

        txtEventSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchDebounce.stop();
            searchDebounce.playFromStart();
        });

        tblEvents.setFixedCellSize(40);

        clnEventName.setCellValueFactory(new PropertyValueFactory<>("title"));

        clnDateTime.setCellValueFactory(cellData -> {
           Event2 event = cellData.getValue();
           if (event.getStartDate() != null && event.getStartTime() != null) {
               String dateTime = event.getStartDate().toString() + " " + event.getStartTime().toString();
               return new SimpleObjectProperty<>(dateTime);
           }
           return new SimpleObjectProperty<>("N/A");
        });

        clnLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        clnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        clnActions.setCellValueFactory(param -> {
            Event2 event = param.getValue();

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e -> {
                //TODO implement this better AND DONT USE RUNTIME E
                //TODO IMPLEMENT ALERT HERE, IF STATUS IS ACTIVE
                try {
                    adminEventModel.deleteEvent(event);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
            return new SimpleObjectProperty<>(deleteButton);
        });

        //TODO FIX THIS - figure out of try/catch should be here, maybe it should be handled better
        try {
            tblEvents.setItems(adminEventModel.getObservableEvents());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void searchEvent() {
        String searchQuery = txtEventSearch.getText();
        if (txtEventSearch.getText().isEmpty()) {
            try {
                tblEvents.setItems(adminEventModel.getAllEvents());
                //TODO implement the custom alert class instead.
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            return;
        }

        Task<ObservableList<Event2>> searchTask = new Task<>() {
            @Override
            protected ObservableList<Event2> call() throws Exception {
                return adminEventModel.searchEvent(searchQuery);
            }
        };

        searchTask.setOnSucceeded(e -> {
            ObservableList<Event2> filteredList = searchTask.getValue();
            if (filteredList != null) {
                tblEvents.setItems(filteredList);
            } else {
                tblEvents.setItems(FXCollections.observableArrayList());
            }
        });

        //TODO change this
        searchTask.setOnFailed(event -> {
            Throwable error = searchTask.getException();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText(error.getMessage());
            alert.showAndWait();
        });

        Thread thread = new Thread(searchTask);
        thread.setDaemon(true);
        thread.start();
    }
}
