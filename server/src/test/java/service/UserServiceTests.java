package service;
import Model.AuthData;
import Model.UserData;
import Service.UserService.LoginService;
import Service.UserService.LogoutService;
import Service.UserService.RegisterService;
import dataaccess.*;
import exception.StatusException;
import org.junit.jupiter.api.*;
import server.Server;


public class UserServiceTests {
    private static Server server;
    private static UserData testUser;

    private static LoginService loginService;

    private static RegisterService registerService;

    private static LogoutService logoutService;

    private static UserDAO mockUserDAO;
    private static AuthDAO mockAuthDAO;

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        testUser = new UserData("ashtyn", "pass", "e.com");
    }

    @BeforeEach
    public void setup() {
        mockAuthDAO = new MemoryAuthDAO();
        mockUserDAO = new MemoryUserDAO();
        loginService = new LoginService(mockUserDAO, mockAuthDAO);
        registerService = new RegisterService(mockUserDAO, mockAuthDAO);
        logoutService = new LogoutService(mockAuthDAO);
    }

    @Test
    @Order(1)
    @DisplayName("Positive register")
    void registerUserTestPositive() throws StatusException {
        AuthData resultAuth = registerService.register(testUser);
        Assertions.assertEquals(mockAuthDAO.getUsername(resultAuth.authToken()), resultAuth);
    }

    @Test
    @Order(2)
    @DisplayName("Negative register")
    void registerUserTestNegative() throws StatusException {
        registerService.register(testUser);
        Assertions.assertThrows(StatusException.class, () -> registerService.register(testUser));
    }

    @Test
    @Order(3)
    @DisplayName("Positive login")
    void loginUserTestPositive() throws StatusException {
        registerService.register(testUser);
        AuthData resultAuth = loginService.login(testUser);
        Assertions.assertEquals(mockAuthDAO.getUsername(resultAuth.authToken()), resultAuth);
    }

    @Test
    @Order(4)
    @DisplayName("Negative login")
    void loginUserTestNegative() throws StatusException {
        UserData badUser = new UserData("badUsername", "pass", "e.com");
        registerService.register(testUser);
        Assertions.assertThrows(StatusException.class, () -> loginService.login(badUser));
    }

    @Test
    @Order(5)
    @DisplayName("Positive logout")
    void logoutUserTestPositive() throws StatusException {
        registerService.register(testUser);
        AuthData resultAuth = loginService.login(testUser);
        logoutService.logout(resultAuth.authToken());
        Assertions.assertNull(mockAuthDAO.getUsername(resultAuth.authToken()));
    }

    @Test
    @Order(6)
    @DisplayName("Negative logout")
    void logoutUserTestNegative() throws StatusException {
        registerService.register(testUser);
        AuthData resultAuth = loginService.login(testUser);
        logoutService.logout(resultAuth.authToken());
        Assertions.assertThrows(StatusException.class, () -> logoutService.logout("invalidAuthToken"));
    }

    @Test
    @Order(6)
    @DisplayName("Clear positive")
    void ClearTestPositive() throws StatusException {
        registerService.register(testUser);
        loginService.login(testUser);

        assert (!mockUserDAO.is_empty());
        assert (!mockAuthDAO.is_empty());

        mockUserDAO.clear_all();
        mockAuthDAO.clear_all();

        assert(mockAuthDAO.is_empty());
        assert(mockUserDAO.is_empty());
    }

}
