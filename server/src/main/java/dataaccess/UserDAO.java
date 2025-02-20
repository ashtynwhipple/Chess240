package dataaccess;

import Model.UserData;

public interface UserDAO {

    void createUser(String username, String password, String email);

    UserData getUser(String username);

    boolean is_empty();

}
