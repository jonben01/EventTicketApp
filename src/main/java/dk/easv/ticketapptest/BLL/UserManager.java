package dk.easv.ticketapptest.BLL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
import dk.easv.ticketapptest.DAL.UserDAO;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class UserManager {

    private UserDAO userDAO;

    public UserManager() throws EasvTicketException {
        userDAO = new UserDAO();
    }


    public String getPassword(String username) throws EasvTicketException {
        return userDAO.getPassword(username);
    }

    public User getUserByUsername (String username) throws EasvTicketException {
        return userDAO.getUserByUsername(username);
    }

    public List<User> getAllCoordinators() throws EasvTicketException {
        return userDAO.getAllCoordinators();
    }



    public ObservableList<User> getUsers() throws EasvTicketException {
        return userDAO.getUsers();
    }

}
