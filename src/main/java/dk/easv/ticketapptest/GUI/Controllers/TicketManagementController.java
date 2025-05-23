package dk.easv.ticketapptest.GUI.Controllers;
//project imports
import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
import dk.easv.ticketapptest.BLL.SessionManager;
import dk.easv.ticketapptest.GUI.AlertClass;
import dk.easv.ticketapptest.GUI.Models.EventManagementModel;
//java imports
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class TicketManagementController implements Initializable {
    BorderPane mainPane;
    @FXML
    private GridPane gridPane;
    int currentX = 0;
    int currentY = 0;
    private String eventCSS;
    private int h = 75;
    private int w = 300;

    @FXML
    private AnchorPane windowPane;
    private List<VBox> vboxList;
    private EventManagementModel eventModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            vboxList = new ArrayList<>();
            eventModel = new EventManagementModel();
            eventCSS = getClass().getResource("/css/eventmanagementstyle.css").toExternalForm();
            gridPane.setPadding(new Insets(30, -30, 30, 0));
            gridPane.setHgap(0);
            gridPane.setVgap(25);

            //Setting up initial constraints for the grid.
            for (int i = 0; i < 3; i++) {
                ColumnConstraints columnConstraints = new ColumnConstraints();
                columnConstraints.setHgrow(Priority.ALWAYS);
                columnConstraints.setMinWidth(w);
                columnConstraints.setPrefWidth(w);

                gridPane.getColumnConstraints().add(columnConstraints);
            }

            for (int i = 0; i < 10; i++) {
                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setVgrow(Priority.ALWAYS);
                rowConstraints.setMinHeight(h);
                rowConstraints.setPrefHeight(h);

                gridPane.getRowConstraints().add(rowConstraints);

                gridPane.setAlignment(Pos.CENTER);
            }
            addExistingEvents(eventModel.getAllEventsForUser(SessionManager.getInstance().getCurrentUser().getId()));
            gridPane.getStylesheets().add(eventCSS);
            gridPane.setStyle("-fx-background-color: #F8F8F8;");
        } catch (EasvTicketException e) {
            AlertClass.alertInfo("Something went wrong", "An error has occurred while initializing the ticket management window");
        }
    }

    public void setPanel(BorderPane rootPaneEvent) {
        mainPane = rootPaneEvent;
    }

    /**
     * Creates new "event panels" that can be added to the grid.
     * Inputs all the information from the event parameter.
     * @param event2 - The event that should be displayed in the ponel.
     * @return VBox, this is the "panel".
     */
    private VBox createEventPanel(Event2 event2) {

        VBox vbox = new VBox();
        vboxList.add(vbox);
        trackWindowSize();
        vbox.getStyleClass().add("vBoxBorder");
        vbox.setCursor(javafx.scene.Cursor.HAND);

        //Creates UI elements:
        Label titleLabel = new Label(event2.getTitle());
        titleLabel.getStyleClass().add("h1");

        Label locationLabel = new Label("📍 " + event2.getLocation().getAddress());
        locationLabel.getStyleClass().add("h2");

        Label dateLabel;
        if(Objects.equals(event2.getStartDate(), event2.getEndDate()))
        {
            dateLabel = new Label("📅 " + event2.getStartDate());
        }
        else {
            dateLabel = new Label("📅 " + event2.getStartDate() + " to " + event2.getEndDate());
        }
        dateLabel.getStyleClass().add("h2");

        Label timeLabel = new Label("🕒 " + event2.getStartTime() + " - " + event2.getEndTime());
        timeLabel.getStyleClass().add("h2");

        //Adds the created UI elements to the panel.
        vbox.getChildren().addAll(titleLabel, locationLabel, dateLabel, timeLabel);

        //Runs whenever the user presses the event panel, to open detailed event view.
        vbox.setOnMouseClicked(event -> {
            if(event.getClickCount() == 1)
            {
                try{
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ticket-print-view.fxml"));
                    Parent eventInDepth = fxmlLoader.load();
                    TicketPrintController controller = fxmlLoader.getController();
                    controller.setPanel(mainPane);
                    controller.setParent(this);
                    controller.setEventDetails(event2);
                    mainPane.setCenter(eventInDepth);
                } catch (IOException e) {
                    AlertClass.alertError("Something went wrong", "An error has occurred while creating event panels");
                }
            }
        });

        return vbox;
    }

    /**
     * Adds the event given in parameter in the grid.
     * @param event
     */
    public void createEvent(Event2 event) {
        //Get the next available coordinates in the grid.
        int x = getNextX();
        int y = getNextY();
        System.out.println("(" + x + "," + y + ")"); //Debugging statement.

        //Creates the panel and then adds it.
        gridPane.add(createEventPanel(event), x, y);

        //increments the next available coordinates.
        currentX++;
    }

    /**
     * For all the available events, create a panel in the grid.
     * @param events
     */
   private void addExistingEvents(List<Event2> events){
        if(!events.isEmpty()){
            for(Event2 event : events){
                createEvent(event);
            }
        }
    }

    private int getNextX() {
        return currentX % 3;
    }

    private int getNextY() {
        return currentX / 3;
    }

    /**
     * Tracks window size and resizes the event panels accordingly.
     * If work period is expanded, revamp of this method would be preferred.
     */
    private void trackWindowSize() {
        windowPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            double width = newValue.doubleValue();
            int columns = gridPane.getColumnConstraints().size();
            double columnWidth = width / (columns+0.4);
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
                row.setMinHeight(rowHeight * 0.7);
                row.setPrefHeight(rowHeight * 0.7);
                row.setMaxHeight(rowHeight * 0.7);
            }
        });
    }
}
