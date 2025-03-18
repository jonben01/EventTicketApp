package dk.easv.ticketapptest.BLL;

import dk.easv.ticketapptest.DAL.UserDAO;

public class TempLoginClass {

    private UserDAO userDAO;

    public boolean login(String username, String inputPassword) {
        String storedHash = userDAO.getPassword(username);
        return storedHash != null && PBKDF2PasswordUtil.verifyPassword(storedHash, inputPassword);
    }
}
