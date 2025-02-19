package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CreateTicketViewController {

    @FXML
    private TextField txtTicketName;
    @FXML
    private TextArea txtTicketDesc;
    @FXML
    private TextField txtTicketPrice;

    private TicketPrintController parent;

    public void setParent(TicketPrintController parent) {
        this.parent = parent;
    }

    @FXML
    private void handleCreateTicket(ActionEvent actionEvent) {

    }
}
