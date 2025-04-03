package dk.easv.ticketapptest.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Location;
import dk.easv.ticketapptest.BE.Ticket;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDAO implements IEventDataAccess {
    private DBConnector connector;
    List<User> tempData;
    UserDAO userDAO;
    TicketDAO ticketDAO;

    //todo dont throw io
    public EventDAO() throws IOException {
        ticketDAO = new TicketDAO();
        connector = new DBConnector();
        tempData = new ArrayList<>();

        try {
            userDAO = new UserDAO();
            //TODO this should be applied to all of the other objects, but also DO NOT THROW RUNTIME - cba changing this right now
        } catch (EasvTicketException e) {
            throw new RuntimeException();
        }

   // tempData.add(tempDataClass.getUsers().get(0));
    }

    public List<Event2> getAllEvents() throws EasvTicketException {
        List<Event2> events = new ArrayList<>();
        String sql = "SELECT e.EventID, e.Title, e.StartTime, e.EndTime, e.LocationID, e.LocationGuidance, e.Description, e.Status, e.StartDate, e.EndDate, e.CreatedBy, " +
                "l.Address, l.City, l.PostalCode, " +
                "u.UserID, u.FirstName, u.LastName, u.Email, u.PasswordHash, " +
                "t.TicketID, t.Title AS TicketTitle, t.Description AS TicketDescription, t.Price, t.Global " +
                "FROM Events e " +
                "JOIN Locations l ON e.LocationID = l.LocationID " +
                "LEFT JOIN Event_Users eu ON e.EventID = eu.EventID " +
                "LEFT JOIN Users u ON eu.UserID = u.UserID " +
                "LEFT JOIN TicketEvent_Junction te ON e.EventID = te.EventID " +
                "LEFT JOIN Tickets t ON te.TicketID = t.TicketID;";

        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            Map<Integer, Event2> eventMap = new HashMap<>(); // Maps event ID to Event2 object

            while (rs.next()) {
                int eventId = rs.getInt("EventID");

                // If the event doesn't exist in the map, create it
                Event2 event = eventMap.get(eventId);
                if (event == null) {
                    int locationID = rs.getInt("LocationID");
                    String address = rs.getString("Address");
                    String city = rs.getString("City");
                    int postalCode = rs.getInt("PostalCode");
                    Location location = new Location(locationID, address, city, postalCode);

                    String title = rs.getString("Title");
                    LocalTime startTime = rs.getTime("StartTime").toLocalTime();
                    LocalTime endTime = rs.getTime("EndTime").toLocalTime();
                    String locationGuidance = rs.getString("LocationGuidance");
                    String description = rs.getString("Description");
                    String status = rs.getString("Status");
                    LocalDate startDate = rs.getDate("StartDate").toLocalDate();
                    LocalDate endDate = rs.getDate("EndDate").toLocalDate();

                    event = new Event2(eventId, title, location, description, locationGuidance, startDate, endDate, startTime, endTime, new ArrayList<>(), new ArrayList<>(), status);
                    eventMap.put(eventId, event);
                }

                // Add user if available
                int userId = rs.getInt("UserID");
                if (userId > 0) {
                    User user = new User();
                    user.setId(userId);
                    user.setFirstName(rs.getString("FirstName"));
                    user.setLastName(rs.getString("LastName"));
                    user.setEmail(rs.getString("Email"));
                    user.setPassword(rs.getString("PasswordHash"));
                    event.getEventCoordinators().add(user);
                }

                int ticketId = rs.getInt("TicketID");
                if (event.getTicketTypes().stream().noneMatch(t -> t.getTicketID() == ticketId)) {
                    Ticket ticket = new Ticket();
                    ticket.setTicketID(ticketId);
                    ticket.setTicketName(rs.getString("TicketTitle"));
                    ticket.setDescription(rs.getString("TicketDescription"));
                    ticket.setPrice(rs.getDouble("Price"));
                    ticket.setGLOBAL(rs.getBoolean("Global"));
                    event.getTicketTypes().add(ticket);
                }
            }

            events.addAll(eventMap.values());
            return events;

        } catch (SQLException ex) {
            throw new EasvTicketException("Could not get events from database", ex);
        }
    }



    public List<User> getAllUsersForEvent(int id) throws EasvTicketException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Event_Users d LEFT JOIN dbo.Users u ON u.UserID = d.UserID WHERE d.EventID = ?;";
        try(Connection conn = connector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("UserID"));
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                user.setEmail(rs.getString("Email"));
                user.setPassword(rs.getString("PasswordHash"));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new EasvTicketException("Could not get users from database", e);
        }
    }

    @Override
    public void updateEvent(Event2 event, Location location ) throws EasvTicketException {

        String sql = "UPDATE dbo.Events SET Title = ?, StartTime = ?, EndTime = ?, LocationID = ?, LocationGuidance = ?, Description = ?, StartDate = ?, EndDate = ? WHERE EventID = ?";
        try (Connection conn = connector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getTitle());
            stmt.setTime(2, Time.valueOf(event.getStartTime()));
            stmt.setTime(3, Time.valueOf(event.getEndTime()));
            stmt.setInt(4, location.getLocationID());
            stmt.setString(5, event.getLocationGuidance());
            stmt.setString(6, event.getDescription());
            stmt.setDate(7, Date.valueOf(event.getStartDate()));
            stmt.setDate(8, Date.valueOf(event.getEndDate()));
            stmt.setInt(9, event.getEventID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new EasvTicketException("Could not update event", e);
        }

    }

    public Event2 createEvent(Event2 event, Location location) throws EasvTicketException {

        String sql = "INSERT INTO dbo.Events(Title, StartTime, EndTime, LocationID, LocationGuidance, Description, CreatedBy, StartDate, EndDate) " +
                "VALUES (?,?,?,?,?,?,?,?,?);";
        try (Connection conn = connector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, event.getTitle());
            ps.setTime(2, Time.valueOf(event.getStartTime()));
            ps.setTime(3, Time.valueOf(event.getEndTime()));
            ps.setInt(4, location.getLocationID());
            ps.setString(5, event.getLocationGuidance());
            ps.setString(6, event.getDescription());
            ps.setInt(7, event.getEventCoordinators().get(0).getId());
            ps.setDate(8, Date.valueOf(event.getStartDate()));
            ps.setDate(9, Date.valueOf(event.getEndDate()));
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                event.setEventID(rs.getInt(1));
            }
            addToEventUsers(event);
            return event;

        } catch (SQLException e) {
            throw new EasvTicketException("Could not create new Event", e);
        }
    }

    public void addToEventUsers(Event2 event) throws EasvTicketException {
        String sql = "INSERT INTO dbo.Event_Users(EventID, UserID) VALUES (?,?)";
        try(Connection conn = connector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, event.getEventID());
            ps.setInt(2, event.getEventCoordinators().get(0).getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new EasvTicketException("Could not add user to event.");
        }
    }

    public void deleteEvent(Event2 eventToBeDeleted) throws EasvTicketException {
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM dbo.Events WHERE EventID = ?")) {
            ps.setInt(1, eventToBeDeleted.getEventID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EasvTicketException("Could not delete event: " + eventToBeDeleted.getTitle(), e);
        }
    }

    public void removeFromEventUsers(Event2 event) throws EasvTicketException {
        String sql = "DELETE FROM dbo.Event_Users WHERE EventID = ? AND UserID = ?";
        try(Connection conn = connector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, event.getEventID());
            ps.setInt(2, event.getEventCoordinators().get(0).getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new EasvTicketException("Could not remove user from event.", e);
        }
    }

    public List<Event2> getAllEventsForUser(int userid) throws EasvTicketException {
        List<Event2> events = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Event_Users d LEFT JOIN dbo.Events u ON u.EventID = d.EventID LEFT JOIN dbo.Locations l ON l.LocationID = u.LocationID WHERE d.UserID = ?;";
        try(Connection conn = connector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, userid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                List<User> users = new ArrayList<>();
                List<Ticket> tickets = new ArrayList<>();
                int locationID = rs.getInt("LocationID");
                String address = rs.getString("Address");
                String city = rs.getString("City");
                int postalCode = rs.getInt("PostalCode");
                Location location = new Location(locationID, address, city, postalCode);

                int id = rs.getInt("EventID");
                String title = rs.getString("Title");
                LocalTime startTime = rs.getTime("StartTime").toLocalTime();
                LocalTime endTime = rs.getTime("EndTime").toLocalTime();
                String locationGuidance = rs.getString("LocationGuidance");
                String description = rs.getString("Description");
                String status = rs.getString("Status");
                LocalDate startDate = rs.getDate("StartDate").toLocalDate();
                LocalDate endDate = rs.getDate("EndDate").toLocalDate();
                int userID = rs.getInt("CreatedBy");

                users.addAll(getAllUsersForEvent(id));
                Event2 tempEvent = new Event2();
                tempEvent.setEventID(id);
                tickets.addAll(ticketDAO.getTicketsForEvent(tempEvent));

                Event2 event = new Event2(id, title, location, description, locationGuidance, startDate, endDate, startTime, endTime, tickets, users, status);
                events.add(event);
            }
            return events;
        } catch (SQLException ex) {
            throw new EasvTicketException("Could not get events for user from database", ex);
        }
    }
}
