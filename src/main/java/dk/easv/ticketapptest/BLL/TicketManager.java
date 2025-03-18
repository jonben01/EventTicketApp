package dk.easv.ticketapptest.BLL;

import dk.easv.ticketapptest.BE.Ticket;
import dk.easv.ticketapptest.DAL.TicketDAO;

import java.io.IOException;
import java.sql.SQLException;

public class TicketManager {
    TicketDAO ticketDAO;

    public TicketManager() throws IOException {
        ticketDAO = new TicketDAO();
    }

    public Ticket createTicket(Ticket ticket) throws SQLException {
        return ticketDAO.createTicket(ticket);
    }
}
