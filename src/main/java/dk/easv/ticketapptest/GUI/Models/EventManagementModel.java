package dk.easv.ticketapptest.GUI.Models;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.EventManager;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EventManagementModel {

    private ObservableList<Event2> eventList;
    private EventManager eventManager;

    public EventManagementModel() throws IOException, EasvTicketException {
        eventManager = new EventManager();
        eventList = FXCollections.observableArrayList();
        eventList.addAll(eventManager.getAllEvents());
    }

    public ObservableList<Event2> getObservableEvents() throws EasvTicketException {return eventList;}

    public void createEvent(Event2 event) throws EasvTicketException {
        Event2 eventCreated = eventManager.createEvent(event);
        eventList.add(eventCreated);
    }

    public void deleteEvent(Event2 eventToBeDeleted) throws EasvTicketException {
        eventManager.deleteEvent(eventToBeDeleted);
        updateList();
    }

    public void updateList() throws EasvTicketException {
        eventList.clear();
        eventList.addAll(eventManager.getAllEvents());
    }

    public void updateEvent(Event2 event) throws EasvTicketException {
        eventManager.updateEvent(event);
        updateList();
    }

    public List<User> getAllUsersForEvent(int eventID) throws EasvTicketException {
        return eventManager.getAllUsersForEvent(eventID);
    }

    public void addToEventUsers(Event2 selectedEvent) throws EasvTicketException {
        eventManager.addToEventUsers(selectedEvent);
    }

    public void removeFromEventUsers(Event2 selectedEvent) throws EasvTicketException {
        eventManager.removeFromEventUsers(selectedEvent);
    }

    public List<Event2> getAllEventsForUser(int userid) throws EasvTicketException {return eventManager.getAllEventsForUser(userid);}
}
