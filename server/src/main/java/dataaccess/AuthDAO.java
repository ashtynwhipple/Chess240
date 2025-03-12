package dataaccess;

import model.AuthData;

public interface AuthDAO {

    AuthData createAuth(String username);

    void deleteAuth(String username);

    AuthData getUsername(String username);

    boolean isEmpty();

    void clearAll();


}
