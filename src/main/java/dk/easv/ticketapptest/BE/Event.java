package dk.easv.ticketapptest.BE;

import java.util.Date;
import java.util.List;

public class Event {
    private Date date;
    private String eventName;
    private String description;
    private String location;
    // IDK WTF TYPE SHOULD BE USED HERE?????
    // Private UNITOFSOMESORT startTime;
    // Private UNITOFSOMESORT endTime;
    private User leadEventCoordinator;
    private List<User> eventCoordinators;

}
