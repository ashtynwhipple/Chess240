package handler;
import service.ClearService;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.StatusException;
import spark.Request;
import spark.Response;

public class ClearHandler {

    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public ClearHandler(UserDAO user, AuthDAO auth, GameDAO game){
        this.authDAO = auth;
        this.gameDAO = game;
        this.userDAO = user;
    }

    public Object clear(Request req, Response res){

        try {
            ClearService serviceInstance = new ClearService(userDAO, authDAO, gameDAO);
            res.status(200);
            serviceInstance.clear();
            return "{}";
        } catch (StatusException e) {
            res.status(e.getStatus());
            return null;
        }
    }

}
