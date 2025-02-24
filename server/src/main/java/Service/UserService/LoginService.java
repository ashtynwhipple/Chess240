package Service.UserService;

import Model.AuthData;
import Model.UserData;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import exception.StatusException;

import java.util.Objects;

public class LoginService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData login(UserData userdata) throws StatusException {

        if (userdata == null || userdata.username() == null || userDAO.getUser(userdata.username()) == null){
            throw new StatusException("no username found", 401);
        } else if (!Objects.equals(userdata.password(), userDAO.getUser(userdata.username()).password())){
            throw new StatusException("Error password does not match or User DNE", 401);
        }

        return authDAO.createAuth(userdata.username());

    }
}
