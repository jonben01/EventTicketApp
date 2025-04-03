package dk.easv.ticketapptest.GUI.Models;
//project imports
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
import dk.easv.ticketapptest.BLL.Exceptions.UsernameAlreadyExistsException;
import dk.easv.ticketapptest.BLL.UserManagementLogic;
//java imports
import javafx.collections.ObservableList;

public class UserManagementModel {

    private UserManagementLogic userManagementLogic;

    public UserManagementModel() throws EasvTicketException {
        userManagementLogic = new UserManagementLogic();
    }

    public void createUserDB(User user) throws EasvTicketException, UsernameAlreadyExistsException {
        userManagementLogic.createUserDB(user);
    }

    public void updateUserDB(User selectedUser, int userId) throws EasvTicketException {
        userManagementLogic.updateUserDB(selectedUser, userId);
    }

    public void deleteUser(User user) throws EasvTicketException {
        userManagementLogic.deleteUser(user);
    }

    public ObservableList<User> searchUser(String searchQuery) throws EasvTicketException {
        return userManagementLogic.searchUser(searchQuery);
    }

}
