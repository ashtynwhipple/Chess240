package myunittests;
import model.UserData;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.*;
import passoff.model.*;
import passoff.server.TestServerFacade;
import server.Server;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserqlDAOTests {

    private static TestUser existingUser;

    private static TestUser newUser;

    private static TestCreateRequest createRequest;

    private static TestServerFacade serverFacade;
    private static Server server;

    private String existingAuth;

    private static MemoryUserDAO userDAO;

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

        userDAO = new MemoryUserDAO();


    }

    @BeforeEach
    public void setup() {
        serverFacade.clear();

        //one user already logged in
        TestAuthResult regResult = serverFacade.register(existingUser);
        existingAuth = regResult.getAuthToken();
        userDAO.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

    }

    @Test
    @Order(1)
    @DisplayName("Create User")
    public void createUser() {
        Assertions.assertNotNull(existingAuth, "Auth token should not be null");
        UserData user = userDAO.getUser(existingAuth);
        Assertions.assertNotNull(user, "UserData should not be null");
        Assertions.assertEquals(existingUser.getUsername(), user.username(), "Username should match");
        Assertions.assertEquals(existingUser.getPassword(), user.password(), "Password should match");
        Assertions.assertEquals(existingUser.getEmail(), user.email(), "Email should match");
    }

    @Test
    @Order(2)
    @DisplayName("Retrieve User Data")
    public void retrieveUserData() {
        UserData retrievedUser = userDAO.getUser(existingAuth);
        Assertions.assertNotNull(retrievedUser, "Retrieved UserData should not be null");
        Assertions.assertEquals(existingUser.getUsername(), retrievedUser.username(), "Username should match");
        Assertions.assertEquals(existingUser.getPassword(), retrievedUser.password(), "Password should match");
        Assertions.assertEquals(existingUser.getEmail(), retrievedUser.email(), "Email should match");
    }

    @Test
    @Order(3)
    @DisplayName("Check if User Storage is Empty")
    public void checkEmptyUserStorage() {
        userDAO.clearAll();
        Assertions.assertTrue(userDAO.isEmpty(), "User storage should be empty after clearing");
    }
}
