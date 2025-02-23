package dataaccess;
import Model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{

    public HashMap<String, UserData> hash = new HashMap<>();

    public void createUser(String username, String password, String email) {
        hash.put(username, new UserData(username, password, email));
    }

    public UserData getUser(String username) {
        return hash.get(username);
    }

    public boolean is_empty() {
        return hash.isEmpty();
    }

    public void clear_all(){
        hash.clear();
    }

}
