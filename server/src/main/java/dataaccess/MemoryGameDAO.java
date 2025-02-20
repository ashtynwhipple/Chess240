package dataaccess;

import Model.AuthData;
import Model.GameData;
import chess.ChessGame;

import java.util.HashMap;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO{

    public HashMap<Integer, GameData> hash = new HashMap<>();

    public void clearGames(){
        hash.clear();
    }

    public void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){

        GameData new_game = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        hash.put(gameID, new_game);
    }

    public GameData getGame(int gameID){
        return hash.get(gameID);
    }

    public HashMap<Integer, GameData> listGames(){
        return hash;
    }

    public void updateGame(int gameID, GameData new_game) {
        hash.put(gameID, new_game);
    }

    public boolean is_empty(){
        return hash.isEmpty();
    }

}
