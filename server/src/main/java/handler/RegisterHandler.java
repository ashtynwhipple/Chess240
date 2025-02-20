package handler;

import Model.AuthData;
import Model.UserData;
import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import spark.Request;
import spark.Response;
import dataaccess.MemoryUserDAO;

import java.util.HashMap;
import java.util.Map;

public class RegisterHandler {

    private static final MemoryUserDAO userDAO = new MemoryUserDAO();
    private static final MemoryAuthDAO authDAO = new MemoryAuthDAO();


    public static Object register(Request req, Response res) throws ResponseException {

        UserData userdata = new Gson().fromJson(req.body(), UserData.class);

        if (userdata == null || userdata.username() == null || userdata.password() == null) {
            res.status(403);
            // throw exception here
        }

        assert userdata != null; //can I do this??
        if (userDAO.getUser(userdata.username()) != null) {
            res.status(403);
            // throw exception here
        }

        userDAO.createUser(userdata.username(), userdata.password(), userdata.email());

        res.status(200);

        String auth = String.valueOf(authDAO.createAuth(userdata.username()));

        AuthData authdata = new AuthData(auth, userdata.username());

        return new Gson().toJson(authdata);
    }
}
