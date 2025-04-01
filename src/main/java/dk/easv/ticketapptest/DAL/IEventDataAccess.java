package dk.easv.ticketapptest.DAL;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Location;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;

import java.sql.SQLException;
import java.util.List;

public interface IEventDataAccess {

    Event2 createEvent (Event2 event, Location location) throws EasvTicketException;

    List<Event2> getAllEvents () throws EasvTicketException;

    void updateEvent (Event2 event, Location location) throws EasvTicketException;

    void deleteEvent (Event2 event) throws EasvTicketException;

}
