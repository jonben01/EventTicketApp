package dk.easv.ticketapptest.GUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreateEventViewController {

    @FXML
    private TextField txtNameEvent;


    public String getEventName() {
        return txtNameEvent.getText();
    }






}
