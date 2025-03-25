package dk.easv.ticketapptest.BLL;

import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.Exceptions.UsernameAlreadyExistsException;
import dk.easv.ticketapptest.DAL.UserDAO;

public class UserManagementLogic {

    private UserDAO userDAO;

    public UserManagementLogic() throws Exception {
        userDAO = new UserDAO();
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
}
