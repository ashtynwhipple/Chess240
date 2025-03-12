package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import org.junit.jupiter.api.*;
import java.util.Collection;
import java.util.Objects;


class GameSqlDAOTests {
    private GameSqlDataAccess gameDao;

    @BeforeEach
    void setUp() {
        gameDao = new GameSqlDataAccess();
        gameDao.clearAll(); // Ensure clean DB before each test
    }

    @Test
    void positiveCreateGame() {
        Assertions.assertDoesNotThrow(() -> gameDao.createGame(1, "white", "black", "Name", new ChessGame()));
        GameData game = gameDao.getGame(1);
        Assertions.assertNotNull(game);
        Assertions.assertEquals(1, game.gameID());
        Assertions.assertEquals("Name", game.gameName());
        Assertions.assertEquals("white", game.whiteUsername());
        Assertions.assertEquals("black", game.blackUsername());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameSqlDAOTests that = (GameSqlDAOTests) o;
        return Objects.equals(gameDao, that.gameDao);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(gameDao);
    }

    @Test
    void negativeCreateGame() {
        gameDao.createGame(1, "white", "black", "Name", new ChessGame());
        Assertions.assertThrows(Exception.class, () -> gameDao.createGame(1, "otherWhite", "otherBlack", "Name", new ChessGame()));
    }

    @Test
    void positiveGetGame() {
        gameDao.createGame(1, "white", "black", "Name", new ChessGame());
        GameData game = gameDao.getGame(1);
        Assertions.assertNotNull(game);
        Assertions.assertEquals(1, game.gameID());
        Assertions.assertEquals("Name", game.gameName());
        Assertions.assertEquals("black", game.blackUsername());
        Assertions.assertEquals("white", game.whiteUsername());
    }

    @Test
    void negativeGetGame() {
        GameData game = gameDao.getGame(999);
        Assertions.assertNull(game);
    }

    @Test
    void positiveIsEmpty() {
        gameDao.createGame(1, "white", "black", "Name", new ChessGame());
        Collection<GameData> games = gameDao.listGames();
        Assertions.assertNotNull(games);
        Assertions.assertEquals(1, games.size());
    }

    @Test
    void negativeIsEmpty() {
        Collection<GameData> games = gameDao.listGames();
        Assertions.assertNotNull(games);
        Assertions.assertTrue(games.isEmpty());
    }

    @Test
    void positveListGames() {
        gameDao.createGame(1, "white1", "black1", "Name1", new ChessGame());
        gameDao.createGame(2, "white2", "black2", "Name2", new ChessGame());
        Collection<GameData> games = gameDao.listGames();
        Assertions.assertNotNull(games);
    }

    @Test
    void negativeListGames() {
        Collection<GameData> games = gameDao.listGames();
        Assertions.assertTrue(games.isEmpty());
    }


    @Test
    void testClearAllGames() {
        gameDao.createGame(1, "white1", "black1", "Name1", new ChessGame());
        gameDao.createGame(2, "white2", "black2", "Name2", new ChessGame());
        Assertions.assertFalse(gameDao.listGames().isEmpty());

        gameDao.clearAll();
        Assertions.assertTrue(gameDao.listGames().isEmpty());
    }

    @Test
    void positiveUpdateGames() {
        // Create an initial game
        gameDao.createGame(1, "white1", "black1", "Name1", new ChessGame());

        ChessGame newGame = new ChessGame();
        Assertions.assertDoesNotThrow(() ->
                newGame.makeMove(new ChessMove(new ChessPosition(2, 1), new ChessPosition(4, 1), null))
        );

        Assertions.assertDoesNotThrow(() ->
                gameDao.updateGame(1, new GameData(1, "white1", "black1", "Name1", newGame))
        );

        GameData updatedGame = gameDao.getGame(1);
        Assertions.assertNotNull(updatedGame);
        Assertions.assertEquals(newGame.getBoard(), updatedGame.game().getBoard(), "The game state should be updated");
    }


    @Test
    void negativeUpdateGames(){
        gameDao.createGame(1, "white1", "black1", "Name1", new ChessGame());

        GameData newGame = new GameData(2, "white1", "black1", "Name1", new ChessGame());

        Assertions.assertDoesNotThrow(() ->
                newGame.game().makeMove(new ChessMove(new ChessPosition(2, 1), new ChessPosition(4, 1), null))
        );

        gameDao.updateGame(2, newGame);

        Assertions.assertNotEquals(gameDao.getGame(1).game().getBoard(), newGame.game().getBoard());
    }


}
