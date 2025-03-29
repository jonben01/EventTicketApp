package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.GUI.AlertClass;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private TableColumn<Event2, LocalDateTime> clnDateTime;
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

        Label loadingLabel = new Label("Loading Events...");
        loadingLabel.styleProperty().set("-fx-font-size: 20px;");
        tblEvents.setPlaceholder(loadingLabel);

        Task<AdminEventModel> initTask = new Task<>() {

            @Override
            protected AdminEventModel call() throws Exception {
                return new AdminEventModel();
            }
        };
        initTask.setOnSucceeded(e -> {
            this.adminEventModel = initTask.getValue();
            loadTableData();
            setupSearchDebounce();
        });
        initTask.setOnFailed(e -> {
            Throwable error = initTask.getException();
            AlertClass.alertError("Setup Error", "An error occurred while setup up events");
            error.printStackTrace();
        });
        Thread thread = new Thread(initTask);
        thread.setDaemon(true);
        thread.start();

        setupColumns();

    }
    public void loadTableData() {
        Task<ObservableList<Event2>> loadDataTask = new Task<>() {

            @Override
            protected ObservableList<Event2> call() throws Exception {
                return adminEventModel.getObservableEvents();
            }
        };
        loadDataTask.setOnSucceeded(event -> {
            tblEvents.setItems(loadDataTask.getValue());
        });
        loadDataTask.setOnFailed(event -> {
            Throwable error = loadDataTask.getException();
            AlertClass.alertError("Load Error","An error occurred while loading events");
            error.printStackTrace();
        });
        Thread thread = new Thread(loadDataTask);
        thread.setDaemon(true);
        thread.start();
    }

    public void setupColumns() {
        tblEvents.setFixedCellSize(40);

        clnEventName.setCellValueFactory(new PropertyValueFactory<>("title"));
        clnDateTime.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        clnDateTime.setCellFactory(cellData -> new TableCell<Event2, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    //format dates to FULL MONTH + DAY - HOURS + MINUTES
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d. - HH:mm");
                    setText(item.format(formatter));
                }
            }
        });
        clnLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        clnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        //creates delete buttons on tableview.
        clnActions.setCellValueFactory(param -> {
            Event2 event = param.getValue();
            Button deleteButton = new Button("Delete");

            deleteButton.setOnAction(e -> {
                //handle deletion in a different thread using Task
                Task<Void> deleteTask = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        //delete the event then exit
                        adminEventModel.deleteEvent(event);
                        return null;
                    }
                };
                //if it fails, inform the user
                deleteTask.setOnFailed(ev -> {
                    deleteTask.getException().printStackTrace();
                    AlertClass.alertError("Deletion error", "An error occurred while deleting event");
                });
                //if it succeeds manually refresh the list using blank search
                deleteTask.setOnSucceeded(ev -> {
                    searchEvent();
                });
                //run the task in a new thread
                new Thread(deleteTask).start();
            });
            //return the delete button
            return new SimpleObjectProperty<>(deleteButton);
        });

    }

    public void setupSearchDebounce() {
        searchDebounce = new PauseTransition(Duration.millis(200));
        searchDebounce.setOnFinished(event -> {
            searchEvent();
        });
        txtEventSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchDebounce.stop();
            searchDebounce.playFromStart();
        });
    }


    public void searchEvent() {
        String searchQuery = txtEventSearch.getText();
        if (txtEventSearch.getText().isEmpty()) {
            try {
                tblEvents.setItems(adminEventModel.getAllEvents());
                //TODO use custom exception
            } catch (Exception e) {
                AlertClass.alertError("Search Error", "An error occurred while searching for events" + e.getMessage());
            }
            return;
        }

        Task<ObservableList<Event2>> searchTask = getObservableListTask(searchQuery);

        Thread thread = new Thread(searchTask);
        thread.setDaemon(true);
        thread.start();
    }

    private Task<ObservableList<Event2>> getObservableListTask(String searchQuery) {
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

        searchTask.setOnFailed(event -> {
            Throwable error = searchTask.getException();
            AlertClass.alertError("Search Error", "An error occurred while searching for events" + error.getMessage());
        });
        return searchTask;
    }
}
