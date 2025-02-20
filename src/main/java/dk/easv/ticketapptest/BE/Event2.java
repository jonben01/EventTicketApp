package dk.easv.ticketapptest.BE;

import java.util.ArrayList;
import java.util.List;

public class Event2 {
    private String date;
    private String title;
    private String description;
    private String location;
    private String startTime;
    private String endTime;
    private User leadEventCoordinator;
    private List<User> eventCoordinators;
    private String[] ticketTypes;

    public Event2(String title, String location, String date, String startTime, String endTime,
                  String[] ticketTypes, List<User> eventCoordinators) {
        this.title = title;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ticketTypes = ticketTypes;
        this.eventCoordinators = eventCoordinators;

    }

    //use DateTimeFormatter on localDate types in actual project instead of this
    //also use a date picker, to avoid edge cases where someone types something stupid.
    //should validate either way and handle those cases.
    public String getDateTime() {
        String date = this.date;
        String[] dateParts = date.split(" ");

        String month = dateParts[0].substring(0, 3);
        StringBuilder sb = new StringBuilder();
        sb.append(month).append(" ").append(dateParts[1]).append(" ").append(dateParts[2]).append(" - ").append(startTime);
        return sb.toString();
    }

    public String getStatus() {
        return "Active";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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
