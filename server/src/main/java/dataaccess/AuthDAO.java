package dataaccess;

import Model.AuthData;

public interface AuthDAO {

    void clearAuth();

    AuthData createAuth(String username);

    void deleteAuth(String username);

    AuthData getUsername(String username);

    boolean is_empty();

    void clear_all();


}
