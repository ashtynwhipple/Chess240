package dataaccess;
import model.UserData;
import java.sql.SQLException;

public class UserSqlDataAccess implements UserDAO{

    public UserSqlDataAccess() {
        configureDatabase();
    }

    public void createUser(String username, String password, String email) {
        try(var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement("INSERT INTO userTable (username, password, email) VALUES (?, ?, ?)")){
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, email);
                ps.executeUpdate();
            }
        }catch (SQLException | DataAccessException _){
        }
    }

    public UserData getUser(String username) {

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT password, email FROM userTable WHERE username = ?")) {
            ps.setString(1, username);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserData(username, rs.getString("password"), rs.getString("email"));
                }
            }
        } catch (SQLException | DataAccessException _) {
        }
        return null;
    }

    public boolean isEmpty() {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT COUNT(*) FROM userTable");
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
             var ps = conn.prepareStatement("DELETE FROM userTable")){

            ps.executeUpdate();

        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS userTable (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            );
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