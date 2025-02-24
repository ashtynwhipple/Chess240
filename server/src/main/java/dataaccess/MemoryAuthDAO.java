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
        AuthData data = new AuthData(token, username);
        hash.put(token, data);
        return data;
    }

    public void deleteAuth(String token){
        hash.remove(token);
    }

    public AuthData getUsername(String token){
        return hash.get(token);
    }

    private String generate_token(){
        return UUID.randomUUID().toString();
    }

    public boolean is_empty(){
        return hash.isEmpty();
    }

    public void clear_all(){
        hash.clear();
    }

}
