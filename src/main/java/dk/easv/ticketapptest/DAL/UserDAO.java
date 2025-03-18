package dk.easv.ticketapptest.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.ticketapptest.BE.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    //TODO figure out if i should return the new user, or just manually creating it in is fine.
    public void createUserDB (User user) {
        String userSQL = "INSERT INTO dbo.Users (Username, PasswordHash, Email, PhoneNumber, FirstName, LastName) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnector.getConnection(); PreparedStatement pstmt = connection.prepareStatement(userSQL)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getFirstName());
            pstmt.setString(6, user.getLastName());
            pstmt.executeUpdate();

            //TODO implement better exception handling, drop runtimeexception
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPassword(String username) {
        String passwordSQL = "SELECT password_hash FROM dbo.Users WHERE username = ?";
        try (Connection conn = DBConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(passwordSQL)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password_hash");
            }

            //TODO IMPLEMENT BETTER EXCEPTION HANDLING, NO RUNTIMEEXCEPTIONS PLS
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }
}
