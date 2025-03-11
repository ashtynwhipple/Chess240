package dataaccess;
import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class GameSqlDataAccess implements GameDAO{

    public GameSqlDataAccess() {
        configureDatabase();
    }

    public void clearGames(){
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("CLEAR gameTable")) {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException | DataAccessException _) {
        }
    }

    public void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){
        try(var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement("INSERT INTO gameTable (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)")){
                ps.setInt(1, gameID);
                ps.setString(2, whiteUsername);
                ps.setString(3, blackUsername);
                ps.setString(4, gameName);
                ps.setObject(5, game);
            }
        }catch (SQLException | DataAccessException _){
        }
    }

    public GameData getGame(int gameID){
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT whiteUsername, blackUsername, gameName, game FROM gameTable Where gameID = ?")) {
            ps.setInt(1, gameID);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new GameData(gameID, rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), (ChessGame) rs.getObject("game"));
                }
            }
        } catch (SQLException | DataAccessException _) {
        }
        return null;

    }


    public boolean isEmpty(){
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT COUNT(*) FROM gameTable");
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
             var ps = conn.prepareStatement("DELETE FROM gameTable")){

            ps.executeUpdate();

        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<GameData> listGames() {
        ArrayList<GameData> games = new ArrayList<>();
        String query = "";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameTable")){
             ps.executeQuery();
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    int gameID = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    ChessGame gameData = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                    games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, gameData));
                }
            }
        } catch (SQLException | DataAccessException e) {
            return null;
        }

        return games;
    }

    public void updateGame(int gameID, GameData newGame) {

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("UPDATE gameTable SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?")) {
            ps.setString(1, newGame.whiteUsername());
            ps.setString(2, newGame.blackUsername());
            ps.setString(3, newGame.gameName());
            ps.setString(4, new Gson().toJson(newGame.game()));
            ps.setInt(5, gameID);
            ps.executeUpdate();
        } catch (SQLException | DataAccessException _) {
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS gameTable (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) NOT NULL,
              `blackUsername` varchar(256) NOT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`gameID`)
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
