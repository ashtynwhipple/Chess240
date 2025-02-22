package handler.UserHandlerclasses;
import Model.UserData;
import Service.UserService.LogoutService;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import exception.StatusException;
import spark.Request;
import spark.Response;

public class LogoutHandler {

    private final AuthDAO authDAO;


    public LogoutHandler(AuthDAO authDAO){
        this.authDAO = authDAO;
    }

    public void logout(Request req, Response res){
        UserData logout_request = new Gson().fromJson(req.body(), UserData.class);

        try {
            LogoutService service_instance = new LogoutService(authDAO);
            res.status(200);
            service_instance.logout(logout_request);
        } catch (StatusException e) {
            res.status(e.get_status());
        }
    }

}
