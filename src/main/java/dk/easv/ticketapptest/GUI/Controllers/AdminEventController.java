package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.EventStatus;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
import dk.easv.ticketapptest.GUI.AlertClass;
import dk.easv.ticketapptest.GUI.Models.AdminEventModel;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminEventController implements Initializable {

    @FXML
    public TableView<Event2> tblEvents;
    @FXML
    public TextField txtEventSearch;
    @FXML
    public Label lblEntries;
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
    @FXML
    private BorderPane rootPane;

    private AdminEventModel adminEventModel;
    private PauseTransition searchDebounce;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Label loadingLabel = new Label("Loading Events...");
        loadingLabel.styleProperty().set("-fx-font-size: 20px;");
        tblEvents.setPlaceholder(loadingLabel);


        //running this in a different thread allows the user to see the window before data has loaded
        //providing a much nicer UX
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
        setupDoubleClick();
    }

    public void setRootPane(BorderPane rootPane) {this.rootPane = rootPane;}

    /**
     * gets all events to show be shown on the table
     * runs in new thread
     */
    public void loadTableData() {
        Task<ObservableList<Event2>> loadDataTask = new Task<>() {

            @Override
            protected ObservableList<Event2> call() throws Exception {
                return adminEventModel.getObservableEvents();
            }
        };
        loadDataTask.setOnSucceeded(event -> {
            tblEvents.setItems(loadDataTask.getValue());
            lblEntries.setText("Showing: " + tblEvents.getItems().size() + " Events");
        });
        loadDataTask.setOnFailed(event -> {
            Throwable error = loadDataTask.getException();
            error.printStackTrace();
            AlertClass.alertError("Load Error","An error occurred while loading events");
        });
        Thread thread = new Thread(loadDataTask);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * cell factory and cell value factory method, fills the columns with the correct information
     */
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
        clnStatus.setCellValueFactory(new PropertyValueFactory<>("eventStatus"));

        tblEvents.setRowFactory(row -> new TableRow<Event2>() {
            @Override
            protected void updateItem(Event2 item, boolean empty) {
                super.updateItem(item, empty);

                getStyleClass().removeAll("ongoing_row", "completed_row");

                if (empty || item == null) {
                    setStyle("");
                } else {
                    //switch to determine what style class to use.
                    switch (item.getEventStatus()) {
                        case ONGOING -> getStyleClass().add("ongoing_row");
                        case COMPLETED -> getStyleClass().add("completed_row");
                        case UPCOMING -> getStyleClass().add("");
                        default -> setStyle("");
                    }
                }
            }
        });

        //creates delete buttons on tableview.
        clnActions.setCellValueFactory(param -> {
            Event2 event = param.getValue();
            Button deleteButton = new Button("Delete");

            deleteButton.setOnAction(e -> {

                if (event.getEventStatus() == EventStatus.ONGOING || event.getEventStatus() == EventStatus.UPCOMING) {
                    Optional<ButtonType> conf = AlertClass.alertConfirmation
                            ("Delete Event", "Are you sure you want to delete: " + event.getTitle() + "?");
                    if (conf.isPresent() && conf.get() != ButtonType.OK) {
                        return;
                    }
                }

                //handle deletion in a different thread using Task
                Task<Void> deleteTask = getDeleteTask(event);
                //run the task in a new thread
                new Thread(deleteTask).start();
            });
            //return the delete button
            return new SimpleObjectProperty<>(deleteButton);
        });

    }

    /**
     * Used to delete in a different thread to free up UI
     * @param event to be deleted
     * @return a task to be run in another thread
     */
    private Task<Void> getDeleteTask(Event2 event) {
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
        return deleteTask;
    }

    /**
     * sets up a search debounce to prevent the user from creating a new thread every key press.
     */
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

    /**
     * runs a search task in a separate thread to free up gui
     * also resets table items if the user deletes their input
     */
    public void searchEvent() {
        String searchQuery = txtEventSearch.getText();

        Task<ObservableList<Event2>> searchTask = getObservableListTask(searchQuery);

        Thread thread = new Thread(searchTask);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * calls the search method with a search query
     * @param searchQuery user input in text field
     * @return task to be run
     */
    private Task<ObservableList<Event2>> getObservableListTask(String searchQuery) {
        Task<ObservableList<Event2>> searchTask = new Task<>() {
            @Override
            protected ObservableList<Event2> call() throws Exception {
                return adminEventModel.searchEvent(searchQuery);
            }
        };

        //TODO should probably make something to counteract out of order completions
        searchTask.setOnSucceeded(e -> {
            ObservableList<Event2> filteredList = searchTask.getValue();
            if (filteredList != null) {
                tblEvents.setItems(filteredList);
                lblEntries.setText("Showing: " + tblEvents.getItems().size() + " Events");

            } else {
                tblEvents.setItems(FXCollections.observableArrayList());
                lblEntries.setText("Showing: " + tblEvents.getItems().size() + " Events");
            }
        });

        searchTask.setOnFailed(event -> {
            Throwable error = searchTask.getException();
            error.printStackTrace();
            AlertClass.alertError("Search Error", "An error occurred while searching for events" + error.getMessage());
        });
        return searchTask;
    }

    private void setupDoubleClick(){
        tblEvents.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                handleOpenEvent();
            }
        });
    }


    @FXML
    private void handleOpenEvent() {
        Event2 selectedEvent = tblEvents.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/event-view.fxml"));
                Parent eventView = loader.load();
                EventViewController controller = loader.getController();
                controller.setSelectedEvent(selectedEvent);
                controller.setPanel(rootPane);
                controller.setPreviousView("admin-event-view");

                // Set the loaded view in the center of the rootPane
                rootPane.setCenter(eventView);

            } catch (IOException e) {
                e.printStackTrace();
                AlertClass.alertError("Error", "Could not open event details");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
