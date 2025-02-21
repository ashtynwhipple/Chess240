package handler;
import dataaccess.*;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;

public class GameHandler {

    private static final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    private static final MemoryGameDAO gameDAO = new MemoryGameDAO();

    public GameHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public list_games(){

    }

}
