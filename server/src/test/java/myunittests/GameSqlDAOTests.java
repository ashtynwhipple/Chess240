package myunittests;
import dataaccess.GameSqlDataAccess;
import model.GameData;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.Server;
import java.util.Collection;
import dataaccess.DatabaseManager;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameSqlDAOTests {

    private static GameSqlDataAccess gameDAO;
    private static Server server;
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

//        DatabaseManager.createDatabase();
        gameDAO = new GameSqlDataAccess();
        testGame = new ChessGame();
    }

    @BeforeEach
    public void setup() {
//        DatabaseManager.createDatabase();
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
        Assertions.assertTrue(gameDAO.isEmpty(), "Game storage should be empty after clearing");
    }

    @Test
    @Order(5)
    @DisplayName("Clear All Games")
    public void clearAllGames() {
        gameDAO.createGame(2, "ExtraWhite", "ExtraBlack", "ExtraGame", new ChessGame());
        gameDAO.clearAll();
        Assertions.assertTrue(gameDAO.isEmpty(), "Game storage should be empty after clear_all()");
    }
}
