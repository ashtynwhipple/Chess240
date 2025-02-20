package server;

import dataaccess.*;
import handler.RegisterHandler;
import spark.*;
import handler.exceptionHandler;
import handler.loginHandler;
import handler.logoutHandler;
import handler.listGamesHandler;



public class Server {

    private final MemoryUserDAO userDAO = new MemoryUserDAO();
    private final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    private final MemoryGameDAO gameDAO = new MemoryGameDAO();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
//
        Spark.post("/user", RegisterHandler::register(userDAO, authDAO));

        Spark.post("/session", loginHandler::login(authDAO));
        Spark.delete("/session", logoutHandler::logout);

        Spark.get("/game", listGamesHandler::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

        Spark.delete("/db", this::clear);

        Spark.exception(DataAccessException.class, exceptionHandler::exception);

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
