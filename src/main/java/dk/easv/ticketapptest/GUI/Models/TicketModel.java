package dk.easv.ticketapptest.GUI.Models;

import dk.easv.ticketapptest.BE.Ticket;
import dk.easv.ticketapptest.BLL.TicketManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;

public class TicketModel {
    private TicketManager ticketManager;
    private ObservableList<Ticket> tickets;


    public TicketModel() throws IOException {
        ticketManager = new TicketManager();
        tickets = FXCollections.observableArrayList();
    }

    public void createTicket(Ticket ticket) throws SQLException {
        Ticket createdTicket = ticketManager.createTicket(ticket);
        System.out.println(createdTicket.getTicketID() + ", " + createdTicket.getTicketName() + ", " + createdTicket.isGLOBAL());
        tickets.add(createdTicket);
    }
}
