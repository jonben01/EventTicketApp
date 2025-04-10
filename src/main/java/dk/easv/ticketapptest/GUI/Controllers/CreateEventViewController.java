package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Location;
import dk.easv.ticketapptest.BE.Ticket;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
import dk.easv.ticketapptest.BLL.SessionManager;
import dk.easv.ticketapptest.GUI.AlertClass;
import dk.easv.ticketapptest.GUI.Models.EventManagementModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CreateEventViewController {


    public Button btnCreateEvent;
    @FXML
    private TextField txtNameEvent;
    @FXML
    private TextArea txtDescriptionEvent;
    @FXML
    private DatePicker dateStartDate;
    @FXML
    private DatePicker dateEndDate;
    @FXML
    private TextField txtStartEvent;
    @FXML
    private TextField txtEndEvent;
    @FXML
    private TextArea txtLocationGuidance;
    @FXML
    private TextField txtCity;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtPostalCode;

    private Event2 selectedEvent;

    private User sessionUser;

    EventManagementModel model;


    private EventEventManagementController parent;
    private EventViewController eventViewController;
    @FXML
    private Label txtTitle;

    @FXML
    private void initialize()  {

        try {
            sessionUser = SessionManager.getInstance().getCurrentUser();

            model = new EventManagementModel();
            String cssFile = getClass().getResource("/css/usermanagementstyle.css").toExternalForm();
            btnCreateEvent.getStylesheets().add(cssFile);
        } catch (EasvTicketException e) {
            e.printStackTrace();
            AlertClass.alertError("Something went wrong", "An error has occurred while initializing the event creation window");
        }
    }

    /**
     * This method is only run in the EventViewController.
     * It is used to set the selected event, and change the UI to make it edit the selectedEvent,
     * instead of creating a new one.
     * @param event
     */
    public void selectEvent(Event2 event)
    {
        selectedEvent = event;
        txtNameEvent.setText(selectedEvent.getTitle());
        dateStartDate.setValue(selectedEvent.getStartDate());
        dateEndDate.setValue(selectedEvent.getEndDate());
        txtDescriptionEvent.setText(selectedEvent.getDescription());
        txtLocationGuidance.setText(selectedEvent.getLocationGuidance());
        txtStartEvent.setText(selectedEvent.getStartTime().toString());
        txtEndEvent.setText(selectedEvent.getEndTime().toString());
        txtCity.setText(selectedEvent.getLocation().getCity());
        txtAddress.setText(selectedEvent.getLocation().getAddress());
        txtPostalCode.setText(String.valueOf(selectedEvent.getLocation().getPostalCode()));
        btnCreateEvent.setText("Update Event");
        txtTitle.setText("Update Event!");
    }

    public void setParent(EventEventManagementController parent) {
        this.parent = parent;
    }

    public void setEventViewController(EventViewController eventViewController) {
        this.eventViewController = eventViewController;
    }

    /**
     * This method is run when the "Create Event" button is pressed.
     * It is used to check if the areas aren't null and thereafter create/edit the event.
     * @param actionEvent
     */
    public void CreateEvent(ActionEvent actionEvent) {
        LocalTime startTime;
        LocalTime endTime;

        String timePattern = "^([01]\\d|2[0-3]):([0-5]\\d)$"; // [xx:xx] pattern

        // Checks if all the information fields have been filled.
        if (dateStartDate.getValue() == null || dateEndDate.getValue() == null ||
                !txtNameEvent.getText().isEmpty() && !dateStartDate.getValue().toString().isEmpty() &&
                        !dateEndDate.getValue().toString().isEmpty() &&
                        !txtEndEvent.getText().isEmpty() &&
                        !txtDescriptionEvent.getText().isEmpty() &&
                        !txtStartEvent.getText().isEmpty() &&
                        !txtLocationGuidance.getText().isEmpty() &&
                        !txtCity.getText().isEmpty() &&
                        !txtAddress.getText().isEmpty() &&
                        !txtPostalCode.getText().isEmpty()) {

            //Additional checks due to DateTime not throwing Exceptions.
            try {
                if (!txtStartEvent.getText().isEmpty() && txtStartEvent.getText().matches(timePattern)) {
                    startTime = LocalTime.parse(txtStartEvent.getText());
                } else {
                    throw new EasvTicketException("Invalid start time format. Use HH:mm.");
                }

                if (!txtEndEvent.getText().isEmpty() && txtEndEvent.getText().matches(timePattern)) {
                    endTime = LocalTime.parse(txtEndEvent.getText());
                } else {
                    throw new EasvTicketException("Invalid end time format. Use HH:mm.");
                }

                if (startTime == null || endTime == null) {
                    throw new EasvTicketException("Start time or end time is empty.");
                }
                    Location location = new Location(txtAddress.getText(), txtCity.getText(), Integer.parseInt(txtPostalCode.getText()));

                //If event is being updated, run:
                    if (selectedEvent != null) {
                        Event2 event = new Event2(selectedEvent.getEventID(), txtNameEvent.getText(), location, txtDescriptionEvent.getText(), txtLocationGuidance.getText(), dateStartDate.getValue(), dateEndDate.getValue(), LocalTime.parse(txtStartEvent.getText()), LocalTime.parse(txtEndEvent.getText()), new ArrayList<Ticket>(), selectedEvent.getEventCoordinators(), "Scheduled");
                        model.updateEvent(event);
                        eventViewController.updateInformation(1);

                //if even is being created, run:
                    } else {
                        Event2 event = new Event2(txtNameEvent.getText(), location, txtDescriptionEvent.getText(), txtLocationGuidance.getText(), dateStartDate.getValue(), dateEndDate.getValue(), LocalTime.parse(txtStartEvent.getText()), LocalTime.parse(txtEndEvent.getText()), (List<Ticket>) new ArrayList<Ticket>() {
                        }, new ArrayList<>(Arrays.asList(sessionUser)));
                        parent.createEvent(event, true);
                        model.createEvent(event);
                    }
                    ((Stage) txtNameEvent.getScene().getWindow()).close();
                } catch (EasvTicketException e) {
                    e.printStackTrace();
                    AlertClass.alertError("Invalid time format", "The time format is incorrect. Make sure you use the Hour:Minute format");
                }

            } else {
                AlertClass.alertError("Missing information", "Please fill all the fields");
            }
        }
    }
