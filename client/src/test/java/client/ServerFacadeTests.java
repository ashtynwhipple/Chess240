package client;

import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.util.Map;


public class ServerFacadeTests {

    private static Server server;
    static int port;
    ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void setup(){
        facade = new ServerFacade(port);
        Assertions.assertDoesNotThrow(() -> facade.clear());
    }

    @Test
    public void registerPositive() {
        UserData user = new UserData("user", "pass", "email");
        Assertions.assertDoesNotThrow(() -> facade.register(user));
    }

    @Test
    public void registerNegative() {
        UserData user = new UserData(null, "", "");
        Assertions.assertThrows(ResponseException.class, () -> facade.register(user));
    }

    @Test
    public void loginPositive() throws ResponseException {
        UserData user = new UserData("user", "pass", "email");
        facade.register(user);
        Assertions.assertThrows(ResponseException.class, () -> facade.login(user));
    }

    @Test
    public void loginNegative() {
        UserData user = new UserData("wrongUser", "wrongPass", "wrongEmail");
        Assertions.assertThrows(ResponseException.class, () -> facade.login(user));
    }

    @Test
    public void createGamePositive() {
        UserData user = new UserData("user", "pass", "email");
        Assertions.assertDoesNotThrow(() -> facade.register(user));
        AuthData auth = Assertions.assertDoesNotThrow(() -> facade.login(user));
        Assertions.assertDoesNotThrow(() -> facade.createGame("NewGame", auth.authToken()));
    }

    @Test
    public void createGameNegative() {
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame("", "invalidToken"));
    }

    @Test
    public void listGamesPositive() {
        UserData user = new UserData("user", "pass", "email");
        Assertions.assertDoesNotThrow(() -> facade.register(user));
        AuthData auth = Assertions.assertDoesNotThrow(() -> facade.login(user));
        Assertions.assertDoesNotThrow(() -> facade.listGames(auth));
    }

    @Test
    public void listGamesNegative() {
        UserData user = new UserData("user", "pass", "email");
        Assertions.assertDoesNotThrow(() -> facade.register(user));
        AuthData auth2 = new AuthData("token", "user");
        Assertions.assertThrows(ResponseException.class, () -> facade.listGames(auth2));
    }

    @Test
    public void joinGamePositive(){
        UserData user = new UserData("user", "pass", "email");
        Assertions.assertDoesNotThrow(() -> facade.register(user));
        AuthData auth = Assertions.assertDoesNotThrow(() -> facade.login(user));
        GameResponse gameRes = Assertions.assertDoesNotThrow(() -> facade.createGame("NewGame", auth.authToken()));
        JoinData joinData = new JoinData("NewGame", gameRes.gameID());
        Assertions.assertDoesNotThrow(() -> facade.joinGame(joinData, auth));
    }

    @Test
    public void joinGameNegative() {
        UserData user = new UserData("myUser", "myPass", "myEmail");
        Assertions.assertDoesNotThrow(() -> facade.register(user));
        Assertions.assertDoesNotThrow(() -> facade.login(user));
        AuthData auth = Assertions.assertDoesNotThrow(() -> facade.login(user));
        JoinData joinData = new JoinData("", 0);
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(joinData, auth));
    }

    @Test
    public void observePositive() {
        UserData user = new UserData("myUser", "myPass", "myEmail");
        Assertions.assertDoesNotThrow(() -> facade.register(user));
        Assertions.assertDoesNotThrow(() -> facade.login(user));
        AuthData auth = Assertions.assertDoesNotThrow(() -> facade.login(user));
        GameResponse gameRes = Assertions.assertDoesNotThrow(() -> facade.createGame("mygame", auth.authToken()));
        Assertions.assertDoesNotThrow(() -> facade.observe(gameRes.gameID(), auth.authToken()));
    }

    @Test
    public void observeNegative() {
        int gameID = -1;
        Assertions.assertThrows(ResponseException.class, () -> facade.observe(gameID, "invalidToken"));
    }

    @Test
    public void logoutPositive(){
        UserData user = new UserData("user", "pass", "email");
        Assertions.assertDoesNotThrow(() -> facade.register(user));
        AuthData auth = Assertions.assertDoesNotThrow(() -> facade.login(user));
        Assertions.assertDoesNotThrow(() -> facade.logout(auth.authToken()));
    }

    @Test
    public void logoutNegative() {
        Assertions.assertThrows(ResponseException.class, () -> facade.logout("invalidToken"));
    }

}
