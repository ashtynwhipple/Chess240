package handler;
import model.GameData;
import model.GameResponse;
import model.JoinData;
import service.GameService;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.StatusException;
import spark.Request;
import spark.Response;

public class GameHandler {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Object listGames(Request req, Response res){

        try {
            GameService serviceInstance = new GameService(authDAO, gameDAO);
            String authToken = req.headers("authorization");
            res.status(200);
            return new Gson().toJson(serviceInstance.listGames(authToken));
        } catch (StatusException e) {
            res.status(e.getStatus());
            return "{ \"message\": \"Error: " + e.getMessage() + "\" }";
        }
    }

    public Object createGame(Request req, Response res) throws StatusException {

        GameData gameData = new Gson().fromJson(req.body(), GameData.class);

        try {
            GameService serviceInstance = new GameService(authDAO, gameDAO);
            String authToken = req.headers("authorization");
            res.status(200);
            int newGameID = serviceInstance.createGame(authToken, gameData);
            Gson gson = new Gson();
            return gson.toJson(new GameResponse(newGameID));
        } catch (StatusException e) {
            res.status(e.getStatus());
            return "{ \"message\": \"Error: " + e.getMessage() + "\" }";
        }

    }

    public Object joinGame(Request req, Response res){

        JoinData joinData = new Gson().fromJson(req.body(), JoinData.class);

        try{
            GameService serviceInstance = new GameService(authDAO, gameDAO);
            String authToken = req.headers("authorization");
            res.status(200);
            serviceInstance.joinGame(authToken, joinData.gameID(), joinData.playerColor());
            return "{}";
        } catch (StatusException e) {
            res.status(e.getStatus());
            return "{ \"message\": \"Error: " + e.getMessage() + "\" }";
        }
    }
}
