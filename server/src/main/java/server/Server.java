package server;
import dataaccess.*;
import handler.*;
import spark.*;


public class Server {

    private final ClearHandler clear;
    private final UserHandler user_handler;
    private final GameHandler gameHandler;

    public Server(){
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        this.clear = new ClearHandler(userDAO, authDAO, gameDAO);
        this.user_handler = new UserHandler(userDAO, authDAO);
        this.gameHandler = new GameHandler(authDAO, gameDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.post("/user", user_handler::register);

        Spark.post("/session", user_handler::login);
        Spark.delete("/session", user_handler::logout);

        Spark.get("/game", gameHandler::list_games);
        Spark.post("/game", gameHandler::create_game);
        Spark.put("/game", gameHandler::joinGame);

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
