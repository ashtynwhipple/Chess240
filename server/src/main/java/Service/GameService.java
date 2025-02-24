package Service;
import Model.AuthData;
import Model.GameData;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.StatusException;
import java.util.Objects;
import java.util.Random;

public class GameService {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Object listGames(AuthData authData) throws StatusException {
        if (authData == null){
            throw new StatusException("authdata invalid", 401);
        }

        String auth = String.valueOf(authDAO.getUsername(authData.username()));

        if (auth == null){
            throw new StatusException("Error not valid auth token", 403);
        }

        return gameDAO.listGames();

    }

    public int createGame(String auth_token, GameData gameData) throws StatusException {
        if (auth_token == null || gameData == null){
            throw new StatusException("Error: unauthorized", 401);
        }

        Random random = new Random();
        int gameID = random.nextInt(1_000_000);

        gameDAO.createGame(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());

        if (gameDAO.getGame(gameID) == null || gameDAO.getGame(gameID).gameID() != gameID){
            throw new StatusException("Error: bad request", 401);
        }

        return gameID;
    }

    public void join_game(String authToken, int gameID, String color) throws StatusException {
        if (authToken == null){
            throw new StatusException("Error: unauthorized", 403);
        }

        if (gameDAO.getGame(gameID) == null){
            throw new StatusException("Error: bad request", 400);
        }

        GameData gamedata = gameDAO.getGame(gameID);
        AuthData authdata = authDAO.getUsername(authToken);

        String white = gamedata.whiteUsername();
        String black = gamedata.blackUsername();

        if (authdata == null){
            throw new StatusException("authdata not valid", 403);
        }

        if (Objects.equals(color, "WHITE")) {
            if (white != null && !Objects.equals(authdata.username(), gamedata.whiteUsername())){
                throw new StatusException("space already taken by another user", 403);
            } else {white = authdata.username();}
        } else if (Objects.equals(color, "BLACK")) {
            if (black != null && !Objects.equals(authdata.username(), gamedata.blackUsername())){
                throw new StatusException("space already taken by another user", 403);
            } else {black = authdata.username();}
        } else if (color != null) throw new StatusException("not a valid team color", 400);


        gameDAO.updateGame(gameID, new GameData(gameID, white, black, gamedata.gameName(), gamedata.game()));

//        if (gameDAO.getGame(gameID) == null){
//            throw new StatusException("game could not update", 400);
//        } else if (gameDAO.getGame(gameID).whiteUsername() != )

    }

}
