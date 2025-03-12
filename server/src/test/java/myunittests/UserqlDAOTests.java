package myunittests;
import dataaccess.UserSqlDataAccess;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserqlDAOTests {
    private static Server server;

    private static UserSqlDataAccess userDao;

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        userDao = new UserSqlDataAccess();
    }

    @BeforeEach
    void setUp() {
        userDao.clearAll();
    }

    @Test
    void positiveCreateUser() {
        Assertions.assertDoesNotThrow(() -> userDao.createUser("testUser", "password123", "test@example.com"));
        UserData user = userDao.getUser("testUser");
        Assertions.assertNotNull(user);
        Assertions.assertEquals("testUser", user.username());
        Assertions.assertEquals("password123", user.password());
        Assertions.assertEquals("test@example.com", user.email());
    }

    @Test
    void negativeCreateUser() {
        userDao.createUser("duplicateUser", "password123", "user@example.com");
        userDao.createUser("duplicateUser", "newPassword", "new@example.com");
        Assertions.assertNotEquals("new@example.com", userDao.getUser("duplicateUser").email());
    }

    @Test
    void positiveGetUser() {
        userDao.createUser("existingUser", "securePass", "user@example.com");
        UserData user = userDao.getUser("existingUser");
        Assertions.assertNotNull(user);
        Assertions.assertEquals("existingUser", user.username());
        Assertions.assertEquals("securePass", user.password());
        Assertions.assertEquals("user@example.com", user.email());
    }

    @Test
    void negativeGetUser() {
        userDao.createUser("duplicateUser", "password123", "user@example.com");
        Assertions.assertNull(userDao.getUser("wrongUser"));
    }

    @Test
    void positiveIsEmpty() {
        Assertions.assertTrue(userDao.isEmpty());
    }

    @Test
    void negativeIsEmpty() {
        userDao.createUser("testUser", "password123", "test@example.com");
        Assertions.assertFalse(userDao.isEmpty());
    }

    @Test
    void testClearAllUsers() {
        userDao.createUser("user1", "password1", "user1@example.com");
        userDao.createUser("user2", "password2", "user2@example.com");
        Assertions.assertFalse(userDao.isEmpty());

        userDao.clearAll();
        Assertions.assertTrue(userDao.isEmpty());
    }

}
