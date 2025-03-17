package dk.easv.ticketapptest.DAL;

import dk.easv.ticketapptest.BE.Event2;

import java.io.IOException;

public class EventDAO {
    private DBConnector connector;

    public EventDAO() throws IOException {
    connector = new DBConnector();
    }

    public Event2 getAllEvents() {
        return null;
    }

    public Event2 createEvent(Event2 event) {
        return null;
    }

    public void deleteEvent(Event2 eventToBeDeleted) {
    }
}
