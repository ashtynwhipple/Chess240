package dataaccess;
import model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{

    public HashMap<String, UserData> hash = new HashMap<>();

    public void createUser(String username, String password, String email) {
        hash.put(username, new UserData(username, password, email));
    }

    public UserData getUser(String username) {
        return hash.getOrDefault(username, null);
    }

    public boolean isEmpty() {
        return hash.isEmpty();
    }

    public void clearAll(){
        hash.clear();
    }

}
