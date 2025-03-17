package dk.easv.ticketapptest.BE;

import java.util.ArrayList;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;

public class Event2 {
    private LocalDate date;
    private String title;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private User leadEventCoordinator;
    private List<User> eventCoordinators;
    private String[] ticketTypes;
    private int eventID;
    private Location location;
    private String locationGuidance;
    private String status;

    public Event2(String title, Location location, String locationGuidance, LocalDate date, LocalTime startTime, LocalTime endTime,
                  String[] ticketTypes, List<User> eventCoordinators) {
        this.title = title;
        this.location = location;
        this.locationGuidance = locationGuidance;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ticketTypes = ticketTypes;
        this.eventCoordinators = eventCoordinators;

    }

    public Event2(int eventID, String title, Location location, String locationGuidance, LocalDate date, LocalTime startTime, LocalTime endTime,
                  String[] ticketTypes, List<User> eventCoordinators, String status) {
        this.eventID = eventID;
        this.title = title;
        this.location = location;
        this.locationGuidance = locationGuidance;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ticketTypes = ticketTypes;
        this.eventCoordinators = eventCoordinators;
        this.status = status;

    }

    //use DateTimeFormatter on localDate types in actual project instead of this
    //also use a date picker, to avoid edge cases where someone types something stupid.
    //should validate either way and handle those cases.

    public int getEventID() {
        return this.eventID;
    }
    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getLocationGuidance() {
        return locationGuidance;
    }

    public void setLocationGuidance(String locationGuidance) {
        this.locationGuidance = locationGuidance;
    }

    public String getStatus() {
        return "Active";
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public User getLeadEventCoordinator() {
        return leadEventCoordinator;
    }

    public void setLeadEventCoordinator(User leadEventCoordinator) {
        this.leadEventCoordinator = leadEventCoordinator;
    }

    public List<User> getEventCoordinators() {
        return eventCoordinators;
    }

    public void setEventCoordinators(List<User> eventCoordinators) {
        this.eventCoordinators = eventCoordinators;
    }

    public String[] getTicketTypes() {
        return ticketTypes;
    }

    public void setTicketTypes(String[] ticketTypes) {
        this.ticketTypes = ticketTypes;
    }
}
