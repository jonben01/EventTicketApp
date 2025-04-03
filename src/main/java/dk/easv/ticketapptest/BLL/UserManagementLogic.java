package dk.easv.ticketapptest.BLL;

import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
import dk.easv.ticketapptest.BLL.Exceptions.UsernameAlreadyExistsException;
import dk.easv.ticketapptest.BLL.util.PBKDF2PasswordUtil;
import dk.easv.ticketapptest.BLL.util.UserSearcher;
import dk.easv.ticketapptest.DAL.UserDAO;
import javafx.collections.ObservableList;

import java.util.List;

public class UserManagementLogic {

    private UserDAO userDAO;
    private UserSearcher userSearcher;

    public UserManagementLogic() throws EasvTicketException {
        userDAO = new UserDAO();
        userSearcher = new UserSearcher();

    }

    public void createUserDB (User user) throws EasvTicketException, UsernameAlreadyExistsException {

        user.setPassword(PBKDF2PasswordUtil.hashPassword(user.getPassword()));
        userDAO.createUserDB(user);

    }

    public void updateUserDB(User user, int userId) throws EasvTicketException {
        user.setPassword(PBKDF2PasswordUtil.hashPassword(user.getPassword()));
        userDAO.updateUserDB(user, userId);
    }

    public void deleteUser(User user) throws EasvTicketException {
        userDAO.deleteUser(user);
    }

    public ObservableList<User> searchUser(String searchQuery) throws EasvTicketException {
        List<User> searchBase = userDAO.getUsers();
        return userSearcher.searchUser(searchBase, searchQuery);
    }
}
