package dataaccess;
import Model.GameData;
import chess.ChessGame;
import java.util.Collection;

public interface GameDAO {

    void clearGames();

    void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game);

    GameData getGame(int gameID);

    Collection<GameData> listGames();

    void updateGame(int gameID, GameData new_game);

    boolean is_empty();

    void clear_all();

}
