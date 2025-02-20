package handler;

import spark.Request;
import spark.Response;

public class exceptionHandler {

    public void exception(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        res.body(ex.toJson());
    }

}
