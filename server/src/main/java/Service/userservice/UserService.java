package Service.userservice;

import model.AuthData;
import model.UserData;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import exception.StatusException;

import java.util.Objects;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

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

    public AuthData login(UserData userdata) throws StatusException {

        if (userdata == null || !Objects.equals(userdata.password(), userDAO.getUser(userdata.username()).password())){
            throw new StatusException("Error password does not match or User DNE", 403);
        }

        String auth = String.valueOf(authDAO.createAuth(userdata.username()));

        return new AuthData(auth, userdata.username());

    }

    public void logout(UserData userdata) throws StatusException {

        String auth = String.valueOf(authDAO.getUsername(userdata.username()));

        if (auth == null){
            throw new StatusException("auth DNE when trying to logout", 403);
        }

        authDAO.deleteAuth(userdata.username());

        if (authDAO.getUsername(userdata.username()) != null){
            throw new StatusException("auth was not deleted", 500);
        }

    }

}
