package dk.easv.ticketapptest.GUI.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class EventEventManagementController {

    @FXML
    private Button btnCreateEvent;
    @FXML
    private GridPane gridPane;

    int currentX = 0;
    int currentY = 0;
    int ignore = 0;


    @FXML
    private void initialize() {
        String cssFile = getClass().getResource("/css/usermanagementstyle.css").toExternalForm();
        btnCreateEvent.getStylesheets().add(cssFile);
    }

    private VBox createEventPanel(String title, String location, String date, String time,
                                  String[] ticketTypes, String coordinator) {
        VBox vbox = new VBox();

        // Set a fixed height for the VBox to prevent overlapping
        vbox.setPrefHeight(150); // Set a preferred height
        vbox.setMaxHeight(150); // Set a maximum height
        vbox.setSpacing(10);
        vbox.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-color: lightgray; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Set GridPane properties
        gridPane.setHgap(10); // Horizontal gap between columns
        gridPane.setVgap(10); // Vertical gap between rows
        gridPane.setPadding(new Insets(10)); // Padding around the grid

        Label titleLabel = new Label(title);
        titleLabel.setFont(new Font("Arial", 18));

        Label locationLabel = new Label("📍 " + location);
        Label dateLabel = new Label("📅 " + date);
        Label timeLabel = new Label("🕒 " + time);

        Label ticketsLabel = new Label("Tickets Sold");
        StringBuilder ticketInfo = new StringBuilder();
        for (String ticket : ticketTypes) {
            ticketInfo.append(ticket).append("\n");
        }
        Label ticketInfoLabel = new Label(ticketInfo.toString().trim());

        Label coordinatorLabel = new Label("Coordinator: " + coordinator);

        Button deleteButton = new Button("🗑️");

        vbox.getChildren().addAll(titleLabel, locationLabel, dateLabel, timeLabel,
                ticketsLabel, ticketInfoLabel, coordinatorLabel, deleteButton);

        return vbox;
    }

    @FXML
    private void handleCreateEvent(ActionEvent actionEvent) {

        int x = getNextX();
        int y = getNextY();
        System.out.println("(" + x + "," + y + ")");
        gridPane.add(createEventPanel("Tech Conference 2025", "Convention Center",
                "August 20, 2025", "9:00 AM - 6:00 PM",
                new String[]{"Early Bird $299", "Regular $399"},
                "John Cooper"), x, y);
    }

    private int getNextX() {
        int x = currentX; // Store the current value of currentX
        if (currentX < 2) { // Allow for indices 0, 1, 2
            currentX++; // Increment currentX for the next call
        } else {
            currentX = 0; // Reset currentX for the next row
        }
        return x; // Return the stored value
    }

    private int getNextY() {
        if (currentX == 1) { // If we just reset currentX, we need to increment currentY
            if(ignore == 0)
            {
                ignore = 1;
                return currentY;
            }
            else {
                currentY++;
            }
        }
        return currentY; // Return the currentY value
    }
}

