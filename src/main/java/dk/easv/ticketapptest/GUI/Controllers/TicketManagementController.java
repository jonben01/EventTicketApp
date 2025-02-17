package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.GUI.TemporaryDataClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.List;

public class TicketManagementController {
    BorderPane mainPane;
    @FXML
    private GridPane gridPane;
    int currentX = 0;
    int currentY = 0;
    private TemporaryDataClass dataClass;
    private String eventCSS;
    private int h = 150;
    private int w = 300;



    @FXML
    private void initialize() {
        dataClass = new TemporaryDataClass();
        eventCSS = getClass().getResource("/css/eventmanagementstyle.css").toExternalForm();
        gridPane.setPadding(new Insets(70, 0, 0, 0));
        gridPane.setHgap(10);
        gridPane.setVgap(10);


        for (int i = 0; i < 3; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.NEVER);
            columnConstraints.setMinWidth(w);
            columnConstraints.setPrefWidth(w);
            columnConstraints.setMaxWidth(w);
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < 10; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.NEVER);
            rowConstraints.setMinHeight(h);
            rowConstraints.setPrefHeight(h);
            rowConstraints.setMaxHeight(h);
            gridPane.getRowConstraints().add(rowConstraints);

            gridPane.setAlignment(Pos.CENTER);
        }
        addExistingEvents(dataClass.getEvents());
        gridPane.getStylesheets().add(eventCSS);
        gridPane.setStyle("-fx-background-color: #F8F8F8;");
    }

    public void setPanel(BorderPane rootPaneEvent) {
        mainPane = rootPaneEvent;
    }

    private class EventDetails {
        String title;
        String location;
        String date;
        String time;
        String description;

        public EventDetails(String title, String location, String date, String time, String description) {
            this.title = title;
            this.location = location;
            this.date = date;
            this.time = time;
            this.description = description;
        }
    }

    private VBox createEventPanel(String title, String location, String date, String time, String description) {
        EventDetails eventDetails = new EventDetails(title, location, date, time, description);
        VBox vbox = new VBox();
        vbox.getStyleClass().add("vBoxBorder");

        // Width constraints
        vbox.setPrefWidth(300);
        vbox.setMaxWidth(300);

        // Height constraints
        vbox.setPrefHeight(300);
        vbox.setMaxHeight(300);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("h1");

        Label locationLabel = new Label("📍 " + location);
        locationLabel.getStyleClass().add("h2");

        Label dateLabel = new Label("📅 " + date);
        dateLabel.getStyleClass().add("h2");

        Label timeLabel = new Label("🕒 " + time);
        timeLabel.getStyleClass().add("h2");

        vbox.getChildren().addAll(titleLabel, locationLabel, dateLabel, timeLabel);

        vbox.setOnMouseClicked(event -> {
            if(event.getClickCount() == 1)
            {
                try{
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ticket-print-view.fxml"));
                    Parent eventInDepth = fxmlLoader.load();
                    TicketPrintController controller = fxmlLoader.getController();
                    controller.setPanel(mainPane);
                    controller.setEventDetails(eventDetails.title, eventDetails.location, eventDetails.date, eventDetails.time);
                    mainPane.setCenter(eventInDepth);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return vbox;
    }

    public void createEvent(String title, String location, String date, String starttime, String endtime, String description) {
        int x = getNextX();
        int y = getNextY();
        System.out.println("(" + x + "," + y + ")");

        gridPane.add(createEventPanel(title, location, date, starttime + " - " + endtime, description), x, y);

        currentX++;
    }

    private void addExistingEvents(List<Event2> events){
        if(!events.isEmpty()){
            for(Event2 event : events){
                createEvent(event.getTitle(), event.getLocation(), event.getDate(), event.getStartTime(), event.getEndTime(), event.getDescription());
            }
        }
    }

    private int getNextX() {
        return currentX % 3;
    }

    private int getNextY() {
        return currentX / 3;
    }
}
