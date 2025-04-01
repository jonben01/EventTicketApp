package dk.easv.ticketapptest.DAL;

import dk.easv.ticketapptest.BE.Location;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;

import java.sql.SQLException;
import java.util.List;

public interface ILocationDataAccess {

    Location createLocation (Location location) throws EasvTicketException;

    void updateLocation (Location location) throws EasvTicketException;

    List<Location> getAllLocations() throws EasvTicketException;

    void deleteLocation (Location location) throws EasvTicketException;

}
