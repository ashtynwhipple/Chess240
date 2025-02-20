package handler;

import spark.Request;
import spark.Response;

public class exceptionHandler {

    public void exception(ResponseException ex, Request req, Response res) {

        switch (res.status() == 403){
            throw
        } (res.status() == 500)


//        res.status(ex.StatusCode());
//        res.body(ex.toJson());
    }

}
