package dk.easv.ticketapptest.DAL;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Ticket;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;

import java.sql.SQLException;
import java.util.List;

public interface ITicketDataAccess {

    public Ticket createTicket(Ticket ticket) throws EasvTicketException;

    public void deleteTicket(Ticket ticket) throws EasvTicketException;

    public Ticket updateTicket(Ticket ticket) throws EasvTicketException;

    public List<Ticket> getAllTickets() throws EasvTicketException;

    public List<Ticket> getTicketsForEvent(Event2 event) throws EasvTicketException;

    public void AssignTicketToEvent(Ticket ticket, Event2 event) throws EasvTicketException;

}
