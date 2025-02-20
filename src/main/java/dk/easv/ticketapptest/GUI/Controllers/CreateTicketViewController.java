package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Event2;
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
    private EventViewController parent2;

    private Event2 selectedEvent;

    // todo: find a better way to do this:
    public void setParent(TicketPrintController parent) {
        this.parent = parent;
    }
    public void setParent(EventViewController parent) { this.parent2 = parent; }

    public void setSelectedEvent(Event2 event2) {
        this.selectedEvent = event2;
    }

    @FXML
    private void handleCreateTicket(ActionEvent actionEvent) {
        String name = txtTicketName.getText();
        String description = txtTicketDesc.getText();
        double price = Double.parseDouble(txtTicketPrice.getText());

        Ticket ticket = new Ticket(selectedEvent, price, false, name, description);
        if(parent != null) {
            parent.addTicket(ticket);
        }
        else{
            parent2.addTicket(ticket);
        }

        Stage stage = (Stage) txtTicketName.getScene().getWindow();
        stage.close();


    }


}
