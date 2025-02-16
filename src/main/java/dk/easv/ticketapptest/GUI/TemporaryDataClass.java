package dk.easv.ticketapptest.GUI;

import dk.easv.ticketapptest.BE.Event;
import dk.easv.ticketapptest.BE.Ticket;
import dk.easv.ticketapptest.BE.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



public class TemporaryDataClass {

    // SHOULD JUST CONTAIN THE OBSERVABLE LISTS OF ALL OUR MOCK DATA

    private ObservableList<Event> eventsToBeViewed;
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


        //TODO finish event BE and ticket BE and then make mock data for both.



    }

    //EVENTS SECTION
    public void createEvent(Event event) {
        eventsToBeViewed.add(event);
    }

    public ObservableList<Event> getEvents(Event event) {
        return eventsToBeViewed;
    }

    public void deleteEvent(Event event) {
        eventsToBeViewed.remove(event);
    }

    public void updateEvent(Event event, Event updatedEvent) {
        //idk if this works ngl
        eventsToBeViewed.set(eventsToBeViewed.indexOf(event), updatedEvent);

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

    public ObservableList<User> getUsers(User user) {
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
