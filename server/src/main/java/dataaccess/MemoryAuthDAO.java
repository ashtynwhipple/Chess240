package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    public HashMap<String, AuthData> hash = new HashMap<>();

    public void clearAuth(){
        hash.clear();
    }

    public AuthData createAuth(String username){
        String token = generateToken();
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

    private String generateToken(){
        return UUID.randomUUID().toString();
    }

    public boolean isEmpty(){
        return hash.isEmpty();
    }

    public void clearAll(){
        hash.clear();
    }

}
