package dk.easv.ticketapptest.GUI;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Role;
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


        //Mock data - Event
        /*Event2 event1 = new Event2("Summer Music Festival", "Central Park, New York", "July 15, 2025", "2:00pm", "10:00pm", new String[]{"good one", "not as good one"}, usersToBeViewed);
        Event2 event2 = new Event2("Tech Conference 2025", "Convention Center", "August 20, 2025", "9:00am", "6:00pm", new String[]{"VIP", "Standard"}, usersToBeViewed);
        Event2 event3 = new Event2("Food & Wine Festival", "Waterfront Park", "September 5, 2025", "12:00pm", "8:00pm", new String[]{"pick this one", "don't pick this one"}, usersToBeViewed);
        eventsToBeViewed.add(event1);
        eventsToBeViewed.add(event2);
        eventsToBeViewed.add(event3);*/

        //Mock data - Ticket
        //TODO change constructor, global and event should be mutually exclusive (dont need an event for a global ticket)
        // also make a direct connection to the event instead of using get()

        /*
        Ticket ticket1 = new Ticket(eventsToBeViewed.get(0),100,true,"VIP", "Free Meal");
        Ticket ticket2 = new Ticket(eventsToBeViewed.get(1),200,true,"Super VIP","Free Meal and Drink");
        Ticket ticket3 = new Ticket(eventsToBeViewed.get(2),500,true,"WIP", "Error: 404 Not Found");
        ticketsToBeViewed.add(ticket1);
        ticketsToBeViewed.add(ticket2);
        ticketsToBeViewed.add(ticket3);
*/
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

    public ObservableList<User> getUsers() {
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
