package handler.UserHandlerclasses;
import Model.UserData;
import Service.UserService.LoginService;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import exception.StatusException;
import spark.Request;
import spark.Response;

public class LoginHandler {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginHandler(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public Object login(Request req, Response res) {

        UserData loginRequest = new Gson().fromJson(req.body(), UserData.class);

        try {
            LoginService service_instance = new LoginService(userDAO, authDAO);
            res.status(200);
            return new Gson().toJson(service_instance.login(loginRequest));
        } catch (StatusException e) {
            res.status(e.get_status());
            return null;
        }

    }

}
