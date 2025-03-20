package dk.easv.ticketapptest.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.ticketapptest.BE.Role;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.Exceptions.UsernameAlreadyExistsException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private DBConnector dbConnector;

    public UserDAO() throws IOException {
        dbConnector = new DBConnector();
    }


    public User createUserDB (User user) throws Exception {
        String userSQL = "INSERT INTO dbo.Users (Username, PasswordHash, Email, PhoneNumber, FirstName, LastName) VALUES (?, ?, ?, ?, ?, ?)";

        String getRoleSQL = "SELECT RoleID FROM Roles WHERE RoleName = ?";

        String setRoleSQL = "INSERT INTO dbo.User_Roles (UserID, RoleID) VALUES (?, ?) ";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(userSQL, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement pstmt2 = connection.prepareStatement(getRoleSQL);
             PreparedStatement pstmt3 = connection.prepareStatement(setRoleSQL)) {
            connection.setAutoCommit(false);

            //TODO handle the bubbling up of this quicker, so it doesnt even make it to the 2627 and 2601
            // race conditions are handled by those, but you shouldnt have to make it it there in "non-race" conditions.
            if (user != null && usernameExists(user.getUsername())) {
                throw new UsernameAlreadyExistsException("Username already exists");
            }
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getFirstName());
            pstmt.setString(6, user.getLastName());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();

            //TODO else throw exception. for this if statement.
            int userID = -1;
            if (rs.next()) {
                userID = rs.getInt(1);
            }

            pstmt2.setString(1, user.getRole().toString());
            ResultSet rs2 = pstmt2.executeQuery();
            //TODO else throw exception. for this if statement.
            int roleID = -1;
            if (rs2.next()) {
                roleID = rs2.getInt("RoleID");
            }

            pstmt3.setInt(1, userID);
            pstmt3.setInt(2, roleID);
            pstmt3.executeUpdate();
            connection.commit();

            return new User(user.getUsername(), user.getPassword(), user.getFirstName(),
                            user.getLastName(), user.getEmail(), user.getPhone(), user.getRole());

            //TODO implement better exception handling, drop runtimeexception

        } catch (SQLException err) {
            //ERROR CODES FOR BREAKING UNIQUE CONSTRAINTS as a workaround, since SQLIntegrityConstraintViolationException
            //is not used.
            //TODO fix this, only pass this inital message and keep bubbling it up
            if (err.getErrorCode() == 2627 || err.getErrorCode() == 2601) {
                throw new UsernameAlreadyExistsException("Username: " + user.getUsername() + "already exists", err);
            } else {
                throw new Exception(err);
            }
        }
    }

    public String getPassword(String username) {
        String passwordSQL = "SELECT PasswordHash FROM dbo.Users WHERE Username = ?";
        try (Connection conn = dbConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(passwordSQL)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("PasswordHash");
            }

            //TODO IMPLEMENT BETTER EXCEPTION HANDLING, NO RUNTIMEEXCEPTIONS PLS
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public User getUserByUsername(String username) {

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
                System.out.println("User created: " + userFromUsername);

                return userFromUsername;

            }
        } catch (SQLException e) {
            System.out.println("OOPSIE WOOPSIE IN GET USER BY USERNAME");
            e.printStackTrace();
        }
        //TODO fix exception handling here, and this return.
        return null;
    }



    public User updateUserDB(User user) throws Exception {
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
            pstmt.setInt(7, getUserId(user.getUsername()));

            pstmt.executeUpdate();

            pstmt2.setString(1, user.getRole().toString());
            ResultSet rs2 = pstmt2.executeQuery();
            int roleID = -1;
            if (rs2.next()) {
                roleID = rs2.getInt("RoleID");
            }

            pstmt3.setInt(1, roleID);
            pstmt3.setInt(2, getUserId(user.getUsername()));
            pstmt3.executeUpdate();

            connection.commit();

            return user;

        } catch (SQLException e) {
            throw new Exception("Could not update user", e);
        }
    }

    private int getUserId(String username) throws SQLException {
        String sql = "SELECT UserID FROM dbo.Users WHERE Username = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("UserID");
            } else {
                throw new SQLException("User not found: " + username);
            }
        }
    }
        // was planned to be used in events. Did not use. It is here if you think is pretty.
    public User getUserByID(int userID) throws SQLServerException {
        String sql = "SELECT * FROM dbo.Users WHERE UserID = ?";
        try(Connection connection = dbConnector.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userID);
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
                return user;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("User not found: " + userID);
    }

    public List<User> getAllCoordinators() throws SQLServerException {
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
            throw new RuntimeException(e);
        }
    }

    public boolean usernameExists(String username) throws Exception {
        String userSQL = "SELECT Username FROM dbo.Users WHERE Username = ?";
        try (Connection conn = dbConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(userSQL)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }

            //TODO better exception handling.
        } catch (SQLException e) {
            throw new Exception("SQLException in usernameExists: " + e.getMessage());
        }
        return false;
    }

}
