package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Ticket;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
import dk.easv.ticketapptest.GUI.AlertClass;
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


    public CreateTicketViewController() {
        try {
            ticketModel = new TicketModel();
        } catch (EasvTicketException e) {
            e.printStackTrace();
            AlertClass.alertError("Error occurred", "Something went wrong");
        }
    }
    public void setParent(TicketPrintController parent) {
        this.parent = parent;
    }
    public void setParent(EventViewController parent) { this.parent2 = parent; }

    public void setSelectedEvent(Event2 event2) {
        this.selectedEvent = event2;

    }

    /**
     * Runs the actions whenever the create/edit button is pressed.
     * Either creates a new ticket, or updates the already existing information.
     * @param actionEvent
     */
    @FXML
    private void handleCreateTicket(ActionEvent actionEvent) {
        String name = txtTicketName.getText();
        String description = txtTicketDesc.getText();
        double price = Double.parseDouble(txtTicketPrice.getText());

        // Create new ticket
        if (selectedTicket == null) {
            Ticket ticket = new Ticket(selectedEvent, price, chkGlobal.isSelected(), name, description);
            try {
                ticketModel.createTicket(ticket);
            } catch (EasvTicketException e) {
                e.printStackTrace();
                AlertClass.alertError("Error","An error occurred while creating ticket");
            }

        // Update existing ticket
        } else {
            try {
                selectedTicket.setTicketName(name);
                selectedTicket.setDescription(description);
                selectedTicket.setPrice(price);
                selectedTicket.setGLOBAL(chkGlobal.isSelected());
                ticketModel.updateTicket(selectedTicket);
            } catch (EasvTicketException e) {
                e.printStackTrace();
                AlertClass.alertError("Error","An error occurred while updating ticket");
            }
        }
        parent2.updateTicketList();
        Stage stage = (Stage) txtTicketName.getScene().getWindow();
        stage.close();


    }

    /**
     * Change the UI from create ticket window to Edit ticket window.
     * @param selectedTicket
     */
    public void setSelectedTicket(Ticket selectedTicket) {
        this.selectedTicket = selectedTicket;
        txtTicketName.setText(selectedTicket.getTicketName());
        txtTicketDesc.setText(selectedTicket.getDescription());
        txtTicketPrice.setText(String.valueOf(selectedTicket.getPrice()));
        chkGlobal.setSelected(selectedTicket.isGLOBAL());
        btnCreateTicket.setText("Update Ticket");
    }
}
