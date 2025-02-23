package handler;
import Model.AuthData;
import Model.GameData;
import Model.JoinData;
import Service.GameService;
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

    public Object list_games(Request req, Response res){
        AuthData registerRequest = new Gson().fromJson(req.body(), AuthData.class);

        try {
            GameService service_instance = new GameService(authDAO, gameDAO);
            res.status(200);
            return new Gson().toJson(service_instance.listGames(registerRequest));
        } catch (StatusException e) {
            res.status(e.get_status());
            return null;
        }

    }

    public Object create_game(Request req, Response res) throws StatusException {

        if (!req.body().contains("\"gameName\":")) {
            throw new StatusException("Error: bad request", 400);
        }

        GameData gameData = new Gson().fromJson(req.body(), GameData.class);

        try {
            GameService service_instance = new GameService(authDAO, gameDAO);
            String authToken = req.headers("authorization");
            res.status(200);
            service_instance.createGame(authToken, gameData);
            return "gameID: " + gameData.gameID();
        } catch (StatusException e) {
            res.status(e.get_status());
            return null;
        }

    }

    public Object joinGame(Request req, Response res) throws StatusException {

        if (!req.body().contains("\"gameID\":")) {
            throw new StatusException("Error: bad request", 400);
        }

        JoinData joinData = new Gson().fromJson(req.body(), JoinData.class);

        try{
            GameService service_instance = new GameService(authDAO, gameDAO);
            String authToken = req.headers("authorization");
            res.status(200);
            service_instance.join_game(authToken, joinData.gameID(), joinData.color());
            return "{}";
        } catch (StatusException e) {
            res.status(e.get_status());
            return null;
        }

    }

}
