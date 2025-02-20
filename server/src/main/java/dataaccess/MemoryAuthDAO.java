package dataaccess;

import Model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    public HashMap<String, AuthData> hash = new HashMap<>();

    public void clearAuth(){
        hash.clear();
    }

    public AuthData createAuth(String username){
        String token = generate_token();
        AuthData data = new AuthData(username, token);
        hash.put(username, data);
        return data;
    }

    public void deleteAuth(String username){}

    public AuthData getAuth(String username){
        return hash.get(username);
    }

    private String generate_token(){
        return UUID.randomUUID().toString();
    }

    public boolean is_empty(){
        return hash.isEmpty();
    }


}
