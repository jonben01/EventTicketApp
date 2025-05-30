package dk.easv.ticketapptest.BE;

import com.google.api.client.util.DateTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;

public class Event2 {
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private User leadEventCoordinator;
    private List<User> eventCoordinators;
    private List<Ticket> ticketTypes;
    private int eventID;
    private Location location;
    private String locationGuidance;
    private String status;
    private LocalDateTime startDateTime;


    public Event2(String title, Location location, String description, String locationGuidance, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime,
                  List<Ticket> ticketTypes, List<User> eventCoordinators) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.locationGuidance = locationGuidance;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ticketTypes = ticketTypes;
        this.eventCoordinators = eventCoordinators;

    }

    public Event2(int eventID, String title, Location location, String Description, String locationGuidance, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime,
                  List<Ticket> ticketTypes, List<User> eventCoordinators, String status) {
        this.eventID = eventID;
        this.title = title;
        this.location = location;
        this.description = Description;
        this.locationGuidance = locationGuidance;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ticketTypes = ticketTypes;
        this.eventCoordinators = eventCoordinators;
        this.status = status;

    }

    public Event2(){
        eventCoordinators = new ArrayList<>();
    }

    public <E> Event2(String text, Location location, String text1, String text2, LocalDate value, LocalDate value1, LocalTime parse, LocalTime parse1, ArrayList<Ticket> tickets, ArrayList<E> es) {
    }

    public EventStatus getEventStatus() {
        LocalDateTime eventEnd = null;
        if (endDate != null && endTime != null) {
            eventEnd = LocalDateTime.of(endDate, endTime);
        }

        LocalDateTime eventStart = LocalDateTime.of(startDate, startTime);
        LocalDateTime now = LocalDateTime.now();
        
        if (now.isBefore(eventStart)) {
            return EventStatus.UPCOMING;
        } else if (eventEnd != null && !now.isAfter(eventEnd)) {
            return EventStatus.ONGOING;
        } else {
            return EventStatus.COMPLETED;
        }
    }


    public LocalDateTime getStartDateTime() {
        return startDateTime = LocalDateTime.of(startDate, startTime);
    }

    public void addCoordinator(User user){
        eventCoordinators.add(user);
    }

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public List<Ticket> getTicketTypes() {
        return ticketTypes;
    }

    public void setTicketTypes(List<Ticket> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }
}
