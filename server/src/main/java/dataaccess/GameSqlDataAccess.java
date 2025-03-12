package dataaccess;
import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class GameSqlDataAccess extends BaseSqlDataAccess implements GameDAO{

    private static final String[] CREATE_STATEMENTS = {
            """
            CREATE TABLE IF NOT EXISTS gameTable (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`gameID`)
            );
            """
    };

    public GameSqlDataAccess() {
        super(CREATE_STATEMENTS);
    }

    public void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){
        try(var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement(
                    "INSERT INTO gameTable (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)"
            )){
                ps.setInt(1, gameID);
                ps.setString(2, whiteUsername);
                ps.setString(3, blackUsername);
                ps.setString(4, gameName);
                ps.setString(5, new Gson().toJson(game));
                ps.executeUpdate();
            }
        }catch (SQLException | DataAccessException e){
            throw new RuntimeException(e);
        }
    }

    public GameData getGame(int gameID){
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT whiteUsername, blackUsername, gameName, game FROM gameTable Where gameID = ?")) {
            ps.setInt(1, gameID);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    String gameJson = rs.getString("game");
                    ChessGame game = new Gson().fromJson(gameJson, ChessGame.class);
                    return new GameData(
                            gameID, rs.getString(
                                    "whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), game
                    );
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
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

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameTable")){
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
            ps.setObject(4, new Gson().toJson(newGame.game()));
            ps.setInt(5, gameID);
            ps.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
