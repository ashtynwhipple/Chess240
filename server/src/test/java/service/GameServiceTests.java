package service;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.userservice.RegisterService;
import chess.ChessGame;
import dataaccess.*;
import exception.StatusException;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.ArrayList;
import java.util.HashMap;

public class GameServiceTests {
    private static Server server;
    private static GameData testGame;
    private static UserData user;
    private static GameService gameService;
    private static RegisterService registerService;

    private static UserDAO mockUserDAO;
    private static AuthDAO mockAuthDAO;
    private static GameDAO mockGameDAO;

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        testGame = new GameData(1234,"white","black","testGame",new ChessGame());
        user = new UserData("ashtyn", "pass", "e.com");
    }

    @BeforeEach
    public void setup() {
        mockAuthDAO = new MemoryAuthDAO();
        mockUserDAO = new MemoryUserDAO();
        mockGameDAO = new MemoryGameDAO();
        registerService = new RegisterService(mockUserDAO, mockAuthDAO);
        gameService = new GameService(mockAuthDAO, mockGameDAO);
    }

    @Test
    @DisplayName("Positive create Game")
    void listGamesTestPositive() throws StatusException {
        AuthData result = registerService.register(user);
        int gameID = gameService.createGame(result.authToken(), testGame);
        Assertions.assertEquals("testGame", mockGameDAO.getGame(gameID).gameName());
    }

    @Test
    @DisplayName("Negative create Game")
    void createGameTestNegative(){
        Assertions.assertThrows(StatusException.class, () -> gameService.createGame("badAuthToken",testGame));
    }

    @Test
    @DisplayName("Positive list Games")
    void listGameTestPositive() throws StatusException {
        AuthData result = registerService.register(user);
        int id = gameService.createGame(result.authToken(), testGame);

        HashMap<Integer, GameData> expectedGames = new HashMap<>();
        expectedGames.put(id, mockGameDAO.getGame(id));

        Assertions.assertEquals(
                new ArrayList<>(gameService.listGames(result.authToken()).games()),
                new ArrayList<>(expectedGames.values())
        );
    }

    @Test
    @DisplayName("Negative list Games")
    void listGameTestNegative() throws StatusException {
        AuthData result = registerService.register(user);
        int id = gameService.createGame(result.authToken(), testGame);

        HashMap<Integer, GameData> expectedGames = new HashMap<>();
        expectedGames.put(id, testGame);

        Assertions.assertNotEquals(
                new ArrayList<>(gameService.listGames(result.authToken()).games()),
                new ArrayList<>(expectedGames.values())
        );
    }

    @Test
    @DisplayName("Positive Join Game")
    void joinGameTestPositive() throws StatusException {
        AuthData result = registerService.register(user);
        int id = gameService.createGame(result.authToken(), testGame);

        gameService.joinGame(result.authToken(), id, "WHITE");

        Assertions.assertEquals(mockGameDAO.getGame(id).whiteUsername(), mockAuthDAO.getUsername(result.authToken()).username());

    }

    @Test
    @DisplayName("Negative Join Game")
    void joinGameTestNegative() throws StatusException{
        AuthData result = registerService.register(user);
        int id = gameService.createGame(result.authToken(), testGame);

        gameService.joinGame(result.authToken(), id, "BLACK");

        Assertions.assertNotEquals(mockGameDAO.getGame(id).whiteUsername(), mockAuthDAO.getUsername(result.authToken()).username());

    }

    @Test
    @Order(6)
    @DisplayName("Clear positive")
    void clearTestPositive() throws StatusException {

        AuthData result = registerService.register(user);
        gameService.createGame(result.authToken(), testGame);

        assert (!mockGameDAO.isEmpty());
        assert (!mockAuthDAO.isEmpty());

        mockAuthDAO.clearAll();
        mockGameDAO.clearAll();

        assert(mockGameDAO.isEmpty());
    }

}
