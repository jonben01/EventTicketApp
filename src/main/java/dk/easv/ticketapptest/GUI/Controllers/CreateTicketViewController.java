package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Ticket;
import dk.easv.ticketapptest.GUI.Models.TicketModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class CreateTicketViewController {

    @FXML
    private TextField txtTicketName;
    @FXML
    private TextArea txtTicketDesc;
    @FXML
    private TextField txtTicketPrice;
    @FXML
    private Button btnCreateTicket;

    private TicketPrintController parent;
    private EventViewController parent2;

    private Event2 selectedEvent;
    private TicketModel ticketModel;
    private Ticket selectedTicket;

    @FXML
    private CheckBox chkGlobal;


    public CreateTicketViewController() throws IOException {
        ticketModel = new TicketModel();
    }
    // todo: find a better way to do this:
    public void setParent(TicketPrintController parent) {
        this.parent = parent;
    }
    public void setParent(EventViewController parent) { this.parent2 = parent; }

    public void setSelectedEvent(Event2 event2) {
        this.selectedEvent = event2;

    }

    @FXML
    private void handleCreateTicket(ActionEvent actionEvent) throws SQLException {
        String name = txtTicketName.getText();
        String description = txtTicketDesc.getText();
        double price = Double.parseDouble(txtTicketPrice.getText());


        if (selectedTicket == null) {
            // Create new ticket
            Ticket ticket = new Ticket(selectedEvent, price, chkGlobal.isSelected(), name, description);
            ticketModel.createTicket(ticket);

        } else {
            // Update existing ticket
            selectedTicket.setTicketName(name);
            selectedTicket.setDescription(description);
            selectedTicket.setPrice(price);
            selectedTicket.setGLOBAL(chkGlobal.isSelected());
            ticketModel.updateTicket(selectedTicket);
        }
        parent2.updateTicketList();
        Stage stage = (Stage) txtTicketName.getScene().getWindow();
        stage.close();


    }
    public void setSelectedTicket(Ticket selectedTicket) {
        this.selectedTicket = selectedTicket;
        txtTicketName.setText(selectedTicket.getTicketName());
        txtTicketDesc.setText(selectedTicket.getDescription());
        txtTicketPrice.setText(String.valueOf(selectedTicket.getPrice()));
        chkGlobal.setSelected(selectedTicket.isGLOBAL());
        btnCreateTicket.setText("Update Ticket");
    }
}
