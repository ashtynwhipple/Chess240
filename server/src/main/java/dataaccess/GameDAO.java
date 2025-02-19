package dataaccess;

import Model.GameData;

public interface GameDAO {

    void clearGames();

    GameData createGame();

    GameData getGame();

    GameData listGames();

    GameData updateGame();

    boolean is_clear();

}
