package handler;
import Model.UserData;
import Service.RegisterService;
import com.google.gson.Gson;
import dataaccess.*;
import exception.StatusException;
import spark.Request;
import spark.Response;

public class RegisterHandler {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    private RegisterHandler(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public Object register(Request req, Response res) {

        UserData registerRequest = new Gson().fromJson(req.body(), UserData.class);

        try {
            RegisterService service_instance = new RegisterService(userDAO, authDAO);
            res.status(200);
            return new Gson().toJson(service_instance.register(registerRequest));
        } catch (StatusException e) {
            res.status(e.get_status());
            return null;
        }

    }
}
