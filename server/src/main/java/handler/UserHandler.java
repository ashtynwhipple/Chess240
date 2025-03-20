package handler;
import model.UserData;
import service.userservice.LoginService;
import service.userservice.LogoutService;
import service.userservice.RegisterService;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import exception.StatusException;
import spark.Request;
import spark.Response;

public class UserHandler {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserHandler(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public Object register(Request req, Response res) throws StatusException {

        UserData registerRequest = new Gson().fromJson(req.body(), UserData.class);

        try {
            RegisterService serviceInstance = new RegisterService(userDAO, authDAO);
            res.status(200);
            return new Gson().toJson(serviceInstance.register(registerRequest));
        } catch (StatusException e) {
            res.status(e.getStatus());
            return "{ \"message\": \"Error: " + e.getMessage() + "\" }";
        }

    }

    public Object logout(Request req, Response res) {
        String logoutRequest = req.headers("authorization");

        try {
            LogoutService serviceInstance = new LogoutService(authDAO);
            res.status(200);
            serviceInstance.logout(logoutRequest);
            return "{}";
        } catch (StatusException e) {
            res.status(e.getStatus());
            return "{ \"message\": \"Error: unauthorized\" }";
        }
    }

    public Object login(Request req, Response res) throws StatusException {

        UserData loginRequest = new Gson().fromJson(req.body(), UserData.class);

        try {
            LoginService serviceInstance = new LoginService(userDAO, authDAO);
            res.status(200);
            return new Gson().toJson(serviceInstance.login(loginRequest));
        } catch (StatusException e) {
            res.status(e.getStatus());
            return "{ \"message\": \"Error: unauthorized\" }";
        }

    }
}
