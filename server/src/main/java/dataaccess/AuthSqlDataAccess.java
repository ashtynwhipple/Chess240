package dataaccess;
import model.AuthData;
import java.sql.*;
import java.util.UUID;


public class AuthSqlDataAccess implements AuthDAO {

    public AuthSqlDataAccess() {
        configureDatabase();
    }

    public void clearAuth(){
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("CLEAR auth")) {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException | DataAccessException _) {
        }
    }

    public AuthData createAuth(String username) {
        String token = generateToken();

        try(var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement("INSERT INTO authTable (token, username) VALUES (?, ?)")){
                ps.setString(1, username);
                ps.setString(2, token);
            }
        }catch (SQLException | DataAccessException _){
        }
        return new AuthData(token, username);
    }

    public void deleteAuth(String token) {
        try(var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement("DELETE FROM authTable WHERE token = ?")){
                ps.setString(1, token);
                ps.executeUpdate();
            }
        }catch (SQLException | DataAccessException _){
        }

    }

    public AuthData getUsername(String token){
        try (var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement("SELECT username FROM authTable WHERE token = ?")) {
            ps.setString(1, token);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AuthData(token, rs.getString("username"));
                }
            }
        } catch (SQLException | DataAccessException _) {
        }
        return null;
    }

    private String generateToken(){
        return UUID.randomUUID().toString();
    }

    public boolean isEmpty(){

        try (var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement("");
            var rs = ps.executeQuery()) {

            if (rs.next()){
                return rs.getInt(1) == 0;
            }

        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void clearAll(){

        try (var conn = DatabaseManager.getConnection();
        var ps = conn.prepareStatement("DELETE FROM auth")){

            ps.executeUpdate();

        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authTable (
              `username` varchar(256) NOT NULL,
              `token` varchar(256) NOT NULL,
              PRIMARY KEY (`token`),
            )
            """
    };

    private void configureDatabase() {
        try { DatabaseManager.createDatabase(); } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
