package service;
import model.AuthData;
import model.GameData;
import model.ListGameData;
import chess.ChessGame;
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

    public ListGameData listGames(String authToken) throws StatusException {
        if (authDAO.getUsername(authToken) == null){
            throw new StatusException("authToken invalid", 401);
        }

        return new ListGameData(gameDAO.listGames());

    }

    public int createGame(String authToken, GameData gameData) throws StatusException {
        if (authToken == null || gameData == null || authDAO.getUsername(authToken) == null){
            throw new StatusException("Error: unauthorized", 401);
        }

        Random random = new Random();
        int gameID = random.nextInt(1_000_000);

        gameDAO.createGame(gameID, null, null, gameData.gameName(), new ChessGame());

        if (gameDAO.getGame(gameID) == null || gameDAO.getGame(gameID).gameID() != gameID){
            throw new StatusException("Error: bad request", 401);
        }

        return gameID;
    }

    public void joinGame(String authToken, int gameID, String color) throws StatusException {
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
            throw new StatusException("authdata not valid", 401);
        }

        if (color == null){
            throw new StatusException("not a valid team color", 400);
        }

        switch (color){
            case "WHITE" -> {if (white != null && !Objects.equals(authdata.username(), gamedata.whiteUsername())){
                throw new StatusException("space already taken by another user", 403);
            } else {white = authdata.username();}
            }
            case "BLACK" -> {if (black != null && !Objects.equals(authdata.username(), gamedata.blackUsername())){
                    throw new StatusException("space already taken by another user", 403);
                } else {black = authdata.username();}
            }
            default -> throw new StatusException("not a valid team color", 400);
        }

        gameDAO.updateGame(gameID, new GameData(gameID, white, black, gamedata.gameName(), gamedata.game()));

    }

}
