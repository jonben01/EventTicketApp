package dk.easv.ticketapptest.DAL;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Location;

import java.sql.SQLException;
import java.util.List;

public interface IEventDataAccess {

    Event2 createEvent (Event2 event, Location location) throws SQLException;

    List<Event2> getAllEvents () throws SQLException;

    void updateEvent (Event2 event, Location location) throws SQLException;

    void deleteEvent (Event2 event) throws SQLException;

}
