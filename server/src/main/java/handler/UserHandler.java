package handler;
import model.UserData;
import Service.userservice.LoginService;
import Service.userservice.LogoutService;
import Service.userservice.RegisterService;
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
            RegisterService service_instance = new RegisterService(userDAO, authDAO);
            res.status(200);
            return new Gson().toJson(service_instance.register(registerRequest));
        } catch (StatusException e) {
            res.status(e.get_status());
            return "{ \"message\": \"Error: already taken\" }";
        }

    }

    public Object logout(Request req, Response res) {
        String logout_request = req.headers("authorization");

        try {
            LogoutService service_instance = new LogoutService(authDAO);
            res.status(200);
            service_instance.logout(logout_request);
            return "{}";
        } catch (StatusException e) {
            res.status(e.get_status());
            return "{ \"message\": \"Error: unauthorized\" }";
        }
    }

    public Object login(Request req, Response res) throws StatusException {

        UserData loginRequest = new Gson().fromJson(req.body(), UserData.class);

        try {
            LoginService service_instance = new LoginService(userDAO, authDAO);
            res.status(200);
            return new Gson().toJson(service_instance.login(loginRequest));
        } catch (StatusException e) {
            res.status(e.get_status());
            return "{ \"message\": \"Error: unauthorized\" }";
        }

    }
}
