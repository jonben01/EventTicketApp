package dk.easv.ticketapptest.GUI.Models;

import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
import dk.easv.ticketapptest.BLL.UserManagementLogic;
import javafx.collections.ObservableList;

public class UserManagementModel {

    private UserManagementLogic userManagementLogic;

    public UserManagementModel() throws Exception {
        userManagementLogic = new UserManagementLogic();
    }

    //TODO ADD TO AN OBSERVABLE LIST
    public User createUserDB(User user) throws Exception {

        return userManagementLogic.createUserDB(user);
    }

    public User updateUserDB(User selectedUser, int userId) throws Exception {
        return userManagementLogic.updateUserDB(selectedUser, userId);
    }

    public void deleteUser(User user) throws Exception {
        userManagementLogic.deleteUser(user);
    }

    public ObservableList<User> searchUser(String searchQuery) throws EasvTicketException {
        return userManagementLogic.searchUser(searchQuery);
    }

}
