package Service;
import Model.AuthData;
import Model.GameData;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.StatusException;
import java.util.Objects;

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

    public void join_game(String authToken, int gameID, String color) throws StatusException {
        if (authDAO.getAuth(authToken) == null){
            throw new StatusException("Error: unauthorized", 401);
        }

        if (gameDAO.getGame(gameID) == null){
            throw new StatusException("Error: bad request", 400);
        }

        GameData gamedata = gameDAO.getGame(gameID);
        AuthData authdata = authDAO.getAuth(authToken);

        String white = gamedata.whiteUsername();
        String black = gamedata.blackUsername();

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
