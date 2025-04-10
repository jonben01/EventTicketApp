package dk.easv.ticketapptest.DAL;

import dk.easv.ticketapptest.BE.Customer;
import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Ticket;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO implements ITicketDataAccess {
    private DBConnector connector;

    public TicketDAO() throws IOException {
        connector = new DBConnector();
    }

    /**
     * Adds a temporary ticket to the DB.
     * @param ticket temporary ticket without ID
     * @return created ticket with the new ID.
     * @throws EasvTicketException
     */
    @Override
    public Ticket createTicket(Ticket ticket) throws EasvTicketException {
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
            throw new EasvTicketException("Could not create new ticket", e);
        }
    }

    /**
     * Deletes a ticket from the DB.
     * @param ticket The ticket that needs to be removed.
     * @throws EasvTicketException
     */
    @Override
    public void deleteTicket(Ticket ticket) throws EasvTicketException {
        String sql = "DELETE FROM dbo.Tickets WHERE TicketID = ?;";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticket.getTicketID());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new EasvTicketException("Could not delete ticket", e);
        }
    }

    /**
     * Updates a specific tickets information.,
     * @param ticket the ticket that needs to be updated.
     * @return return the ticket with the new information.
     * @throws EasvTicketException
     */
    @Override
    public Ticket updateTicket(Ticket ticket) throws EasvTicketException {
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
            throw new EasvTicketException("Could not update ticket", e);
        }


    }

    /**
     * Get alls available tickets saved in the application.
     * Currently no usages. Could be used in future development
     * to reuse tickets for different events without adding it to global
     * @return List of all the tickets saved in DB.
     * @throws EasvTicketException
     */
    @Override
    public List<Ticket> getAllTickets() throws EasvTicketException {
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
            throw new EasvTicketException("Could not get all tickets", e);
        }
        return allTickets;
    }

    /**
     * Get all the tickets assigned to a specific event.
     * @param event the event in question
     * @return list of all the tickets that are assigned to the event.
     * @throws EasvTicketException
     */
    @Override
    public List<Ticket> getTicketsForEvent(Event2 event) throws EasvTicketException {
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
            throw new EasvTicketException("Could not get tickets for event" , e);
        }
    }

    /**
     * Assigning a ticket to an event.
     * @param ticket the ticket that should be added.
     * @param event the event in question.
     * @throws EasvTicketException
     */
    @Override
    public void AssignTicketToEvent(Ticket ticket, Event2 event) throws EasvTicketException{
        String sql = "INSERT INTO dbo.TicketEvent_Junction(TicketID, EventID) " + "VALUES(?, ?);";
        try (Connection connection = connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ticket.getTicketID());
            ps.setInt(2, event.getEventID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EasvTicketException("Could not assign ticket to event", e);
        }
    }

    /**
     * Saves the created ticket + assigned customer info.
     * @param generatedID The ID created when sending the ticket. (Should be hashed)
     * @param ticket Ticket type, that the ID is connected to.
     * @param event The event that this ticket is connected to.
     * @param customer The customer information that this ticket is connected to.
     * @throws EasvTicketException
     */
    public void savePrintedTicket(String generatedID, Ticket ticket, Event2 event, Customer customer) throws EasvTicketException {
        String sql = "INSERT INTO dbo.Printed_Tickets(GeneratedTicket, TicketID, EventID, FirstName, LastName, Email, PhoneNumber) " + "VALUES(?, ?, ?, ?, ?, ?, ?);";
        try (Connection conn = connector.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, generatedID);
            ps.setInt(2, ticket.getTicketID());
            ps.setInt(3, event.getEventID());
            ps.setString(4, customer.getFirstName());
            ps.setString(5, customer.getLastName());
            ps.setString(6, customer.getEmail());
            ps.setInt(7, customer.getPhoneNumber());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EasvTicketException("Could not save printed ticket information", e);
        }
    }
}
