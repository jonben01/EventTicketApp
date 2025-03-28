package dk.easv.ticketapptest.BLL;

import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.util.PBKDF2PasswordUtil;
import dk.easv.ticketapptest.BLL.util.UserSearcher;
import dk.easv.ticketapptest.DAL.UserDAO;
import javafx.collections.ObservableList;

import java.util.List;

public class UserManagementLogic {

    private UserDAO userDAO;
    private UserSearcher userSearcher;

    public UserManagementLogic() throws Exception {
        userDAO = new UserDAO();
        userSearcher = new UserSearcher();

    }

    public User createUserDB (User user) throws Exception {

        user.setPassword(PBKDF2PasswordUtil.hashPassword(user.getPassword()));
        return userDAO.createUserDB(user);

    }

    public User updateUserDB(User user) throws Exception {
        user.setPassword(PBKDF2PasswordUtil.hashPassword(user.getPassword()));
        return userDAO.updateUserDB(user);
    }

    public void deleteUser(User user) throws Exception {
        userDAO.deleteUser(user);
    }

    public ObservableList<User> searchUser(String searchQuery) {
        List<User> searchBase = userDAO.getUsers();
        return userSearcher.searchUser(searchBase, searchQuery);
    }
}
