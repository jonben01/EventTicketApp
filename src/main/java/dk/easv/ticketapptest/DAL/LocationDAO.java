package dk.easv.ticketapptest.DAL;

import dk.easv.ticketapptest.BE.Location;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO implements ILocationDataAccess {
    private DBConnector connector;

    public LocationDAO() throws IOException {
        connector = new DBConnector();
    }

    @Override
    public Location createLocation(Location location) throws SQLException {
        String sql = "INSERT INTO dbo.Locations(Address, City, PostalCode) " + "VALUES(?,?,?);";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, location.getAddress());
            ps.setString(2, location.getCity());
            ps.setInt(3, location.getPostalCode());
            ps.executeUpdate();
            return location;
        } catch (SQLException e) {
            throw new SQLException("Could not create new location", e);
        }
    }

    @Override
    public void updateLocation(Location location) throws SQLException {

        //TODO implement this, if even necessary.

    }

    @Override
    public ArrayList<Location> getAllLocations () throws SQLException {
        ArrayList<Location> allLocations = new ArrayList<>();
        try (Connection conn = connector.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT LocationID, Address, City, PostalCode FROM Category";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("LocationID");
                String address = rs.getString("Address");
                String city = rs.getString("City");
                int postalCode = rs.getInt("PostalCode");
                allLocations.add(new Location(id, address, city, postalCode));
            }
            return allLocations;
        } catch (SQLException e) {
            throw new SQLException("Could not get all categories from database", e);
        }
    }

    @Override
    public void deleteLocation(Location location) throws SQLException {
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM dbo.Locations WHERE Id = ?")) {
            ps.setInt(1, location.getLocationID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Could not delete location: " + location.getAddress(), e);
        }
    }
}
