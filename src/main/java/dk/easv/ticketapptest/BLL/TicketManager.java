package dk.easv.ticketapptest.BLL;

import dk.easv.ticketapptest.BE.Customer;
import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Ticket;
import dk.easv.ticketapptest.DAL.TicketDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TicketManager {
    TicketDAO ticketDAO;

    public TicketManager() throws IOException {
        ticketDAO = new TicketDAO();
    }

    public Ticket createTicket(Ticket ticket) throws SQLException {
        return ticketDAO.createTicket(ticket);
    }

    public List<Ticket> getTicketsForEvent(Event2 event) throws SQLException {
        return ticketDAO.getTicketsForEvent(event);
    }

    public void updateTicket(Ticket selectedTicket) throws SQLException {
        ticketDAO.updateTicket(selectedTicket);
    }

    public void deleteTicket(Ticket selectedTicket) throws SQLException {
        ticketDAO.deleteTicket(selectedTicket);
    }

    public void savePrintedTicket(String rndString, Ticket selectedTicket, Event2 selectedEvent, Customer customer) throws SQLException {
        ticketDAO.savePrintedTicket(rndString, selectedTicket, selectedEvent, customer);
    }
}
