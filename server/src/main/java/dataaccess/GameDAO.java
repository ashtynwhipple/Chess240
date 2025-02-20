package dataaccess;

import Model.GameData;
import chess.ChessGame;

import java.util.ArrayList;
import java.util.HashMap;

public interface GameDAO {

    void clearGames();

    void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game);

    GameData getGame(int gameID);

    HashMap<Integer, GameData> listGames();

    void updateGame(int gameID, GameData new_game);

    boolean is_empty();

}
