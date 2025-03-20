package dk.easv.ticketapptest.GUI.Models;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.PBKDF2PasswordUtil;
import dk.easv.ticketapptest.BLL.UserManager;
import dk.easv.ticketapptest.DAL.UserDAO;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class UserModel {

    private UserManager userManager;

    public UserModel() throws Exception {
        userManager = new UserManager();


    }

    public String getPassword(String username) {
        return userManager.getPassword(username);

    }

    public boolean verifyPassword(String password, String hashedPassword) throws Exception {
        return PBKDF2PasswordUtil.verifyPassword(password, hashedPassword );
    }

    public User getUserByUsername(String username) {
        return userManager.getUserByUsername(username);
    }

    public String hashPassword(String inputPassword) throws Exception {
        return PBKDF2PasswordUtil.hashPassword(inputPassword);
    }

    public List<User> getAllCoordinators() throws SQLServerException {
        return userManager.getAllCoordinators();
    }

    public ObservableList<User> getUsers() {
        return userManager.getUsers();
    }
}
