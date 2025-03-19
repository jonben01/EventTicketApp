package dk.easv.ticketapptest.BLL;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Location;
import dk.easv.ticketapptest.DAL.EventDAO;
import dk.easv.ticketapptest.DAL.LocationDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EventManager {
    EventDAO eventDAO;
    LocationDAO locationDAO;

    public EventManager() throws IOException {
        eventDAO = new EventDAO();
        locationDAO = new LocationDAO();
    }


    public List<Event2> getAllEvents() throws SQLException {
        return eventDAO.getAllEvents();
    }

    public Event2 createEvent(Event2 event) throws SQLException {
        Location createdLocation = locationDAO.createLocation(event.getLocation());
        Event2 createdEvent = eventDAO.createEvent(event, createdLocation);
        createdEvent.setLocation(createdLocation);
        return createdEvent;

    }

    public void DeleteEvent(Event2 eventToBeDeleted) throws SQLException {
        eventDAO.deleteEvent(eventToBeDeleted);
    }

    public void updateEvent(Event2 event) throws SQLException {
        Location createdLocation = locationDAO.createLocation(event.getLocation());
        eventDAO.updateEvent(event, createdLocation);

    }
}
