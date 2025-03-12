package myunittests;
import dataaccess.AuthSqlDataAccess;
import model.AuthData;
import dataaccess.MemoryAuthDAO;
import org.junit.jupiter.api.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthSqlDAOTests {

    private AuthSqlDataAccess authDao;

    @BeforeEach
    void setUp() {
        authDao = new AuthSqlDataAccess();
        authDao.clearAll();
    }

    @Test
    void positiveCreateAuth() {
        AuthData token = authDao.createAuth("testUser");
        AuthData auth = authDao.getUsername(token.authToken());
        Assertions.assertNotNull(auth);
        Assertions.assertEquals(token.authToken(), auth.authToken());
        Assertions.assertEquals("testUser", auth.username());
    }

    @Test
    void negativeCreateAuth() {
        Assertions.assertNull(authDao.getUsername(null));
    }

    @Test
    void positiveGetUser() {
        AuthData authData = authDao.createAuth("user123");
        AuthData auth = authDao.getUsername(authData.authToken());
        Assertions.assertNotNull(auth);
        Assertions.assertEquals(authData.authToken(), auth.authToken());
        Assertions.assertEquals("user123", auth.username());
    }

    @Test
    void negativeGetUser() {
        AuthData auth = authDao.getUsername("nonExistentToken");
        Assertions.assertNull(auth);
    }

    @Test
    void positiveDeleteAuth() {
        AuthData auth = authDao.createAuth("userToDelete");
        Assertions.assertNotNull(authDao.getUsername(auth.authToken()));

        authDao.deleteAuth(auth.authToken());
        Assertions.assertNull(authDao.getUsername(auth.authToken()));
    }

    @Test
    void negativeDeleteAuth() {
        Assertions.assertNull(authDao.getUsername("nonExistentToken"));
    }

    @Test
    void testClearAllAuths() {
        AuthData auth1 = authDao.createAuth("user1");
        AuthData auth2 = authDao.createAuth("user2");
        Assertions.assertNotNull(authDao.getUsername(auth1.authToken()));
        Assertions.assertNotNull(authDao.getUsername(auth2.authToken()));

        authDao.clearAll();
        Assertions.assertNull(authDao.getUsername(auth1.authToken()));
        Assertions.assertNull(authDao.getUsername(auth2.authToken()));
    }

}
