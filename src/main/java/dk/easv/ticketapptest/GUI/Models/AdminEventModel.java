package dk.easv.ticketapptest.GUI.Models;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BLL.EventManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;

public class AdminEventModel {

    private EventManager eventManager;

    private ObservableList<Event2> eventsToBeViewed;

    public AdminEventModel() throws IOException, SQLException {
        eventManager = new EventManager();
        eventsToBeViewed = FXCollections.observableArrayList();
        eventsToBeViewed.addAll(eventManager.getAllEvents());
    }

    public ObservableList<Event2> getObservableEvents() throws SQLException {
        return eventsToBeViewed;
    }

    public void deleteEvent(Event2 event) throws SQLException {
        eventManager.deleteEvent(event);
        eventsToBeViewed.remove(event);
    }
}
