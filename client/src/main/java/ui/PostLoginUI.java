package ui;

import exception.ResponseException;
import model.AuthData;
import server.ServerFacade;

import java.util.Scanner;

public class PostLoginUI {
    Scanner scanner;
    ServerFacade server;
    AuthData authData;


    public PostLoginUI(AuthData authData, ServerFacade server) {
        this.scanner = new Scanner(System.in);
        this.server = server;
        this.authData = authData;
    }

    public void run() {
        System.out.println(
                "\nWelcome! Type 'help' to get started"
        );

        while (true) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();

            switch (line) {
                case "help":
                    System.out.println("\nAvailable Commands:");
                    System.out.println("create <GAME> - a game");
                    System.out.println("list - games");
                    System.out.println("join <ID> [WHITE|BLACK] - a game");
                    System.out.println("observe <ID> - a game");
                    System.out.println("logout - when you are done");
                    System.out.println("quit - quit playing chess");
                    System.out.println("help = with possible commands");
                    break;
                case "create":
                    break;
                case "list":
//                    listGames();
                    break;
                case "join":
//                    join();
                    break;
                case "observe":
//                    observe();
                    break;
                case "logout":
                    System.out.println("Logging out...");
                    logout();
                    return;
                case "quit":
//                    quit();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }


    private void logout() {
        try {
            server.logout(authData.authToken());
            System.out.println("Successfully logged out.");

            PreLoginUI preLoginUI = new PreLoginUI(server);
            preLoginUI.run();
        } catch (ResponseException e) {
            System.out.println("Logout failed: " + e.getMessage());
        }
    }

}
