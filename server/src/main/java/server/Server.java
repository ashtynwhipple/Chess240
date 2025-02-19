package server;

import dataaccess.DataAccessException;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
//
        Spark.post("/user", this::register_handle);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();


        Spark.exception(DataAccessException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register_handle(){ }

    private Object register(){
        return null;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
