package handler;
import model.GameData;
import model.JoinData;
import Service.GameService;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.StatusException;
import spark.Request;
import spark.Response;

import java.util.Map;

public class GameHandler {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Object list_games(Request req, Response res){
        String authToken = req.headers("authorization");

        try {
            GameService service_instance = new GameService(authDAO, gameDAO);
            res.status(200);
            return new Gson().toJson(service_instance.listGames(authToken));
        } catch (StatusException e) {
            res.status(e.get_status());
            return "{ \"message\": \"Error: unauthorized\" }";
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
            int new_game_ID = service_instance.createGame(authToken, gameData);
            Gson gson = new Gson();
            return gson.toJson(Map.of("gameID", new_game_ID));
        } catch (StatusException e) {
            res.status(e.get_status());
            return "{ \"message\": \"Error: unauthorized\" }";
        }

    }

    public Object joinGame(Request req, Response res) throws StatusException {

        JoinData joinData = new Gson().fromJson(req.body(), JoinData.class);

        try{
            GameService service_instance = new GameService(authDAO, gameDAO);
            String authToken = req.headers("authorization");
            res.status(200);
            service_instance.join_game(authToken, joinData.gameID(), joinData.playerColor());
            return "{}";
        } catch (StatusException e) {
            res.status(e.get_status());
            return "{ \"message\": \"Error: unauthorized\" }";
        }

    }

}
