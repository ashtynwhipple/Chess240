package handler;

import Model.AuthData;
import Model.UserData;
import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class loginHandler {

    private static final MemoryUserDAO userDAO = new MemoryUserDAO();

    private static final MemoryAuthDAO authDAO = new MemoryAuthDAO();


    public static Object login(Request req, Response res) {

        UserData userdata = new Gson().fromJson(req.body(), UserData.class);

        if (userdata == null || !Objects.equals(userdata.password(), userDAO.getUser(userdata.username()).password())){
            res.status(401);
            // exception
        }

        assert userdata != null; // problem??

        String auth = String.valueOf(authDAO.createAuth(userdata.username()));

        AuthData authdata = new AuthData(auth, userdata.username());

        res.status(200);

        return new Gson().toJson(authdata);

    }

}
