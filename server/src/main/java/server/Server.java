package server;
import dataaccess.*;
import handler.*;
import server.websocket.WebSocketHandler;
import spark.*;


public class Server {

    private final ClearHandler clear;
    private final UserHandler userHandler;
    private final GameHandler gameHandler;

    private final WebSocketHandler webSocketHandler;

    public Server(){
        UserSqlDataAccess userDAO = new UserSqlDataAccess();
        AuthSqlDataAccess authDAO = new AuthSqlDataAccess();
        GameSqlDataAccess gameDAO = new GameSqlDataAccess();
        this.clear = new ClearHandler(userDAO, authDAO, gameDAO);
        this.userHandler = new UserHandler(userDAO, authDAO);
        this.gameHandler = new GameHandler(authDAO, gameDAO);
        webSocketHandler = new WebSocketHandler(authDAO, gameDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", webSocketHandler);

        Spark.post("/user", userHandler::register);

        Spark.post("/session", userHandler::login);
        Spark.delete("/session", userHandler::logout);

        Spark.get("/game", gameHandler::listGames);
        Spark.post("/game", gameHandler::createGame);
        Spark.put("/game", gameHandler::joinGame);

        Spark.delete("/db", clear::clear);

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
