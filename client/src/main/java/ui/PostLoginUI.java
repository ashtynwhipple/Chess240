package ui;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinData;
import model.ListGameData;
import server.ServerFacade;

import java.util.Collection;
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
                    System.out.println("play <ID> [WHITE|BLACK] - a game");
                    System.out.println("observe <ID> - a game");
                    System.out.println("logout - when you are done");
                    System.out.println("help - with possible commands");
                    break;
                case "create":
                    create();
                    break;
                case "list":
                    listGames();
                    break;
                case "play":
                    join();
                    break;
                case "observe":
                    observe();
                    break;
                case "logout":
                    System.out.println("Logging out...");
                    logout();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void create(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Game Name: ");
        String gameName = scanner.nextLine();

        try {
            String gameID = server.createGame(gameName, authData.authToken());
            System.out.println("Created game " + gameName + "with gameID: " + gameID);
        } catch (ResponseException e){
            System.out.println("Could not create game: " + e.getMessage());
        }

    }

    private void listGames(){
        try {
            ListGameData games = server.listGames(authData.authToken());
            for (GameData game : games.games()) {
                System.out.println(game);
            }
        } catch (ResponseException e){
            System.out.println("listGames failed: " + e.getMessage());
        }
    }

    private void join(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Color: ");
        String playerColor = scanner.nextLine();
        System.out.print("Game ID: ");
        int gameID = scanner.nextInt();

        try {
            JoinData joinData = new JoinData(playerColor, gameID);
            server.joinGame(joinData);
            System.out.println("Joined game:" + gameID);
        } catch (ResponseException e){
            System.out.println("Join game failed: " + e.getMessage());
        }
    }

    private void observe(){
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Game ID: ");
            int gameID = scanner.nextInt();

            server.observe(gameID, authData.authToken());
            System.out.println("You are observing game " + gameID);
        } catch (ResponseException e){
            System.out.println("Observe failed: " + e.getMessage());
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
