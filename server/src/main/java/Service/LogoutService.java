package Service;
import Model.UserData;
import dataaccess.AuthDAO;
import exception.StatusException;

public class LogoutService {

    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO){
        this.authDAO = authDAO;
    }

    public void logout(UserData userdata) throws StatusException {

        String auth = String.valueOf(authDAO.getAuth(userdata.username()));

        if (auth == null){
            throw new StatusException("auth DNE when trying to logout", 403);
        }

        authDAO.deleteAuth(userdata.username());

        if (authDAO.getAuth(userdata.username()) != null){
            throw new StatusException("auth was not deleted", 500);
        }

    }
}
