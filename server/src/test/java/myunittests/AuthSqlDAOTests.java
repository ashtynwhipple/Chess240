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
        authDao.createAuth("user1");
        Assertions.assertThrows(Exception.class, () -> authDao.createAuth("user1"));
    }
}
