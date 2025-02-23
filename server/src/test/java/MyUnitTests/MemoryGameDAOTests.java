package MyUnitTests;

import Model.GameData;
import chess.ChessGame;
import dataaccess.MemoryGameDAO;
import org.junit.jupiter.api.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemoryGameDAOTests {

    private static MemoryGameDAO gameDAO;
    private static final int TEST_GAME_ID = 1;
    private static final String WHITE_USERNAME = "WhitePlayer";
    private static final String BLACK_USERNAME = "BlackPlayer";
    private static final String GAME_NAME = "TestGame";
    private static ChessGame testGame;
    private static GameData testGameData;

    @BeforeAll
    public static void init() {
        gameDAO = new MemoryGameDAO();
        testGame = new ChessGame();
    }

    @BeforeEach
    public void setup() {
        gameDAO.clearGames();
        gameDAO.createGame(TEST_GAME_ID, WHITE_USERNAME, BLACK_USERNAME, GAME_NAME, testGame);
        testGameData = gameDAO.getGame(TEST_GAME_ID);
    }

    @Test
    @Order(1)
    @DisplayName("Create and Retrieve Game")
    public void createAndRetrieveGame() {
        assertNotNull(testGameData, "GameData should not be null");
        assertEquals(TEST_GAME_ID, testGameData.gameID(), "Game ID should match");
        assertEquals(WHITE_USERNAME, testGameData.whiteUsername(), "White username should match");
        assertEquals(BLACK_USERNAME, testGameData.blackUsername(), "Black username should match");
        assertEquals(GAME_NAME, testGameData.gameName(), "Game name should match");
        assertNotNull(testGameData.game(), "ChessGame object should not be null");
    }

    @Test
    @Order(2)
    @DisplayName("List Games")
    public void listGames() {
        Collection<GameData> games = gameDAO.listGames();
        assertFalse(games.isEmpty(), "Game list should not be empty");
        assertTrue(games.contains(testGameData), "Game list should contain the created game");
    }

    @Test
    @Order(3)
    @DisplayName("Update Game Data")
    public void updateGameData() {
        ChessGame newChessGame = new ChessGame();
        GameData updatedGameData = new GameData(TEST_GAME_ID, "NewWhite", "NewBlack", "UpdatedGame", newChessGame);
        gameDAO.updateGame(TEST_GAME_ID, updatedGameData);
        GameData retrievedGame = gameDAO.getGame(TEST_GAME_ID);

        assertNotNull(retrievedGame, "Updated GameData should not be null");
        assertEquals("NewWhite", retrievedGame.whiteUsername(), "Updated white username should match");
        assertEquals("NewBlack", retrievedGame.blackUsername(), "Updated black username should match");
        assertEquals("UpdatedGame", retrievedGame.gameName(), "Updated game name should match");
    }

    @Test
    @Order(4)
    @DisplayName("Check if Game Storage is Empty")
    public void checkEmptyGameStorage() {
        gameDAO.clearGames();
        assertTrue(gameDAO.is_empty(), "Game storage should be empty after clearing");
    }

    @Test
    @Order(5)
    @DisplayName("Clear All Games")
    public void clearAllGames() {
        gameDAO.createGame(2, "ExtraWhite", "ExtraBlack", "ExtraGame", new ChessGame());
        gameDAO.clear_all();
        assertTrue(gameDAO.is_empty(), "Game storage should be empty after clear_all()");
    }
}
