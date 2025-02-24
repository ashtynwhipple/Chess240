package Service.UserService;
import Model.UserData;
import dataaccess.AuthDAO;
import exception.StatusException;

public class LogoutService {

    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO){
        this.authDAO = authDAO;
    }

    public void logout(String token) throws StatusException {

        if (token == null || authDAO.getAuth(token) == null){
            throw new StatusException("not valid user data or username DNE", 401);
        }

        authDAO.deleteAuth(token);

        if (authDAO.getAuth(token) != null){
            throw new StatusException("auth was not deleted", 500);
        }

    }
}
