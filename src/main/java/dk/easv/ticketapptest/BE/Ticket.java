package dk.easv.ticketapptest.BE;

public class Ticket {

    //TODO - a list of tickets should be iterated through when looking at events, where the event matches the selected
    // event, the ticket should be shown.
    // Should probably also have a "GLOBAL" mode, for tickets that can be used for all events - like STANDARD or FREE BEER
    private Event event;
    private final boolean GLOBAL;
    private String ticketName;
    private String description;
    //not sure if double is optimal
    private double price;

    //maybe its just better to not do the GLOBAL, and instead just have an if statement IF (EVENT == GLOBAL) and do it like that instead

    public Ticket(Event event, double price, boolean GLOBAL, String ticketName, String description) {
        this.event = event;
        this.price = price;
        this.GLOBAL = GLOBAL;
        this.ticketName = ticketName;
        this.description = description;
    }
}
