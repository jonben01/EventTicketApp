package dk.easv.ticketapptest.DAL;

import dk.easv.ticketapptest.BE.Location;

import java.sql.SQLException;
import java.util.List;

public interface ILocationDataAccess {

    Location createLocation (Location location) throws SQLException;

    void updateLocation (Location location) throws SQLException;

    List<Location> getAllLocations() throws SQLException;

    void deleteLocation (Location location) throws SQLException;

}
