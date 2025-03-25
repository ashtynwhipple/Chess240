package ui;

import chess.ChessBoard;
import chess.ChessGame;
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
        System.out.print("Game Number: ");
        int gameNumber = scanner.nextInt();

        try {

            if (gameNumber < 1 || gameNumber > games.size()) {
                System.out.println("Invalid game number. Please enter a valid number from the list.");
                return;
            }

            GameData joinedGame = games.get(gameNumber - 1);
            int gameID = joinedGame.gameID();

            JoinData joinData = new JoinData(playerColor, gameID);
            server.joinGame(joinData, authData);
            System.out.println("Joined game:" + gameID);

            printBoard(joinedGame.game().getBoard(), Objects.equals(playerColor, "WHITE"));

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

        if (observedGame != null) {
            printBoard(observedGame.game().getBoard(), true); //is it true
        } else {
            System.out.println("Error: Game to observe could not found.");
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

    public void printBoard(ChessBoard board, boolean whitePerspective) {
        String reset = "\u001B[0m";
        String darkSquare = "\u001B[47m";
        String lightSquare = "\u001B[40m";
        String whitePieceColor = "\u001B[31m";
        String blackPieceColor = "\u001B[34m";

        System.out.println("\n  a b c d e f g h");
        System.out.println("  ----------------");

        for (int row = 0; row < 8; row++) {
            int printRow = whitePerspective ? (8 - row) : (row + 1);
            System.out.print(printRow + "| ");

            for (int col = 1; col < 9; col++) {
                boolean isDark = (row + col) % 2 != 0;
                String squareColor = isDark ? darkSquare : lightSquare;

                ChessPosition position = new ChessPosition(whitePerspective ? (8 - row) : row, col);
                ChessPiece piece = board.getPiece(position);

                String pieceSymbol = " ";
                if (piece != null) {
                    switch (piece.getPieceType()) {
                        case PAWN -> pieceSymbol = "P";
                        case ROOK -> pieceSymbol = "R";
                        case KNIGHT -> pieceSymbol = "N";
                        case BISHOP -> pieceSymbol = "B";
                        case QUEEN -> pieceSymbol = "Q";
                        case KING -> pieceSymbol = "K";
                    }
                }

                String pieceColor = (piece != null && piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? whitePieceColor : blackPieceColor;
                System.out.print(squareColor + pieceColor + pieceSymbol + reset + " ");
            }
            System.out.println("|");
        }
        System.out.println("  ----------------");
    }

}


