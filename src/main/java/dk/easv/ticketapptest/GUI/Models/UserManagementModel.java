package dk.easv.ticketapptest.GUI.Models;

import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.Exceptions.UsernameAlreadyExistsException;
import dk.easv.ticketapptest.BLL.UserManagementLogic;

public class UserManagementModel {

    private UserManagementLogic userManagementLogic;

    public UserManagementModel() throws Exception {
        userManagementLogic = new UserManagementLogic();
    }

    //TODO ADD TO AN OBSERVABLE LIST
    public User createUserDB(User user) throws Exception {

        return userManagementLogic.createUserDB(user);
    }

    public User updateUserDB(User selectedUser) throws Exception {
        return userManagementLogic.updateUserDB(selectedUser);
    }
}
