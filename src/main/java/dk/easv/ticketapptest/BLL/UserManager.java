package dk.easv.ticketapptest.BLL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.DAL.UserDAO;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class UserManager {

    private UserDAO userDAO;

    public UserManager() throws Exception {
        userDAO = new UserDAO();
    }


    public String getPassword(String username) {
        return userDAO.getPassword(username);
    }

    public User getUserByUsername (String username) {
        return userDAO.getUserByUsername(username);
    }

    public List<User> getAllCoordinators() throws Exception {
        return userDAO.getAllCoordinators();
    }



    public ObservableList<User> getUsers() {
        return userDAO.getUsers();
    }

    public void editRole(User user) throws Exception {
        userDAO.editRole(user);
    }
}
