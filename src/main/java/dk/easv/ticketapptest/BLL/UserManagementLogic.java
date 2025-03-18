package dk.easv.ticketapptest.BLL;

import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.DAL.UserDAO;

public class UserManagementLogic {

    private UserDAO userDAO;

    public UserManagementLogic() throws Exception {
        userDAO = new UserDAO();
    }

    public void createUserDB (User user) {
        userDAO.createUserDB(user);
    }
}
