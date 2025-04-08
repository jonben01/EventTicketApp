package dk.easv.ticketapptest.GUI.Controllers;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Location;
import dk.easv.ticketapptest.BE.Ticket;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
import dk.easv.ticketapptest.BLL.SessionManager;
import dk.easv.ticketapptest.GUI.AlertClass;
import dk.easv.ticketapptest.GUI.Models.EventManagementModel;
import dk.easv.ticketapptest.GUI.Models.TicketModel;
import dk.easv.ticketapptest.GUI.Models.UserModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class EventViewController {

    BorderPane root;
    @FXML
    private Label lblTitle;
    @FXML
    private Label lblLocation;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblTime;
    @FXML
    private Label lblDesc;
    @FXML
    private Button btnEditEvent;
    @FXML
    private TextArea txtDesc;
    @FXML
    private Label lblTicket;
    @FXML
    private Button btnAddTicket;
    @FXML
    public Button btnEditTicket;
    @FXML
    public Button btnDeleteTicket;
    @FXML
    private TableView<Ticket> tblTicket;
    @FXML
    private Label lblCoords;
    @FXML
    private ListView lstCoords;
    @FXML
    private Button btnAddCoord;
    @FXML
    private Button btnReturn;
    @FXML
    private VBox vboxLeft;
    private TicketModel ticketModel;
    private EventManagementModel eventModel;
    @FXML
    private VBox vboxRight;

    private HashMap<User, Boolean> selectionState = new HashMap<>();
    @FXML
    private TableColumn<Ticket, String> clnTicket;

    @FXML
    private TableColumn<Ticket, String> clnDescription;

    @FXML
    private TableColumn<Ticket, Double> clnPrice;

    private Event2 selectedEvent;
    private UserModel userModel;
    private List<User> usersWithAccess;
    private boolean userHasAccess = false;
    private boolean taskRunning = false;
    @FXML
    private Button btnRemoveCoord;
    @FXML
    private Label lblAccess;

    private String previousView;

    public void setSelectedEvent(Event2 event2) {
        this.selectedEvent = event2;

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                List<User> coordinators = userModel.getAllCoordinators();
                List<User> usersWithAccess = eventModel.getAllUsersForEvent(selectedEvent.getEventID());


                boolean userHasAccess = false;
                for (User coordinator : usersWithAccess) {
                    if (SessionManager.getInstance().getCurrentUser().getId() == coordinator.getId()) {
                        userHasAccess = true;
                    }
                }


                boolean finalUserHasAccess = userHasAccess;
                Platform.runLater(() -> {
                    for (User coordinator : coordinators) {
                        lstCoords.getItems().add(coordinator);
                    }


                    EventViewController.this.usersWithAccess = usersWithAccess;


                    EventViewController.this.userHasAccess = finalUserHasAccess;


                    magicLinesOfCode();
                    lstCoords.refresh();
                    checkUserAccess();
                    updateInformation(1);
                    updateTicketList();
                });

                return null;
            }

            @Override
            protected void failed() {
                super.failed();
                AlertClass.alertError("Error", "An error occurred while loading the event data.");
            }
        };

        new Thread(task).start();
    }


    private void checkUserAccess() {
        if(!userHasAccess){
            btnEditEvent.setDisable(true);
            btnAddCoord.setDisable(true);
            btnRemoveCoord.setDisable(true);
            btnAddTicket.setDisable(true);
            btnEditTicket.setDisable(true);
            btnDeleteTicket.setDisable(true);
            lblAccess.setText("You do not have access to modify this event.");
            lblAccess.getStylesheets().add("hAccess");

        }
    }

    public void setPanel(BorderPane root)
    {
        this.root = root;
    }

    public void setPreviousView(String previousView)
    {
        this.previousView = previousView;
    }

        @FXML
        public void initialize() {
            Label loadingLabel = new Label("Loading Coordinators");
            loadingLabel.styleProperty().set("-fx-font-size: 20px;");
            lstCoords.setPlaceholder(loadingLabel);
        try {
            usersWithAccess = new ArrayList<>();
            userModel = new UserModel();
            eventModel = new EventManagementModel();
            ticketModel = new TicketModel();
            tblTicket.getStylesheets().add("css/eventmanagementstyle.css");
            tblTicket.getStyleClass().add("table-view");

            //TODO: FIND EN BEDRE MÅDE AT GØRE DET HER PÅ.
            lblTitle.getStyleClass().add("h1");
            lblLocation.getStyleClass().add("h2");
            lblDate.getStyleClass().add("h2");
            lblTime.getStyleClass().add("h2");
            lblDesc.getStyleClass().add("h3");
            lblTicket.getStyleClass().add("h2");
            lblCoords.getStyleClass().add("h2");
            btnEditEvent.getStyleClass().add("button2");
            btnAddTicket.getStyleClass().add("button2");
            btnAddCoord.getStyleClass().add("button2");
            btnReturn.getStyleClass().add("returnButton");
            vboxLeft.getStyleClass().add("vBoxBorder2");
            vboxRight.getStyleClass().add("vBoxBorder2");

            populateList();
            clnTicket.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue()).getTicketName()));
            clnDescription.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue()).getDescription()));
            clnPrice.setCellValueFactory(cellData -> new SimpleObjectProperty<>((cellData.getValue()).getPrice()));
            clnPrice.setCellFactory(tc -> new TableCell<Ticket, Double>() {
                @Override
                protected void updateItem(Double price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty || price == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.2f", price));
                    }
                }
            });
        } catch (EasvTicketException e) {
            e.printStackTrace();
            AlertClass.alertError("Something went wrong", "An error has occurred while initializing the event window");
        }
    }


    private void populateList(){
        lstCoords.setCellFactory(param -> new ListCell<User>() {
            private Label nameLabel = new Label();
            private Label emailLabel = new Label();
            private Label phoneLabel = new Label();
            private Label checkMarkLabel = new Label();

            {
                nameLabel.getStyleClass().add("h5");
                emailLabel.getStyleClass().add("h2");
                phoneLabel.getStyleClass().add("h3");

                VBox vBox = new VBox(5);
                HBox hBox = new HBox(5);
                hBox.getChildren().addAll(nameLabel, checkMarkLabel);
                vBox.getChildren().addAll(hBox, emailLabel, phoneLabel);
                setGraphic(vBox);
            }

            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    nameLabel.setText(null);
                    emailLabel.setText(null);
                    phoneLabel.setText(null);
                    checkMarkLabel.setText(null);
                } else {
                    nameLabel.setText(item.getFirstName() + " " + item.getLastName());
                    emailLabel.setText(item.getEmail());
                    phoneLabel.setText(item.getPhone());
                    if(userHasAssignedEvent(item))
                    {
                        checkMarkLabel.setText("✓");
                    }
                    else
                    {
                        checkMarkLabel.setText("");
                    }
                }
            }
        });
    }

    private Boolean userHasAssignedEvent(User user){
        for(User allUser : usersWithAccess)
        {
        if(user.getId() == allUser.getId())
        {
            return true;
        }
        }
        return false;
    }

    @FXML
    private void handleAddTicket(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/create-ticket-view.fxml"));
            Parent root = loader.load();
            CreateTicketViewController controller = loader.getController();
            controller.setParent(this);
            controller.setSelectedEvent(selectedEvent);
            Stage stage = new Stage();
            String imgPath = Objects.requireNonNull(getClass().getResource("/BASW_logo2.png")).toExternalForm();
            stage.getIcons().add(new Image(imgPath));
            stage.setTitle("Create Ticket");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/Base-stylesheet.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertClass.alertError("Error", "An error occurred while opening the ticket window.");
        }
    }

    public void updateTicketList() {
        if(taskRunning) {
            return;
        }

        taskRunning = true;

        tblTicket.getItems().clear();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                List<Ticket> tickets = ticketModel.getTicketsForEvent(selectedEvent);


                Platform.runLater(() -> {
                    tblTicket.getItems().clear();
                    tblTicket.getItems().addAll(tickets);
                });

                return null;
            }

            @Override
            protected void failed() {
                super.failed();
                taskRunning = false;
                AlertClass.alertError("Error", "Error while updating the ticket list.");
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                taskRunning = false;
            }
        };


        new Thread(task).start();
    }



    public void updateInformation(int version) {
        if(version == 1) {
            try {
                eventModel.updateList();
                List<Event2> events = new ArrayList<>(eventModel.getObservableEvents());

                for (Event2 event : events) {
                    if (selectedEvent.getEventID() == event.getEventID()) {
                        System.out.println("event found");
                        selectedEvent = event;
                    }
                }
            } catch (EasvTicketException e) {
                e.printStackTrace();
                AlertClass.alertError("Error", "Error while updating information");
            }
        }

        lblTitle.setText(selectedEvent.getTitle());
        lblLocation.setText(selectedEvent.getLocation().getAddress());
        if(Objects.equals(selectedEvent.getStartDate(), selectedEvent.getEndDate()))
        {
            lblDate.setText(selectedEvent.getStartDate().toString());
        }
        else {
            lblDate.setText(selectedEvent.getStartDate() + " to " + selectedEvent.getEndDate());
        }
        lblTime.setText(selectedEvent.getStartTime().toString() + " - " + selectedEvent.getEndTime().toString());
        txtDesc.setText(selectedEvent.getDescription());
    }

    @FXML
    private void onEditEvent(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/create-event-view.fxml"));
            Parent root = loader.load();
            CreateEventViewController controller = loader.getController();
            controller.setEventViewController(this);
            controller.selectEvent(selectedEvent);
            Stage stage = new Stage();
            String imgPath = Objects.requireNonNull(getClass().getResource("/BASW_logo2.png")).toExternalForm();
            stage.getIcons().add(new Image(imgPath));
            stage.setTitle("Update Event");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/Base-stylesheet.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertClass.alertError("Error", "An error occurred while loading event panel");
        }
    }

    @FXML
    private void handleRemoveEvent(ActionEvent actionEvent) {
        try {
            User temp = (User) lstCoords.getSelectionModel().getSelectedItem();
            if (temp != null) {
                usersWithAccess.removeIf(user -> user.getId() == temp.getId());

                Event2 tempEvent = new Event2();
                tempEvent.setEventID(selectedEvent.getEventID());
                tempEvent.addCoordinator(temp);
                eventModel.removeFromEventUsers(tempEvent);
                lstCoords.refresh();
                magicLinesOfCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertClass.alertError("Error", "An error occurred while removing event");
        }
    }

    private void magicLinesOfCode(){
        List<User> sortedUsers = sortUsers(new ArrayList<>(lstCoords.getItems()));
        lstCoords.getItems().clear();
        lstCoords.getItems().addAll(sortedUsers);
    }

    @FXML
    private void handleAddEvent(ActionEvent actionEvent){
        User temp = (User) lstCoords.getSelectionModel().getSelectedItem();
        System.out.println(temp.getFirstName() + " " + temp.getLastName());
        if (temp != null) {
            try {
                usersWithAccess.add(temp);
                Event2 tempEvent = new Event2();
                tempEvent.setEventID(selectedEvent.getEventID());
                tempEvent.addCoordinator(temp);
                eventModel.addToEventUsers(tempEvent);
                lstCoords.refresh();
                magicLinesOfCode();
            } catch (EasvTicketException e) {
                e.printStackTrace();
                AlertClass.alertError("Error", "An error occurred while adding event");
            }
        }
    }

    public void handleEditTicket(ActionEvent actionEvent) {
        Ticket selectedTicket = tblTicket.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/create-ticket-view.fxml"));
                Parent root = loader.load();
                CreateTicketViewController controller = loader.getController();
                controller.setParent(this);
                controller.setSelectedTicket(selectedTicket);
                controller.setSelectedEvent(selectedEvent);
                Stage stage = new Stage();
                String imgPath = Objects.requireNonNull(getClass().getResource("/BASW_logo2.png")).toExternalForm();
                stage.getIcons().add(new Image(imgPath));
                stage.setTitle("Edit Ticket");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                updateTicketList();
            } catch (IOException e) {
                e.printStackTrace();
                AlertClass.alertError("Error", "An error while editing the ticket");
            }
        } else {
            AlertClass.alertWarning("Missing ticket", "Please select a ticket to edit");
        }
    }

    public void handleDeleteTicket(ActionEvent actionEvent) {
        Ticket selectedTicket = tblTicket.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Ticket");
            alert.setHeaderText("Are you sure you want to delete this ticket?");
            alert.setContentText("Ticket: " + selectedTicket.getTicketName());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    ticketModel.deleteTicket(selectedTicket);
                    updateTicketList();
                } catch (EasvTicketException e) {
                    e.printStackTrace();
                    AlertClass.alertError("Error", "An error occurred while deleting the ticket");
                }
            }
        } else {
            AlertClass.alertWarning("Missing ticket", "Please select a ticket to delete");
        }
    }
    private List<User> sortUsers(List<User> users) {
        List<User> hasAccessList = new ArrayList<>();
        List<User> noAccessList = new ArrayList<>();

        for(User user : users) {
            Boolean check = false;
            for(User userWithAccess : usersWithAccess) {
                if(userWithAccess.getId() == user.getId()) {
                    check = true;
                }
            }
            if(check)
            {
                hasAccessList.add(user);
            }
            else
            {
                noAccessList.add(user);
            }
        }

        hasAccessList.addAll(noAccessList);
        return hasAccessList;
    }

    @FXML
    private void handleReturn(MouseEvent mouseEvent) {
        try{
            FXMLLoader loader;
            Parent view;

            if ("event-Dashboard-event-management".equals(previousView)){
                loader = new FXMLLoader(getClass().getResource("/views/event-Dashboard-event-management.fxml"));
            } else if ("admin-event-view".equals(previousView)) {
                loader = new FXMLLoader(getClass().getResource("/views/admin-event-view.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/views/event-Dashboard-event-management.fxml"));
            }
            view = loader.load();
            if("admin-event-view".equals(previousView)){
                AdminEventController controller = loader.getController();
                controller.setRootPane(root);
            }
            else if("event-Dashboard-event-management".equals(previousView))
            {
                EventEventManagementController controller = loader.getController();
                controller.setPanel(root);
            }
            root.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            AlertClass.alertError("Error","Could not return to previous view");

        }

    }
}

