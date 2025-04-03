package dk.easv.ticketapptest.GUI.Models;
//project imports
import dk.easv.ticketapptest.BE.Customer;
import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Ticket;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
import dk.easv.ticketapptest.BLL.TicketManager;
//java imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.util.List;

public class TicketModel {
    private TicketManager ticketManager;
    private ObservableList<Ticket> tickets;


    public TicketModel() throws EasvTicketException {
        try {
            ticketManager = new TicketManager();
            tickets = FXCollections.observableArrayList();
        } catch (IOException e) {
            throw new EasvTicketException("Error instantiating ticket model", e);
        }

    }

    public void createTicket(Ticket ticket) throws EasvTicketException {
        Ticket createdTicket = ticketManager.createTicket(ticket);
        System.out.println(createdTicket.getTicketID() + ", " + createdTicket.getTicketName() + ", " + createdTicket.isGLOBAL());
        tickets.add(createdTicket);
    }

    public List<Ticket> getTicketsForEvent(Event2 event) throws EasvTicketException {
        tickets.clear();
        ticketManager.getTicketsForEvent(event);
        tickets.addAll(ticketManager.getTicketsForEvent(event));
        return tickets;
    }

    public void updateTicket(Ticket selectedTicket) throws EasvTicketException {
        ticketManager.updateTicket(selectedTicket);
    }

    public void deleteTicket(Ticket selectedTicket) throws EasvTicketException {
        ticketManager.deleteTicket(selectedTicket);
    }

    public void savePrintedTicket(String rndString, Ticket selectedTicket, Event2 selectedEvent, Customer customer) throws EasvTicketException {
        ticketManager.savePrintedTicket(rndString, selectedTicket, selectedEvent, customer);
    }
}
