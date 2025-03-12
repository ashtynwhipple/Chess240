package dataaccess;

import model.AuthData;
import java.sql.*;
import java.util.UUID;

public class AuthSqlDataAccess extends BaseSqlDataAccess implements AuthDAO {

    private static final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authTable (
              `username` varchar(256) NOT NULL,
              `token` varchar(256) NOT NULL,
              PRIMARY KEY (`token`)
            );
            """
    };

    public AuthSqlDataAccess() {
        super(createStatements);
    }

    public AuthData createAuth(String username) {
        String token = generateToken();
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("INSERT INTO authTable (token, username) VALUES (?, ?)")) {
            ps.setString(1, token);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return new AuthData(token, username);
    }

    public void deleteAuth(String token) {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("DELETE FROM authTable WHERE token = ?")) {
            ps.setString(1, token);
            ps.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthData getUsername(String token) {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT username FROM authTable WHERE token = ?")) {
            ps.setString(1, token);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AuthData(token, rs.getString("username"));
                }
                return null;
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public boolean isEmpty() {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT COUNT(*) FROM authTable");
             var rs = ps.executeQuery()) {
            return rs.next() && rs.getInt(1) == 0;
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearAll() {
        super.clearAll("authTable");
    }
}
