package dk.easv.ticketapptest.BE;

public class Ticket {

    private Event2 event;
    private boolean GLOBAL;
    private String ticketName;
    private String description;
    private double price;
    private int ticketID;

    public Ticket(Event2 event2, double price, boolean GLOBAL, String ticketName, String description) {
        this.event = event2; //event was taken
        this.price = price;
        this.GLOBAL = GLOBAL;
        this.ticketName = ticketName;
        this.description = description;
    }

    public Ticket(int ticketID, double price, boolean GLOBAL, String ticketName, String description) {
        this.ticketID = ticketID;
        this.price = price;
        this.GLOBAL = GLOBAL;
        this.ticketName = ticketName;
        this.description = description;
    }

    public Ticket()
    {
    }

    public Event2 getEvent(){ return this.event; }

    public void setEvent(Event2 event){ this.event = event; }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public boolean isGLOBAL() {
        return GLOBAL;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public void setGLOBAL(boolean global) {this.GLOBAL = global;}
    }

