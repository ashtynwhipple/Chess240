package dataaccess;
import model.GameData;
import chess.ChessGame;
import java.util.Collection;
import java.util.HashMap;


public class MemoryGameDAO implements GameDAO{

    public HashMap<Integer, GameData> hash = new HashMap<>();

    public void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){

        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        hash.put(gameID, newGame);
    }

    public GameData getGame(int gameID){
        return hash.get(gameID);
    }

    public Collection<GameData> listGames(){
        return hash.values();
    }

    public void updateGame(int gameID, GameData newGame) {
        hash.remove(gameID);
        hash.put(gameID, newGame);
    }

    public boolean isEmpty(){
        return hash.isEmpty();
    }

    public void clearAll(){
        hash.clear();
    }

}
