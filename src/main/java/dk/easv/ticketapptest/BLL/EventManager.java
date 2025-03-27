package dk.easv.ticketapptest.BLL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Location;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.util.AdminEventSearcher;
import dk.easv.ticketapptest.DAL.EventDAO;
import dk.easv.ticketapptest.DAL.LocationDAO;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EventManager {
    EventDAO eventDAO;
    LocationDAO locationDAO;
    private AdminEventSearcher adminEventSearcher;

    public EventManager() throws IOException {
        eventDAO = new EventDAO();
        locationDAO = new LocationDAO();
        adminEventSearcher = new AdminEventSearcher();
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

    public void deleteEvent(Event2 eventToBeDeleted) throws SQLException {
        eventDAO.deleteEvent(eventToBeDeleted);
    }

    public void updateEvent(Event2 event) throws SQLException {
        Location createdLocation = locationDAO.createLocation(event.getLocation());
        eventDAO.updateEvent(event, createdLocation);

    }


    public List<User> getAllUsersForEvent(int eventID) throws SQLServerException {
        return eventDAO.getAllUsersForEvent(eventID);
    }

    public void addToEventUsers(Event2 selectedEvent) throws SQLServerException {
        eventDAO.addToEventUsers(selectedEvent);
    }

    public void removeFromEventUsers(Event2 selectedEvent) {
        eventDAO.removeFromEventUsers(selectedEvent);
    }

    public List<Event2> getAllEventsForUser(int userid) throws SQLException {return eventDAO.getAllEventsForUser(userid);}

    public ObservableList<Event2> searchEvent(String searchQuery) throws Exception {
        List<Event2> searchBase = getAllEvents();
        return adminEventSearcher.searchEvents(searchBase, searchQuery);
    }
}

