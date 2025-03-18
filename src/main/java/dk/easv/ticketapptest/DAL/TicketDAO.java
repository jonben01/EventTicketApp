package dk.easv.ticketapptest.DAL;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Ticket;

import java.io.IOException;
import java.sql.*;
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

            if(ticket.getEvent() != null)
            {
                System.out.println("THIS CODE IS RUNNING");
                 sql = "INSERT INTO dbo.TicketEvent_Junction(TicketID, EventID) " + "VALUES(?, ?);";
                try (Connection connection = connector.getConnection();
                     PreparedStatement ps2 = connection.prepareStatement(sql)) {
                    ps2.setInt(1, ticket.getTicketID());
                    ps2.setInt(2, ticket.getEvent().getEventID());
                    ps2.executeUpdate();
                }
            }
            System.out.println("THE CODE SHOULD HAVE RUN BY NOW.");
            return ticket;
        } catch (SQLException e) {
            throw new SQLException("Could not create new ticket", e);
        }
    }

    @Override
    public void deleteTicket(Ticket ticket) {

    }

    @Override
    public Ticket updateTicket(Ticket ticket) {
        return null;
    }

    @Override
    public List<Ticket> getAllTickets() {
        return List.of();
    }

    @Override
    public List<Ticket> getTicketsForEvent(Event2 event) {
        return List.of();
    }

    @Override
    public void AssignTicketToEvent(Ticket ticket, Event2 event) {

    }
}
