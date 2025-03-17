package dk.easv.ticketapptest.DAL;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Location;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class EventDAO implements IEventDataAccess {
    private DBConnector connector;

    public EventDAO() throws IOException {
    connector = new DBConnector();
    }

    public List<Event2> getAllEvents() {
        return null;
    }

    @Override
    public void updateEvent(Event2 event) throws SQLException {

    }

    public Event2 createEvent(Event2 event, Location location) throws SQLException {

        String sql = "INSERT INTO dbo.Events(Title, StartDateTime, EndDateTime, LocationID, LocationGuidance, Description, CreatedBy) " +
                "VALUES (?,?,?,?,?,?,?);";
        try (Connection conn = connector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, event.getTitle());
            ps.setDate(2, startTime);
            ps.setDate(3, endTime);
            ps.setInt(4, location.getLocationID());
            ps.setString(5, event.getLocationGuidance());
            ps.setString(6, event.getDescription());
            ps.setInt(7, 101);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                event.setEventID(rs.getInt(1));
                event.setStatus(rs.getString(9));
            }
            return event;

        } catch (SQLException e) {
            throw new SQLException("Could not create new Movie", e);
        }
    }

    public void deleteEvent(Event2 eventToBeDeleted) {
    }
}
