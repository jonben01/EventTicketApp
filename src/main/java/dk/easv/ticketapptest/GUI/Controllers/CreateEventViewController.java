package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Location;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.GUI.Models.EventManagementModel;
import dk.easv.ticketapptest.GUI.TemporaryDataClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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





    TemporaryDataClass data;

    //todo bad. fix.
    EventManagementModel model;


    private EventEventManagementController parent;

    @FXML
    private void initialize() throws SQLException, IOException {
        model = new EventManagementModel();
        data = new TemporaryDataClass();
        String cssFile = getClass().getResource("/css/usermanagementstyle.css").toExternalForm();
        btnCreateEvent.getStylesheets().add(cssFile);
        //btnAddTicket.getStylesheets().add(cssFile);
    }



    public void setParent(EventEventManagementController parent) {
        this.parent = parent;
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
            Event2 event = new Event2(txtNameEvent.getText(), location, txtDescriptionEvent.getText(), txtLocationGuidance.getText(), dateStartDate.getValue(), dateEndDate.getValue(), LocalTime.parse(txtStartEvent.getText()), LocalTime.parse(txtEndEvent.getText()), new String[]{"ticket 1", "ticket 2"}, data.getUsers());
            parent.createEvent(event);
            model.createEvent(event);
            ((Stage) txtNameEvent.getScene().getWindow()).close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill in all the fields");
            alert.setContentText("You must fill in all the fields to create an event.");
            alert.showAndWait();
        }

    }


}
