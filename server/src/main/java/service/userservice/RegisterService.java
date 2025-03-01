package service.userservice;

import model.AuthData;
import model.UserData;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import exception.StatusException;


public class RegisterService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData userdata) throws StatusException {


        if (userdata == null || userdata.username() == null || userdata.password() == null) {
            throw new StatusException("", 400);
        }

        if (userDAO.getUser(userdata.username()) != null) {
            throw new StatusException("", 403);
        }

        userDAO.createUser(userdata.username(), userdata.password(), userdata.email());

//        String auth = String.valueOf(authDAO.createAuth(userdata.username()));

//        return new AuthData(auth, userdata.username());

        return authDAO.createAuth(userdata.username());
    }

}
