package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import model.*;
import server.ServerFacade;

import java.util.*;

public class PostLoginUI {
    Scanner scanner;
    ServerFacade server;
    AuthData authData;

    List<GameData> games;


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
            GameResponse gameRes = server.createGame(gameName, authData.authToken());
            System.out.println("Created game " + gameName + "with gameID: " + gameRes.gameID());
        } catch (ResponseException e){
            System.out.println("Could not create game: " + e.getMessage());
        }

    }

    private void listGames(){
        try {
            ListGameData games = server.listGames(authData);
            for (GameData game : games.games()) {
                System.out.println(game);
            }
        } catch (ResponseException e){
            System.out.println("listGames failed: " + e.getMessage());
        }
    }

    private void addGames() {
        try {
            games = new ArrayList<>();
            ListGameData gameList = server.listGames(authData);

            games.addAll(gameList.games());

        }catch (ResponseException e) {
            System.out.println("listGames failed: " + e.getMessage());
        }
    }

    private void join(){
        addGames();
        listGames();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Color: ");
        String playerColor = scanner.nextLine();
        System.out.print("Game ID: ");
        int gameID = scanner.nextInt();

        try {
            JoinData joinData = new JoinData(playerColor, gameID);
            server.joinGame(joinData, authData);
            System.out.println("Joined game:" + gameID);

            GameData joinedGame = loopGames(gameID, "joined");

            if (joinedGame != null){
                printBoard(joinedGame.game().getBoard());
            } else {
                System.out.println("Error: Game to join could not be found.");
            }

        } catch (ResponseException e){
            System.out.println("Join game failed: " + e.getMessage());
        }
    }

    private GameData loopGames(int gameID, String messageForPrint){

        GameData chosenGame;

        for (GameData game : games) {
            if (game.gameID() == gameID) {
                String gameName = game.gameName();
                chosenGame = game;
                System.out.println("You " + messageForPrint + " " + gameName + " (ID: " + gameID + ")");
                return chosenGame;
            } else {
                System.out.println("Error: Game to not found.");
            }
        }
        return null;
    }

    private void observe(){
        Scanner scanner = new Scanner(System.in);

        listGames();

        try {
            System.out.print("Game ID: ");
            int gameID = scanner.nextInt();

            JoinData joinData = new JoinData(null, gameID);
            server.joinGame(joinData, authData);

            addGames();

            GameData observedGame = loopGames(gameID, "are observing");

            if (observedGame != null) {
                printBoard(observedGame.game().getBoard());
            } else {
                System.out.println("Error: Game to observe could not found.");
            }

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

    public void printBoard(ChessBoard board) {
        System.out.println("\n  a b c d e f g h");
        System.out.println("  ----------------");

        for (int row = 0; row < 8; row++) {
            System.out.print((8 - row) + "| ");
            for (int col = 0; col < 8; col++) {
                ChessPosition position = new ChessPosition(row,col);
                ChessPiece piece = board.getPiece(position);
                System.out.print(piece + " ");
            }
            System.out.println("|");
        }
        System.out.println("  ----------------");
    }

}
