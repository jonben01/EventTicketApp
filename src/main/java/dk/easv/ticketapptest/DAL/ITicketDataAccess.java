package dk.easv.ticketapptest.DAL;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Ticket;

import java.sql.SQLException;
import java.util.List;

public interface ITicketDataAccess {

    public Ticket createTicket(Ticket ticket) throws SQLException;

    public void deleteTicket(Ticket ticket) throws SQLException;

    public Ticket updateTicket(Ticket ticket) throws SQLException;

    public List<Ticket> getAllTickets() throws SQLException;

    public List<Ticket> getTicketsForEvent(Event2 event) throws SQLException;

    public void AssignTicketToEvent(Ticket ticket, Event2 event) throws SQLException;

}
