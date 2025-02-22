package Service;

import Model.AuthData;
import Model.GameData;
import Model.UserData;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.StatusException;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class GameService {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Object listGames(AuthData authData) throws StatusException {
        String auth = String.valueOf(authDAO.getAuth(authData.username()));

        if (auth == null){
            throw new StatusException("Error not valid auth token", 403);
        }

        return gameDAO.listGames();

    }

    public void createGame(String auth_token, GameData gameData) throws StatusException {
        if (authDAO.getAuth(auth_token) == null){
            throw new StatusException("Error: unauthorized", 401);
        }

        gameDAO.createGame(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());

        if (gameDAO.getGame(gameData.gameID()) == null){
            throw new StatusException("Error: bad request", 400);
        }

    }

    public void join_game(String authToken, int gameID) throws StatusException {
        if (authDAO.getAuth(authToken) == null){
            throw new StatusException("Error: unauthorized", 401);
        }

        if (gameDAO.getGame(gameID) == null){
            throw new StatusException("Error: bad request", 400);
        }

        GameData gamedata = gameDAO.getGame(gameID);

        String white = gamedata.whiteUsername();
        String black = gamedata.blackUsername();

//        if ()

    }

}
