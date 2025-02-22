package Service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.StatusException;

public class ClearService {

    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public ClearService(UserDAO user, AuthDAO auth, GameDAO game){
        this.authDAO = auth;
        this.gameDAO = game;
        this.userDAO = user;
    }
    public void clear() throws StatusException {

        authDAO.clear_all();
        userDAO.clear_all();
        gameDAO.clear_all();

        if (!authDAO.is_empty()){
            throw new StatusException("could not clear AuthDAO", 500);
        } else if (!userDAO.is_empty()){
            throw new StatusException("could not clear UserDAO", 500);
        } else if (!gameDAO.is_empty()) {
            throw new StatusException("could not clear GameDAO", 500);
        }

    }
}
