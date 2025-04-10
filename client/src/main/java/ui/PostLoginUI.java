package ui;
import chess.ChessGame;
import exception.ResponseException;
import model.*;
import server.NotificationHandler;
import server.ServerFacade;
import server.WebSocketFacade;

import java.util.*;

public class PostLoginUI extends BoardAccess implements NotificationHandler {
    Scanner scanner;

    public PostLoginUI(AuthData authData, ServerFacade server) {
        super(authData, server);
        this.scanner = new Scanner(System.in);
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
            server.createGame(gameName, authData.authToken());
            System.out.println("Created game " + gameName);
        } catch (ResponseException e){
            System.out.println("Could not create game: " + e.getMessage());
        }

    }

    private void listGames(){
        try {
            ListGameData games = server.listGames(authData);
            int count = 1;
            for (GameData game : games.games()) {
                String whitePlayer = (game.whiteUsername() != null) ? game.whiteUsername() : "open";
                String blackPlayer = (game.blackUsername() != null) ? game.blackUsername() : "open";
                System.out.println(count + ". Game Name: " + game.gameName() + " | White User: " + whitePlayer + " | Black User: " + blackPlayer);
                count++;
            }
        } catch (ResponseException e){
            System.out.println("listGames failed: " + e.getMessage());
        }
    }

    private void join(){
        this.addGames();
        listGames();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Color: ");
        String playerColor = scanner.nextLine().trim().toUpperCase();

        System.out.print("Game Number: ");
        String gameNumberInput = scanner.nextLine().trim();
        int gameNumber;

        try {
            gameNumber = Integer.parseInt(gameNumberInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid game number.");
            return;
        }

        try {

            if (gameNumber < 1 || gameNumber > games.size()) {
                System.out.println("Invalid game number. Please enter a valid number from the list.");
                return;
            }

            GameData joinedGame = games.get(gameNumber - 1);
            int gameID = joinedGame.gameID();

            JoinData joinData = new JoinData(playerColor, gameID);
            server.joinGame(joinData, authData);
            System.out.println("Joined game:" + games.get(gameNumber - 1).gameName());

            WebSocketFacade facade = new WebSocketFacade(server.getServerUrl(), this);

            if (Objects.equals(joinData.playerColor(), "WHITE")){
                facade.connect(authData.authToken(), gameID, ChessGame.TeamColor.WHITE);
                GamePlayUI gamePlayUI = new GamePlayUI(authData, server, gameNumber, "WHITE");
                gamePlayUI.run();
            } else {
                facade.connect(authData.authToken(), gameID, ChessGame.TeamColor.BLACK);
                GamePlayUI gamePlayUI = new GamePlayUI(authData, server, gameNumber, "BLACK");
                gamePlayUI.run();
            }

        } catch (ResponseException e){
            System.out.println("Join game failed: " + e.getMessage());
        }
    }

    private void observe(){
        Scanner scanner = new Scanner(System.in);

        listGames();

        System.out.print("Game Number: ");
        int gameNumber = scanner.nextInt();

        addGames();

        if (gameNumber < 1 || gameNumber > games.size()) {
            System.out.println("Invalid game number. Please enter a valid number from the list.");
            return;
        }

        GameData observedGame = games.get(gameNumber - 1);

        try{
            WebSocketFacade facade = new WebSocketFacade(server.getServerUrl(), this);

            if (observedGame != null) {
                facade.connect(authData.authToken(), gameNumber, null);
                GamePlayUI gamePlayUI = new GamePlayUI(authData, server, gameNumber, "OBSERVER");
                gamePlayUI.run();
            } else {
                System.out.println("Error: Game to observe could not found.");
            }
        } catch (ResponseException e){
            System.out.println("Observe game failed: " + e.getMessage());
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

    @Override
    public void notify(String notification) {

    }
}


