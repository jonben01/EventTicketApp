package dk.easv.ticketapptest.BLL;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.DAL.EventDAO;

public class EventManager {
    EventDAO eventDAO;

    public EventManager() {
        eventDAO = new EventDAO();
    }


    public Event2 getAllEvents() {
        return eventDAO.getAllEvents();
    }

    public Event2 createEvent(Event2 event) {
        Event2 createdEvent = eventDAO.createEvent(event);
        return null;
    }

    public void DeleteEvent(Event2 eventToBeDeleted) {
        eventDAO.deleteEvent(eventToBeDeleted);
    }
}
