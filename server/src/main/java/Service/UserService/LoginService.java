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

        if (userdata == null || !Objects.equals(userdata.password(), userDAO.getUser(userdata.username()).password())){
            throw new StatusException("Error password does not match or User DNE", 403);
        }

        String auth = String.valueOf(authDAO.createAuth(userdata.username()));

        return new AuthData(auth, userdata.username());

    }
}
