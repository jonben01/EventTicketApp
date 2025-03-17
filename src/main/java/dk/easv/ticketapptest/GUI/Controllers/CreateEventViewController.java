package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Location;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.GUI.TemporaryDataClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;

public class CreateEventViewController {


    public Button btnCreateEvent;
    @FXML
    private TextField txtNameEvent;
    @FXML
    private TextArea txtDescriptionEvent;
    @FXML
    private TextField txtDateEvent;
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


    private EventEventManagementController parent;

    @FXML
    private void initialize() {
        data = new TemporaryDataClass();
        String cssFile = getClass().getResource("/css/usermanagementstyle.css").toExternalForm();
        btnCreateEvent.getStylesheets().add(cssFile);
        //btnAddTicket.getStylesheets().add(cssFile);
    }



    public void setParent(EventEventManagementController parent) {
        this.parent = parent;
    }


    public void CreateEvent(ActionEvent actionEvent) {

        if (!txtNameEvent.getText().isEmpty()
                && !txtDateEvent.getText().isEmpty()
                && !txtEndEvent.getText().isEmpty()
                && !txtDescriptionEvent.getText().isEmpty()
                && !txtStartEvent.getText().isEmpty()
                && !txtLocationGuidance.getText().isEmpty()
                && !txtCity.getText().isEmpty()
                && !txtAddress.getText().isEmpty()
                && !txtPostalCode.getText().isEmpty()) {
          //  parent.createEvent(txtNameEvent.getText(), txtLocationGuidance.getText(), txtDateEvent.getText(), txtStartEvent.getText(), txtEndEvent.getText(), new String[]{"Early Bird $299", "Regular $399"}, data.getUsers() );
            Location location = new Location(txtAddress.getText(), txtCity.getText(), Integer.parseInt(txtPostalCode.getText()));
            Event2 event = new Event2(txtNameEvent.getText(), location, txtLocationGuidance.getText(), LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime,
                    String[] ticketTypes, List< User > eventCoordinators);
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
