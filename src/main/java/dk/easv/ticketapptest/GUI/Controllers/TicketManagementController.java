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
import java.util.ArrayList;
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
    private AnchorPane windowPane;
    private List<VBox> vboxList;


    @FXML
    private void initialize() {
        vboxList = new ArrayList<>();
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

            gridPane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < 10; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.NEVER);
            rowConstraints.setMinHeight(h);
            rowConstraints.setPrefHeight(h);

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
        vboxList.add(vbox);
        trackWindowSize();
        vbox.getStyleClass().add("vBoxBorder");
        vbox.setCursor(javafx.scene.Cursor.HAND);

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
                    controller.setParent(this);
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
            for(VBox vBox : vboxList)
            {
                vBox.setPrefWidth(columnWidth * 0.9);
                vBox.setMinWidth(columnWidth * 0.9);
                vBox.setMaxWidth(columnWidth * 0.9);
                System.out.println(vBox.getPrefWidth() + " - " + columnWidth);
            }
        });

        windowPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            double height = newValue.doubleValue();
            int columns = gridPane.getColumnConstraints().size();
            double rowHeight = height / columns;
            for (RowConstraints row : gridPane.getRowConstraints()) {
                row.setMinHeight(rowHeight * 0.9);
                row.setPrefHeight(rowHeight * 0.9);
                row.setMaxHeight(rowHeight * 0.9);
            }
        });
    }
}
