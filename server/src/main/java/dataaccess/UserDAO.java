package dataaccess;

import model.UserData;

public interface UserDAO {

    void createUser(String username, String password, String email);

    UserData getUser(String username);

    boolean isEmpty();

    void clearAll();

    boolean verifyUser(String username, String providedClearTextPassword);

}
