package dataaccess;

import Model.AuthData;

public interface AuthDAO {

    void clearAuth();

    AuthData createAuth(String username);

    void deleteAuth(String username);

    AuthData getAuth(String username);

    boolean is_empty();

}
