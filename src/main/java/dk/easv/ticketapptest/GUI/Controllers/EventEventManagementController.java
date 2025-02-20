package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.GUI.Main;
import dk.easv.ticketapptest.GUI.TemporaryDataClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventEventManagementController {
    String userCSS;
    String eventCSS;
    BorderPane mainPane;
    @FXML
    private Button btnCreateEvent;
    @FXML
    private GridPane gridPane;
    private TemporaryDataClass dataClass;

    int currentX = 0;
    int currentY = 0;
    @FXML
    private AnchorPane windowPane;
    private List<VBox> vBoxList = new ArrayList<>();


    @FXML
    private void initialize() {
            dataClass = new TemporaryDataClass();
            userCSS = getClass().getResource("/css/usermanagementstyle.css").toExternalForm();
            eventCSS = getClass().getResource("/css/eventmanagementstyle.css").toExternalForm();
            btnCreateEvent.getStylesheets().add(userCSS);
            gridPane.getStylesheets().add(eventCSS);
        gridPane.setStyle("-fx-background-color: #F8F8F8;");


            gridPane.setPadding(new Insets(70, 0, 0, 0));
            gridPane.setHgap(10);
            gridPane.setVgap(10);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.NEVER);
            columnConstraints.setMinWidth(300);
            columnConstraints.setPrefWidth(300);
            //columnConstraints.setMaxWidth(300);
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < 10; i++) { // Set the number of rows you expect
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.NEVER);
            rowConstraints.setMinHeight(300);
            rowConstraints.setPrefHeight(300);
            //rowConstraints.setMaxHeight(300);
            gridPane.getRowConstraints().add(rowConstraints);

            gridPane.setAlignment(Pos.CENTER);
            gridPane.getStylesheets().add(eventCSS);
        }
        addExistingEvents(dataClass.getEvents());
        trackWindowSize();
    }

    public void setPanel(BorderPane mainPane)
    {
        this.mainPane = mainPane;
    }

    private VBox createEventPanel(String title, String location, String date, String time,
                                  String[] ticketTypes, List<User> coordinator) {

        VBox vbox = new VBox();
        vBoxList.add(vbox);
        vbox.getStyleClass().add("vBoxBorder");
            // Width constraints
            vbox.setPrefWidth(300);
            vbox.setMaxWidth(300);

            // Height constraints
            vbox.setPrefHeight(300);
            vbox.setMaxHeight(300);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("h1");

        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().add("button2");

        deleteButton.setOnAction(event -> {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Delete the " + title + " event?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
            }
        });


        Label locationLabel = new Label("📍 " + location);
        locationLabel.getStyleClass().add("h2");
        Label dateLabel = new Label("📅 " + date);
        dateLabel.getStyleClass().add("h2");
        Label timeLabel = new Label("🕒 " + time);
        timeLabel.getStyleClass().add("h2");
        Separator separator1 = new Separator();

        Label ticketsLabel = new Label("Tickets Sold");
        ticketsLabel.getStyleClass().add("h2");
        StringBuilder ticketInfo = new StringBuilder();
        for (String ticket : ticketTypes) {
            ticketInfo.append(ticket).append("\n");
        }
        Label ticketInfoLabel = new Label(ticketInfo.toString().trim());
        ticketInfoLabel.getStyleClass().add("h3");
        Separator separator2 = new Separator();

        Label coordinatorLabel = new Label("Coordinator: " + coordinator.get(1).getFirstName() + " " + coordinator.get(1).getLastName());
        coordinatorLabel.getStyleClass().add("h4");

        vbox.getChildren().addAll(titleLabel, locationLabel, dateLabel, timeLabel, separator1,
                ticketsLabel, ticketInfoLabel, separator2, coordinatorLabel, deleteButton);

        vbox.setOnMouseClicked(event -> {
            if(event.getClickCount() == 1)
            {
                try{
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/event-view.fxml"));
                    Parent eventInDepth = fxmlLoader.load();
                    EventViewController controller = fxmlLoader.getController();
                    controller.setPanel(mainPane);
                    mainPane.setCenter(eventInDepth);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return vbox;
    }

    @FXML
    private void handleCreateEvent(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/create-event-view.fxml"));
            Parent root = loader.load();
            CreateEventViewController controller = loader.getController();
            controller.setParent(this);
            Stage stage = new Stage();
            stage.setTitle("Create Event");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/Base-stylesheet.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading create event view: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void createEvent(String title, String location, String date, String starttime, String endtime,
                            String[] ticketTypes, List<User> coordinator) {
        int x = getNextX();
        int y = getNextY();
        System.out.println("(" + x + "," + y + ")");

        gridPane.add(createEventPanel(title, location, date, starttime + " - " + endtime, ticketTypes, coordinator), x, y);

        currentX++;
    }

    private void addExistingEvents(List<Event2> events){
        if(!events.isEmpty()){
            for(Event2 event : events){
                createEvent(event.getTitle(), event.getLocation(), event.getDate(), event.getStartTime(), event.getEndTime(), event.getTicketTypes(), event.getEventCoordinators());
            }
        }
    }

    private int getNextX() {
        return currentX % 3;
    }

    private int getNextY() {
        return currentX / 3;
    }

    private void trackWindowSize() {
        windowPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            double width = newValue.doubleValue();
            int columns = gridPane.getColumnConstraints().size();
            double columnWidth = width / columns;
            for (ColumnConstraints column : gridPane.getColumnConstraints()) {
                column.setMinWidth(columnWidth);
                column.setPrefWidth(columnWidth);
                column.setMaxWidth(columnWidth);
            }
        for(VBox vBox : vBoxList)
        {
            vBox.setPrefWidth(columnWidth * 0.9);
            vBox.setMinWidth(columnWidth * 0.9);
            vBox.setMaxWidth(columnWidth * 0.9);
            System.out.println(vBox.getPrefWidth() + " - " + columnWidth);
        }
        });
    }
}

