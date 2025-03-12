package dataaccess;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class UserSqlDataAccess extends BaseSqlDataAccess implements UserDAO{

    private static final String[] CREATE_STATEMENTS = {
            """
            CREATE TABLE IF NOT EXISTS userTable (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            );
            """
    };

    public UserSqlDataAccess() {
        super(CREATE_STATEMENTS);
    }

    public void createUser(String username, String password, String email) {
        try(var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement("INSERT INTO userTable (username, password, email) VALUES (?, ?, ?)")){
                ps.setString(1, username);
                ps.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
                ps.setString(3, email);
                ps.executeUpdate();
            }
        }catch (SQLException | DataAccessException e){
            throw new RuntimeException(e);
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
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
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

    public boolean verifyUser(String username, String providedClearTextPassword) {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT password FROM userTable WHERE username = ?")) {
            ps.setString(1, username);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password");
                    return BCrypt.checkpw(providedClearTextPassword, storedHashedPassword);
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("Error verifying user", e);
        }
        return false;
    }


}