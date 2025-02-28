package myunittests;
import model.AuthData;
import dataaccess.MemoryAuthDAO;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemoryAuthDAOTests {

    private static MemoryAuthDAO authDAO;
    private static final String TEST_USERNAME = "testUser";
    private static AuthData testAuth;

    @BeforeAll
    public static void init() {
        authDAO = new MemoryAuthDAO();
    }

    @BeforeEach
    public void setup() {
        authDAO.clearAuth();
        testAuth = authDAO.createAuth(TEST_USERNAME);
    }

    @Test
    @Order(1)
    @DisplayName("Create Authentication Token")
    public void createAuthToken() {
        assertNotNull(testAuth, "AuthData should not be null");
        assertEquals(TEST_USERNAME, testAuth.username(), "Username should match");
        assertNotNull(testAuth.authToken(), "Auth token should not be null");
    }

    @Test
    @Order(2)
    @DisplayName("Retrieve Authentication Data")
    public void retrieveAuthData() {
        AuthData retrievedAuth = authDAO.getUsername(testAuth.authToken());
        assertNotNull(retrievedAuth, "Retrieved AuthData should not be null");
        assertEquals(testAuth.username(), retrievedAuth.username(), "Username should match");
        assertEquals(testAuth.authToken(), retrievedAuth.authToken(), "Auth token should match");
    }

    @Test
    @Order(3)
    @DisplayName("Delete Authentication Token")
    public void deleteAuthToken() {
        authDAO.deleteAuth(testAuth.authToken());
        assertNull(authDAO.getUsername(testAuth.authToken()), "AuthData should be null after deletion");
    }

    @Test
    @Order(4)
    @DisplayName("Check if Auth Storage is Empty")
    public void checkEmptyAuthStorage() {
        authDAO.clearAuth();
        assertTrue(authDAO.isEmpty(), "Auth storage should be empty after clearing");
    }

    @Test
    @Order(5)
    @DisplayName("Clear All Auth Data")
    public void clearAllAuthData() {
        authDAO.createAuth("anotherUser");
        authDAO.clearAll();
        assertTrue(authDAO.isEmpty(), "Auth storage should be empty after clear_all()");
    }
}
