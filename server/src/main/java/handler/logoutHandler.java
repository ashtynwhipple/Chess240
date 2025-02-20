package handler;

import Model.AuthData;
import Model.UserData;
import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import spark.Request;
import spark.Response;

public class logoutHandler {

    private static final MemoryUserDAO userDAO = new MemoryUserDAO();

    private static final MemoryAuthDAO authDAO = new MemoryAuthDAO();

    public static Object logout(Request req, Response res){

        UserData userdata = new Gson().fromJson(req.body(), UserData.class);

        String auth = String.valueOf(authDAO.getAuth(userdata.username()));

        if (auth == null){
            res.status(401);
        }

        authDAO.deleteAuth(userdata.username());

        if (authDAO.getAuth(userdata.username()) != null){
            res.status(500);
        }

        res.status(200);

        return null;

    }

}
