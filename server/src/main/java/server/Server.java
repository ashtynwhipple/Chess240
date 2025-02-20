package server;

import dataaccess.DataAccessException;
import spark.*;
import exception.ResponseException;
import com.google.gson.Gson;
import Model.UserData;


public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
//
        Spark.post("/user", this::register);

        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);

        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

        Spark.delete("/db", this::clear);

        Spark.exception(DataAccessException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        res.body(ex.toJson());
    }

    private Object register(Request req, Response res) throws ResponseException {
        var pet = new Gson().fromJson(req.body(), UserData.class);
        pet = service.addPet(pet);
        webSocketHandler.makeNoise(pet.name(), pet.sound());
        return new Gson().toJson(pet);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
