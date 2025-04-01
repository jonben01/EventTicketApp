package dk.easv.ticketapptest.GUI.Models;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BLL.EventManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminEventModel {

    private EventManager eventManager;

    private ObservableList<Event2> eventsToBeViewed;

    public AdminEventModel() throws IOException, SQLException {
        eventManager = new EventManager();
        eventsToBeViewed = FXCollections.observableArrayList();
        eventsToBeViewed.addAll(eventManager.getAllEvents());
    }

    public ObservableList<Event2> getObservableEvents() {
        return eventsToBeViewed;
    }

    public void deleteEvent(Event2 event) throws SQLException {
        eventManager.deleteEvent(event);
        eventsToBeViewed.remove(event);
    }

    public ObservableList<Event2> searchEvent(String searchQuery) throws Exception {
        return eventManager.searchEvent(searchQuery);
    }

    public ObservableList<Event2> getAllEvents() throws Exception {
        List<Event2> allEvents = eventManager.getAllEvents();
        return FXCollections.observableArrayList(allEvents);
    }
}
