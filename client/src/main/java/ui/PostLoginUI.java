package ui;

import model.AuthData;
import java.util.Scanner;

public class PostLoginUI {
    private final Scanner scanner;
    private final AuthData authData;

    public PostLoginUI(AuthData authData) {
        this.scanner = new Scanner(System.in);
        this.authData = authData;
    }

    public void run() {
        System.out.println(
                "\nWelcome, " + authData.username() + "! Type 'help' to get started"
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
            server.logout(authData.getAuthToken());  // Call the server logout API
            System.out.println("Successfully logged out.");

            // Transition back to PreLoginUI
            PreLoginUI preLoginUI = new PreLoginUI(server);
            preLoginUI.run();
        } catch (ResponseException e) {
            System.out.println("Logout failed: " + e.getMessage());
        }
    }


    private void viewProfile() {
        System.out.println("\n--- User Profile ---");
        System.out.println("Username: " + authData.getUsername());
        System.out.println("Email: " + authData.getEmail());
        // You may want to fetch more details from the server
    }

    private void listItems() {
        System.out.println("\nFetching items from server...");
        // Call serverFacade to fetch items here
    }
}
