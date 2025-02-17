package dk.easv.ticketapptest.GUI;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Ticket;
import dk.easv.ticketapptest.BE.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;


public class TemporaryDataClass {

    // SHOULD JUST CONTAIN THE OBSERVABLE LISTS OF ALL OUR MOCK DATA

    private ObservableList<Event2> eventsToBeViewed;
    private ObservableList<Ticket> ticketsToBeViewed;
    private ObservableList<User> usersToBeViewed;

    public TemporaryDataClass() {
        eventsToBeViewed = FXCollections.observableArrayList();
        ticketsToBeViewed = FXCollections.observableArrayList();
        usersToBeViewed = FXCollections.observableArrayList();


        //mock data
        User user1 = new User("John", "Cooper", "johncooper@mail.com", "+45 66827391");
        User user2 = new User("Sarah", "Wilson", "sarahwilson@mail.com", "+45 45829101");
        User user3 = new User("Michael", "Brown", "michaelbrown@mail.com", "+45 12749228");
        usersToBeViewed.add(user1);
        usersToBeViewed.add(user2);
        usersToBeViewed.add(user3);


        //TODO finish ticket BE and then make mock data.

        //Mock data - Event
        Event2 event1 = new Event2("Summer Music Festival", "Central Park, New York", "July 15, 2025", "2:00pm", "10:00pm", new String[]{"good one", "not as good one"}, usersToBeViewed);
        Event2 event2 = new Event2("Tech Conference 2025", "Convention Center", "August 20, 2025", "9:00am", "6:00pm", new String[]{"VIP", "Standard"}, usersToBeViewed);
        Event2 event3 = new Event2("Food & Wine Festival", "Waterfront Park", "September 5, 2025", "12:00pm", "8:00pm", new String[]{"pick this one", "don't pick this one"}, usersToBeViewed);
        eventsToBeViewed.add(event1);
        eventsToBeViewed.add(event2);
        eventsToBeViewed.add(event3);

    }

    //EVENTS SECTION
    public void createEvent(Event2 event2) {
        eventsToBeViewed.add(event2);
    }

    public ObservableList<Event2> getEvents() {
        return eventsToBeViewed;
    }

    public void deleteEvent(Event2 event2) {
        eventsToBeViewed.remove(event2);
    }

    public void updateEvent(Event2 event2, Event2 updatedEvent2) {
        //idk if this works ngl
        eventsToBeViewed.set(eventsToBeViewed.indexOf(event2), updatedEvent2);

    }


    //TICKETS SECTION

    public void createTicket(Ticket ticket) {
        ticketsToBeViewed.add(ticket);
    }

    public ObservableList<Ticket> getTickets(Ticket ticket) {
        return ticketsToBeViewed;
    }

    public void deleteTicket(Ticket ticket) {
        ticketsToBeViewed.remove(ticket);
    }

    public void updateTicket(Ticket ticketToBeRemoved, Ticket updatedTicket) {
        ticketsToBeViewed.remove(ticketToBeRemoved);
        ticketsToBeViewed.add(updatedTicket);
    }

    //USER SECTION

    public void createUser(User user) {
        usersToBeViewed.add(user);
    }

    public List<User> getUsers() {
        return usersToBeViewed;
    }

    public void deleteUser(User user) {
        usersToBeViewed.remove(user);
    }

    public void updateUser(User userToBeUpdated, User updatedUser) {
        usersToBeViewed.remove(userToBeUpdated);
        usersToBeViewed.add(updatedUser);
    }

}
