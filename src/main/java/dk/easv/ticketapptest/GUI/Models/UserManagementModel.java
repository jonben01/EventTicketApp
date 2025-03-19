package dk.easv.ticketapptest.GUI.Models;

import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.UserManagementLogic;

public class UserManagementModel {

    private UserManagementLogic userManagementLogic;

    public UserManagementModel() throws Exception {
        userManagementLogic = new UserManagementLogic();
    }

    public User createUserDB(User user) throws Exception {
        return userManagementLogic.createUserDB(user);

    }

    public void updateUserDB(User selectedUser) {
    }
}
