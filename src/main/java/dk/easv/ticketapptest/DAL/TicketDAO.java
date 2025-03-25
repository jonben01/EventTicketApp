package dk.easv.ticketapptest.DAL;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Ticket;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO implements ITicketDataAccess {
    private DBConnector connector;

    public TicketDAO() throws IOException {
        connector = new DBConnector();
    }

    @Override
    public Ticket createTicket(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO dbo.Tickets(Title, Description, Price, Global) VALUES(?,?,?,?);";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ticket.getTicketName());
            ps.setString(2, ticket.getDescription());
            ps.setFloat(3, (float) ticket.getPrice());
            ps.setBoolean(4, ticket.isGLOBAL());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                ticket.setTicketID(rs.getInt(1)); // Set the generated ID
            }

            if(ticket.getEvent() != null && ticket.isGLOBAL() == false)
            {
                 sql = "INSERT INTO dbo.TicketEvent_Junction(TicketID, EventID) " + "VALUES(?, ?);";
                try (Connection connection = connector.getConnection();
                     PreparedStatement ps2 = connection.prepareStatement(sql)) {
                    ps2.setInt(1, ticket.getTicketID());
                    ps2.setInt(2, ticket.getEvent().getEventID());
                    ps2.executeUpdate();
                }
            }
            return ticket;
        } catch (SQLException e) {
            throw new SQLException("Could not create new ticket", e);
        }
    }

    @Override
    public void deleteTicket(Ticket ticket) throws SQLException {
        String sql = "DELETE FROM dbo.Tickets WHERE TicketID = ?;";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticket.getTicketID());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new SQLException("Could not delete ticket", e);
        }
    }

    @Override
    public Ticket updateTicket(Ticket ticket) throws SQLException {
        String sql = "UPDATE dbo.Tickets SET Title = ?, Description = ?, Price = ?, Global = ? WHERE TicketID = ?;";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, ticket.getTicketName());
            ps.setString(2, ticket.getDescription());
            ps.setFloat(3, (float) ticket.getPrice());
            ps.setBoolean(4, ticket.isGLOBAL());
            ps.setInt(5, ticket.getTicketID());
            ps.executeUpdate();
            return ticket;
        } catch (SQLException e) {
            throw new SQLException("Could not update ticket", e);
        }


    }

    @Override
    public List<Ticket> getAllTickets() throws SQLException {
        List<Ticket> allTickets = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Tickets;";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             ResultSet rs = ps.executeQuery(sql);
             while (rs.next()) {
                 int ticketID = rs.getInt("TicketID");
                 String title = rs.getString("Title");
                 String description = rs.getString("Description");
                 double price = rs.getDouble("Price");
                 boolean GLOBAL = rs.getBoolean("Global");
                 Ticket ticket = new Ticket(ticketID, price, GLOBAL, title, description);
                 allTickets.add(ticket);
             }
            }catch (SQLException e) {
            throw new SQLException("Could not get all tickets", e);
        }
        return allTickets;
    }

    @Override
    public List<Ticket> getTicketsForEvent(Event2 event) throws SQLException {
        List<Ticket> eventtickets = new ArrayList<>();
        String sql = "SELECT t.* FROM dbo.Tickets t JOIN dbo.TicketEvent_Junction te ON t.TicketID = te.TicketID WHERE te.EventID = ?;";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, event.getEventID());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int ticketID = rs.getInt("TicketID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                double price = rs.getDouble("Price");
                boolean GLOBAL = rs.getBoolean("Global");
                Ticket ticket = new Ticket(ticketID, price, GLOBAL, title, description);
                eventtickets.add(ticket);
            }

         sql = "Select t.* FROM dbo.Tickets t WHERE Global = 1";
        try(Connection conn2 = connector.getConnection();
        PreparedStatement ps2 = conn2.prepareStatement(sql)) {
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                int ticketID = rs2.getInt("TicketID");
                String title = rs2.getString("Title");
                String description = rs2.getString("Description");
                double price = rs2.getDouble("Price");
                boolean GLOBAL = rs2.getBoolean("Global");
                Ticket ticket = new Ticket(ticketID, price, GLOBAL, title, description);
                eventtickets.add(ticket);
            }
        }
        return eventtickets;
        } catch (SQLException e) {
            throw new SQLException("Could not get tickets for event" , e);
        }
    }

    @Override
    public void AssignTicketToEvent(Ticket ticket, Event2 event) throws SQLException{
        String sql = "INSERT INTO dbo.TicketEvent_Junction(TicketID, EventID) " + "VALUES(?, ?);";
        try (Connection connection = connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ticket.getTicketID());
            ps.setInt(2, event.getEventID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Could not assign ticket to event", e);
        }


    }
}
