package dk.easv.ticketapptest.GUI.Models;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Location;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.EventManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EventManagementModel {

    private ObservableList<Event2> eventList;
    private EventManager eventManager;

    public EventManagementModel() throws IOException, SQLException {
        eventManager = new EventManager();
        eventList = FXCollections.observableArrayList();
        eventList.addAll(eventManager.getAllEvents());
    }

    public ObservableList<Event2> getObservableEvents() throws SQLException {return eventList;}

    public void createEvent(Event2 event) throws SQLException {
        Event2 eventCreated = eventManager.createEvent(event);
        eventList.add(eventCreated);
    }

    public void deleteEvent(Event2 eventToBeDeleted) throws SQLException {
        eventManager.DeleteEvent(eventToBeDeleted);
        updateList();
    }

    public void updateList() throws SQLException {
        eventList.clear();
        eventList.addAll(eventManager.getAllEvents());
    }

    public void updateEvent(Event2 event) throws SQLException {
        eventManager.updateEvent(event);
        updateList();
    }

    public List<User> getAllUsersForEvent(int eventID) throws SQLServerException {
        return eventManager.getAllUsersForEvent(eventID);
    }

    public void addToEventUsers(Event2 selectedEvent) throws SQLServerException {
        eventManager.addToEventUsers(selectedEvent);
    }

    public void removeFromEventUsers(Event2 selectedEvent) {
        eventManager.removeFromEventUsers(selectedEvent);
    }
}
