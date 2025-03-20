package client;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;


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
    }

    @Test
    public void registerPositive() {
        UserData user = new UserData("user", "pass", "email");
        Assertions.assertDoesNotThrow(() -> facade.register(user));
    }

    @Test
    public void registerNegative() {
        UserData user = new UserData("", "", "");
        Assertions.assertThrows(ResponseException.class, () -> facade.register(user));
    }

    @Test
    public void loginPositive() throws ResponseException {
        UserData user = new UserData("user", "pass", "email");
        facade.register(user);
        Assertions.assertDoesNotThrow(() -> facade.login(user));
    }

    @Test
    public void loginNegative() {
        UserData user = new UserData("wrongUser", "wrongPass", "wrongEmail");
        Assertions.assertThrows(ResponseException.class, () -> facade.login(user));
    }

    @Test
    public void createGamePositive() {
        String authToken = "validToken";
        Assertions.assertDoesNotThrow(() -> facade.createGame("NewGame", authToken));
    }

    @Test
    public void createGameNegative() {
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame("", "invalidToken"));
    }

    @Test
    public void listGamesPositive() {
        String authToken = "validToken";
        Assertions.assertDoesNotThrow(() -> facade.listGames(authToken));
    }

    @Test
    public void listGamesNegative() {
        Assertions.assertThrows(ResponseException.class, () -> facade.listGames("invalidToken"));
    }

    @Test
    public void joinGamePositive(){
        JoinData joinData = new JoinData("validGame", 123);
        Assertions.assertDoesNotThrow(() -> facade.joinGame(joinData));
    }

    @Test
    public void joinGameNegative() {
        JoinData joinData = new JoinData("", 0);
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(joinData));
    }

    @Test
    public void observePositive() {
        UserData user = new UserData("myUser", "myPass", "myEmail");
        Assertions.assertDoesNotThrow(() -> facade.register(user));
        Assertions.assertDoesNotThrow(() -> facade.login(user));
        AuthData auth = Assertions.assertDoesNotThrow(() -> facade.login(user));
        int gameID = Assertions.assertDoesNotThrow(() -> facade.createGame("mygame", auth.authToken()));
        Assertions.assertDoesNotThrow(() -> facade.observe(gameID, auth.authToken()));
    }

    @Test
    public void observeNegative() {
        int gameID = -1;
        Assertions.assertThrows(ResponseException.class, () -> facade.observe(gameID, "invalidToken"));
    }

    @Test
    public void logoutPositive(){
        String authToken = "validToken";
        Assertions.assertDoesNotThrow(() -> facade.logout(authToken));
    }

    @Test
    public void logoutNegative() {
        Assertions.assertThrows(ResponseException.class, () -> facade.logout("invalidToken"));
    }

}
