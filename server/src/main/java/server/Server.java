package server;

import dataaccess.*;
import handler.*;
import handler.UserHandlerclasses.LoginHandler;
import handler.UserHandlerclasses.LogoutHandler;
import handler.UserHandlerclasses.RegisterHandler;
import spark.*;


public class Server {

    private final ClearHandler clear;
    private final UserHandler user_handler;

    public Server(){
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        this.clear = new ClearHandler(userDAO, authDAO, gameDAO);
        this.user_handler = new UserHandler(userDAO, authDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.post("/user", user_handler::register);

        Spark.post("/session", user_handler::login);
        Spark.delete("/session", user_handler::logout);

//        Spark.get("/game", listGamesHandler::listGames);
//        Spark.post("/game", this::createGame);
//        Spark.put("/game", this::joinGame);
//
        Spark.delete("/db", clear::clear);

//        Spark.exception(DataAccessException.class, exceptionHandler::exception);

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
