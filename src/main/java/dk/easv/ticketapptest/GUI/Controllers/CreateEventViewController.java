package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Location;
import dk.easv.ticketapptest.BE.Ticket;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.SessionManager;
import dk.easv.ticketapptest.GUI.AlertClass;
import dk.easv.ticketapptest.GUI.Models.EventManagementModel;
import dk.easv.ticketapptest.GUI.TemporaryDataClass;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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






    TemporaryDataClass data;

    //todo bad. fix.
    EventManagementModel model;


    private EventEventManagementController parent;
    private EventViewController eventViewController;
    @FXML
    private Label txtTitle;
    @FXML
    private Label txtInfo;

    @FXML
    private void initialize() throws SQLException, IOException {
        sessionUser = SessionManager.getInstance().getCurrentUser();

        model = new EventManagementModel();
        data = new TemporaryDataClass();
        String cssFile = getClass().getResource("/css/usermanagementstyle.css").toExternalForm();
        btnCreateEvent.getStylesheets().add(cssFile);
        //btnAddTicket.getStylesheets().add(cssFile);
    }

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
        txtInfo.setText("Fill in the details to update the selected event!");

    }

    public void setParent(EventEventManagementController parent) {
        this.parent = parent;
    }

    public void setEventViewController(EventViewController eventViewController) {
        this.eventViewController = eventViewController;
    }


    public void CreateEvent(ActionEvent actionEvent) throws SQLException {

            if (!txtNameEvent.getText().isEmpty()
                    && !dateStartDate.getValue().toString().isEmpty()
                    && !dateEndDate.getValue().toString().isEmpty()
                    && !txtEndEvent.getText().isEmpty()
                    && !txtDescriptionEvent.getText().isEmpty()
                    && !txtStartEvent.getText().isEmpty()
                    && !txtLocationGuidance.getText().isEmpty()
                    && !txtCity.getText().isEmpty()
                    && !txtAddress.getText().isEmpty()
                    && !txtPostalCode.getText().isEmpty()) {
                Location location = new Location(txtAddress.getText(), txtCity.getText(), Integer.parseInt(txtPostalCode.getText()));
                if(selectedEvent != null) {
                    Event2 event = new Event2(selectedEvent.getEventID(), txtNameEvent.getText(), location, txtDescriptionEvent.getText(), txtLocationGuidance.getText(), dateStartDate.getValue(), dateEndDate.getValue(), LocalTime.parse(txtStartEvent.getText()), LocalTime.parse(txtEndEvent.getText()), new ArrayList<Ticket>(), selectedEvent.getEventCoordinators(), "Scheduled");
                    model.updateEvent(event);
                    eventViewController.updateInformation(1);
                }
                else {
                    Event2 event = new Event2(txtNameEvent.getText(), location, txtDescriptionEvent.getText(), txtLocationGuidance.getText(), dateStartDate.getValue(), dateEndDate.getValue(), LocalTime.parse(txtStartEvent.getText()), LocalTime.parse(txtEndEvent.getText()), new ArrayList<Ticket>() {
                    }, new ArrayList<>(Arrays.asList(sessionUser)));
                    parent.createEvent(event, true);
                    model.createEvent(event);
                }
                ((Stage) txtNameEvent.getScene().getWindow()).close();
            } else {
                AlertClass.alertError("Missing information", "Please fill all the fields");
            }
        }

    }
