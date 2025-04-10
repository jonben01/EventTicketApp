package dk.easv.ticketapptest.DAL;

import dk.easv.ticketapptest.BE.Role;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
import dk.easv.ticketapptest.BLL.Exceptions.UsernameAlreadyExistsException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private DBConnector dbConnector;

    public UserDAO() throws EasvTicketException {
        try {
            dbConnector = new DBConnector();
        } catch (IOException e) {
            throw new EasvTicketException("Couldn't instantiate DBConnector", e);
        }
    }


    public void createUserDB (User user) throws UsernameAlreadyExistsException, EasvTicketException {
        String userSQL = "INSERT INTO dbo.Users (Username, PasswordHash, Email, PhoneNumber, FirstName, LastName, ImagePath) VALUES (?, ?, ?, ?, ?, ?, ?)";

        String getRoleSQL = "SELECT RoleID FROM Roles WHERE RoleName = ?";

        String setRoleSQL = "INSERT INTO dbo.User_Roles (UserID, RoleID) VALUES (?, ?) ";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(userSQL, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement pstmt2 = connection.prepareStatement(getRoleSQL);
             PreparedStatement pstmt3 = connection.prepareStatement(setRoleSQL)) {
            connection.setAutoCommit(false);

            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }
            if (usernameExists(user.getUsername())) {
                throw new UsernameAlreadyExistsException("Username already exists");
            }

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getFirstName());
            pstmt.setString(6, user.getLastName());
            pstmt.setString(7, user.getImgFilePath());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();


            int userID = -1;
            if (rs.next()) {
                userID = rs.getInt(1);
            }

            pstmt2.setString(1, user.getRole().toString());
            ResultSet rs2 = pstmt2.executeQuery();

            int roleID = -1;
            if (rs2.next()) {
                roleID = rs2.getInt("RoleID");
            }

            pstmt3.setInt(1, userID);
            pstmt3.setInt(2, roleID);
            pstmt3.executeUpdate();
            try {
                connection.commit();
            } catch (SQLException e) {
                //rollback in case one of the queries somehow break
                connection.rollback();
            }

        } catch (SQLException err) {
            //ERROR CODES FOR BREAKING UNIQUE CONSTRAINTS as a workaround, since SQLIntegrityConstraintViolationException
            //is not used.
            if (err.getErrorCode() == 2627 || err.getErrorCode() == 2601) {
                throw new UsernameAlreadyExistsException("Username: " + user.getUsername() + "already exists", err);
            } else {
                throw new EasvTicketException("Error creating new user", err);
            }
        }
    }

    public String getPassword(String username) throws EasvTicketException {
        String passwordSQL = "SELECT PasswordHash FROM dbo.Users WHERE Username = ?";
        try (Connection conn = dbConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(passwordSQL)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("PasswordHash");
            }
        } catch (SQLException e) {
            throw new EasvTicketException("Failed to retrieve password", e);
        }
        return null;
    }

    public User getUserByUsername(String username) throws EasvTicketException {

        String userSQL = "SELECT U.*, R.RoleName FROM dbo.Users U " +
                        "INNER JOIN dbo.User_Roles AS UR ON U.UserID = UR.UserID " +
                        "INNER JOIN dbo.Roles AS R ON UR.RoleID = R.RoleID " +
                        "WHERE Username = ?";

        try (Connection conn = dbConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(userSQL)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User userFromUsername = new User();

                userFromUsername.setId(rs.getInt("UserID"));
                userFromUsername.setUsername(rs.getString("Username"));
                userFromUsername.setPassword(rs.getString("PasswordHash"));
                userFromUsername.setEmail(rs.getString("Email"));
                userFromUsername.setPhone(rs.getString("PhoneNumber"));
                userFromUsername.setFirstName(rs.getString("FirstName"));
                userFromUsername.setLastName(rs.getString("LastName"));

                String roleName = rs.getString("RoleName");
                userFromUsername.setRole(Role.valueOf(roleName));

                return userFromUsername;

            }
        } catch (SQLException e) {
            throw new EasvTicketException("Failed to retrieve user by username", e);
        }
        return null;
    }



    public void updateUserDB(User user, int userId) throws EasvTicketException {
        String sql = "UPDATE dbo.Users SET Username = ?, PasswordHash = ?, Email = ?, PhoneNumber = ?, FirstName = ?, LastName = ? WHERE UserID = ?";
        String getRoleSQL = "SELECT RoleID FROM Roles WHERE RoleName = ?";
        String updateRoleSQL = "UPDATE dbo.User_Roles SET RoleID = ? WHERE UserID = ?";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             PreparedStatement pstmt2 = connection.prepareStatement(getRoleSQL);
             PreparedStatement pstmt3 = connection.prepareStatement(updateRoleSQL)) {

            connection.setAutoCommit(false);

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getFirstName());
            pstmt.setString(6, user.getLastName());
            pstmt.setInt(7, userId);

            pstmt.executeUpdate();

            pstmt2.setString(1, user.getRole().toString());
            ResultSet rs2 = pstmt2.executeQuery();
            int roleID = -1;
            if (rs2.next()) {
                roleID = rs2.getInt("RoleID");
            }

            pstmt3.setInt(1, roleID);
            pstmt3.setInt(2, userId);
            pstmt3.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            throw new EasvTicketException("Could not update user", e);
        }
    }

    public List<User> getAllCoordinators() throws EasvTicketException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.* FROM dbo.User_Roles ru RIGHT JOIN dbo.Users u ON u.UserID = ru.UserID WHERE RoleID = 2";
        try(Connection conn = dbConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setId(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("PasswordHash"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("PhoneNumber"));
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new EasvTicketException("Couldn't get all Coordinators",e);
        }
    }

    public boolean usernameExists(String username) throws EasvTicketException {
        String userSQL = "SELECT Username FROM dbo.Users WHERE Username = ?";
        try (Connection conn = dbConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(userSQL)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            throw new EasvTicketException("SQLException in usernameExists: ", e);
        }
        return false;
    }

    public ObservableList<User> getUsers() throws EasvTicketException {
        String userSQL = "SELECT u.*, r.RoleName FROM dbo.Users u JOIN dbo.User_Roles ur ON u.UserID = ur.UserID JOIN dbo.Roles r ON r.RoleID = ur.RoleID; ";

        try (Connection conn = dbConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(userSQL)) {

            ResultSet rs = pstmt.executeQuery();
            ObservableList<User> users = FXCollections.observableArrayList();

            while(rs.next()) {
                User user = new User();
                user.setId(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("PasswordHash"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("PhoneNumber"));
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                String roleName = rs.getString("RoleName");
                user.setRole(Role.valueOf(roleName));
                user.setImgFilePath(rs.getString("ImagePath"));
                users.add(user);
            }
            return users;

        } catch (SQLException e) {
            throw new EasvTicketException("Couldn't get users",e);
        }
    }

    public void deleteUser(User user) throws EasvTicketException {
        String deleteSQL = "DELETE FROM dbo.Users WHERE UserID = ?";
        try (Connection conn = dbConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setInt(1, user.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new EasvTicketException("SQLException in deleteUser: " + e.getMessage());
        }
    }
}
