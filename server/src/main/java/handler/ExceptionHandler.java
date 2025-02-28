package handler;

import spark.Request;
import spark.Response;

public class ExceptionHandler {

    public void exception(Request req, Response res) {
        int statusCode = res.status();
        String message = res.body();

        switch (statusCode) {
            case 400:
                res.body("Error: bad request");
                break;
            case 403:
                res.body("Error: Already taken");
                break;
            case 401:
                res.body(message + ": Error: unauthorized");
                break;
            case 500:
                res.body("Error: " + message);
                break;
            default:
                res.body(statusCode + " Error: An unknown error occurred.");
                break;
        }
    }

}
