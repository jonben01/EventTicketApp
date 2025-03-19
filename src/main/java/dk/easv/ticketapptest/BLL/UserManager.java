package dk.easv.ticketapptest.BLL;

import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.DAL.UserDAO;

import java.io.IOException;

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
}
