package handler.GameHandlerclasses;

import Model.GameData;
import Model.UserData;
import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class ListGamesHandler {

    private static final MemoryAuthDAO authDAO = new MemoryAuthDAO();

    private static final MemoryGameDAO gameDAO = new MemoryGameDAO();


    public static Object listGames(Request req, Response res){
        UserData userdata = new Gson().fromJson(req.body(), UserData.class);

        String auth = String.valueOf(authDAO.getAuth(userdata.username()));

        if (auth == null){
            res.status(401);
        }

        HashMap<Integer, GameData> games = gameDAO.listGames();

        res.status(200);

        return new Gson().toJson(games);

    }

}
