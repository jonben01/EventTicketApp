package dk.easv.ticketapptest.DAL;

import dk.easv.ticketapptest.BE.Ticket;
import dk.easv.ticketapptest.GUI.TemporaryDataClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TicketDataStore {
    private static TicketDataStore instance;
    private ObservableList<Ticket> tickets;

    private TicketDataStore() {
        tickets = FXCollections.observableArrayList();
        loadTickets();
    }

    public static TicketDataStore getInstance() {
        if (instance == null) {
            instance = new TicketDataStore();
        }
        return instance;
    }

    public ObservableList<Ticket> getTickets() {
        return tickets;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    private void loadTickets() {
        TemporaryDataClass temporaryDataClass = new TemporaryDataClass();
        tickets = temporaryDataClass.getTickets(null);
    }
}
