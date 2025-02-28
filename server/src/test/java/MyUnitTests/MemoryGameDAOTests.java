package MyUnitTests;
import Model.GameData;
import chess.ChessGame;
import dataaccess.MemoryGameDAO;
import org.junit.jupiter.api.*;
import passoff.model.*;
import passoff.server.TestServerFacade;
import server.Server;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
//import Model.GameData;
//import chess.ChessGame;
//import dataaccess.MemoryGameDAO;
//import org.junit.jupiter.api.*;
//
//import java.util.Collection;
//
//import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemoryGameDAOTests {

    private static MemoryGameDAO gameDAO;

    private static TestUser existingUser;

    private static TestUser newUser;

    private static TestCreateRequest createRequest;

    private static TestServerFacade serverFacade;
    private static Server server;

    private String existingAuth;
    private static final int TEST_GAME_ID = 1;
    private static final String WHITE_USERNAME = "WhitePlayer";
    private static final String BLACK_USERNAME = "BlackPlayer";
    private static final String GAME_NAME = "TestGame";
    private static ChessGame testGame;
    private static GameData testGameData;

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        serverFacade = new TestServerFacade("localhost", Integer.toString(port));

        existingUser = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");

        newUser = new TestUser("NewUser", "newUserPassword", "nu@mail.com");

        createRequest = new TestCreateRequest("testGame");
        gameDAO = new MemoryGameDAO();
        testGame = new ChessGame();
    }

    @BeforeEach
    public void setup() {
        serverFacade.clear();
        gameDAO.clearGames();
        gameDAO.createGame(TEST_GAME_ID, WHITE_USERNAME, BLACK_USERNAME, GAME_NAME, testGame);
        testGameData = gameDAO.getGame(TEST_GAME_ID);
    }

    @Test
    @Order(1)
    @DisplayName("Create and Retrieve Game")
    public void createAndRetrieveGame() {
        Assertions.assertNotNull(testGameData, "GameData should not be null");
        Assertions.assertEquals(TEST_GAME_ID, testGameData.gameID(), "Game ID should match");
        Assertions.assertEquals(WHITE_USERNAME, testGameData.whiteUsername(), "White username should match");
        Assertions.assertEquals(BLACK_USERNAME, testGameData.blackUsername(), "Black username should match");
        Assertions.assertEquals(GAME_NAME, testGameData.gameName(), "Game name should match");
        Assertions.assertNotNull(testGameData.game(), "ChessGame object should not be null");
    }

    @Test
    @Order(2)
    @DisplayName("List Games")
    public void listGames() {
        Collection<GameData> games = gameDAO.listGames();
        Assertions.assertFalse(games.isEmpty(), "Game list should not be empty");
        Assertions.assertTrue(games.contains(testGameData), "Game list should contain the created game");
    }

    @Test
    @Order(3)
    @DisplayName("Update Game Data")
    public void updateGameData() {
        ChessGame newChessGame = new ChessGame();
        GameData updatedGameData = new GameData(TEST_GAME_ID, "NewWhite", "NewBlack", "UpdatedGame", newChessGame);
        gameDAO.updateGame(TEST_GAME_ID, updatedGameData);
        GameData retrievedGame = gameDAO.getGame(TEST_GAME_ID);

        Assertions.assertNotNull(retrievedGame, "Updated GameData should not be null");
        Assertions.assertEquals("NewWhite", retrievedGame.whiteUsername(), "Updated white username should match");
        Assertions.assertEquals("NewBlack", retrievedGame.blackUsername(), "Updated black username should match");
        Assertions.assertEquals("UpdatedGame", retrievedGame.gameName(), "Updated game name should match");
    }

    @Test
    @Order(4)
    @DisplayName("Check if Game Storage is Empty")
    public void checkEmptyGameStorage() {
        gameDAO.clearGames();
        Assertions.assertTrue(gameDAO.is_empty(), "Game storage should be empty after clearing");
    }

    @Test
    @Order(5)
    @DisplayName("Clear All Games")
    public void clearAllGames() {
        gameDAO.createGame(2, "ExtraWhite", "ExtraBlack", "ExtraGame", new ChessGame());
        gameDAO.clear_all();
        Assertions.assertTrue(gameDAO.is_empty(), "Game storage should be empty after clear_all()");
    }
}
