package Service;

import Model.AuthData;
import Model.UserData;
import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exception.StatusException;
import spark.Request;
import spark.Response;

public class RegisterService {

    private static final MemoryUserDAO userDAO = new MemoryUserDAO();

    private static final MemoryAuthDAO authDAO = new MemoryAuthDAO();

    public AuthData register(UserData userdata) throws StatusException {


        if (userdata == null || userdata.username() == null || userdata.password() == null) {
            throw new StatusException("", 403);
        }

        if (userDAO.getUser(userdata.username()) != null) {
            throw new StatusException("", 403);
        }

        userDAO.createUser(userdata.username(), userdata.password(), userdata.email());

        String auth = String.valueOf(authDAO.createAuth(userdata.username()));

        return new AuthData(auth, userdata.username());
    }

}
