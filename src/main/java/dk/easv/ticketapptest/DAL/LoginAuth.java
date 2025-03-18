package dk.easv.ticketapptest.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.ticketapptest.BLL.PBKDF2PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginAuth {

    //TODO add comments where it makes sense, simple read though, and login method is just using sha class methods

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

    public boolean login(String username, String inputPassword) {
        String storedHash = getPassword(username);
        return storedHash != null && PBKDF2PasswordUtil.verifyPassword(storedHash, inputPassword);
    }
}
