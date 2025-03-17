package dk.easv.ticketapptest.GUI.Models;

import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.UserManagementLogic;

public class UserManagementModel {

    private UserManagementLogic userManagementLogic;

    public UserManagementModel() {
        userManagementLogic = new UserManagementLogic();
    }

    public void createUserDB(User user) {
        userManagementLogic.createUserDB(user);

    }
}
