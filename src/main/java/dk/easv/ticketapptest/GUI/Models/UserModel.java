package dk.easv.ticketapptest.GUI.Models;

import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
import dk.easv.ticketapptest.BLL.util.PBKDF2PasswordUtil;
import dk.easv.ticketapptest.BLL.UserManager;
import javafx.collections.ObservableList;

import java.util.List;

public class UserModel {

    private UserManager userManager;

    public UserModel() throws EasvTicketException {
        userManager = new UserManager();


    }

    public String getPassword(String username) throws EasvTicketException {
        return userManager.getPassword(username);

    }

    public boolean verifyPassword(String password, String hashedPassword) throws EasvTicketException {
        return PBKDF2PasswordUtil.verifyPassword(password, hashedPassword );
    }

    public User getUserByUsername(String username) throws EasvTicketException {
        return userManager.getUserByUsername(username);
    }

    public String hashPassword(String inputPassword) throws Exception {
        return PBKDF2PasswordUtil.hashPassword(inputPassword);
    }

    public List<User> getAllCoordinators() throws EasvTicketException {
        return userManager.getAllCoordinators();
    }

    public ObservableList<User> getUsers() throws EasvTicketException {
        return userManager.getUsers();
    }

    public void editRole(User user) throws EasvTicketException {
        userManager.editRole(user);
    }
}
