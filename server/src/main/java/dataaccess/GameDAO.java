package dataaccess;
import model.GameData;
import chess.ChessGame;
import java.util.Collection;

public interface GameDAO {

    void clearGames();

    void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game);

    GameData getGame(int gameID);

    Collection<GameData> listGames();

    void updateGame(int gameID, GameData newGame);

    boolean isEmpty();

    void clearAll();

}
