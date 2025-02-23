package MyUnitTests;

import Model.UserData;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemoryUserDAOTests {

    private static MemoryUserDAO userDAO;
    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "testPass";
    private static final String TEST_EMAIL = "test@example.com";

    @BeforeAll
    public static void init() {
        userDAO = new MemoryUserDAO();
    }

    @BeforeEach
    public void setup() {
        userDAO.clear_all();
        userDAO.createUser(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
    }

    @Test
    @Order(1)
    @DisplayName("Create User")
    public void createUser() {
        UserData user = userDAO.getUser(TEST_USERNAME);
        assertNotNull(user, "UserData should not be null");
        assertEquals(TEST_USERNAME, user.username(), "Username should match");
        assertEquals(TEST_PASSWORD, user.password(), "Password should match");
        assertEquals(TEST_EMAIL, user.email(), "Email should match");
    }

    @Test
    @Order(2)
    @DisplayName("Retrieve User Data")
    public void retrieveUserData() {
        UserData retrievedUser = userDAO.getUser(TEST_USERNAME);
        assertNotNull(retrievedUser, "Retrieved UserData should not be null");
        assertEquals(TEST_USERNAME, retrievedUser.username(), "Username should match");
        assertEquals(TEST_PASSWORD, retrievedUser.password(), "Password should match");
        assertEquals(TEST_EMAIL, retrievedUser.email(), "Email should match");
    }

    @Test
    @Order(3)
    @DisplayName("Check if User Storage is Empty")
    public void checkEmptyUserStorage() {
        userDAO.clear_all();
        assertTrue(userDAO.is_empty(), "User storage should be empty after clearing");
    }
}
