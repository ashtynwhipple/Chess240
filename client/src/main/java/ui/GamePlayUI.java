package ui;

import chess.*;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import server.NotificationHandler;
import server.ServerFacade;
import websocket.commands.Connect;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.util.*;

public class GamePlayUI extends BoardAccess implements NotificationHandler {
    private final Scanner scanner;
    private final ServerFacade server;
    private final AuthData authData;
    private final int gameNumber;
    private final String role; // "WHITE", "BLACK", or "OBSERVER"
    private boolean running;

    public GamePlayUI(AuthData authData, ServerFacade server, int gameNumber, String role) {
        super(authData, server);
        this.scanner = new Scanner(System.in);
        this.server = server;
        this.authData = authData;
        this.gameNumber = gameNumber;
        this.role = role;
        this.running = true;
    }

    public void run() {
        System.out.println("\nEntered Game! Type 'help' for available commands.");

        while (running) {
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "help":
                    System.out.println("\nAvailable Commands:");
                    System.out.println("help - with possible commands");
                    System.out.println("redraw chess board - Redraws the chess board");
                    System.out.println("leave - Leave the game");
                    System.out.println("make move - Input a move to make");
                    System.out.println("resign - Resign from the game");
                    System.out.println("highlight legal moves - Show legal moves for a selected piece");
                    break;
                case "redraw chess board":
                    redrawBoard();
                    break;
                case "leave":
                    leaveGame();
                    break;
                case "make move":
                    makeMove();
                    break;
                case "resign":
                    resign();
                    break;
                case "highlight legal moves":
                    highlightLegalMoves();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void redrawBoard() {
        boolean whitePerspective = role.equals("WHITE") || role.equals("OBSERVER");
        printBoard(games.get(gameNumber).game().getBoard(), whitePerspective);
    }

    private void leaveGame() {
        System.out.println("Leaving game...");
        running = false;
        new PostLoginUI(authData, server).run();
    }

    private void makeMove() {
        try {
            System.out.print("From: ");
            String from = scanner.nextLine();
            System.out.print("To: ");
            String to = scanner.nextLine();

            ChessPosition start = new ChessPosition(8 - (from.charAt(1) - '1'), from.charAt(0) - 'a' + 1);
            ChessPosition end = new ChessPosition(8 - (to.charAt(1) - '1'), to.charAt(0) - 'a' + 1);
            ChessMove move = new ChessMove(start, end, null); //how do I deal with promotion pieces here?

            games.get(gameNumber).game().makeMove(move);

            System.out.println("Move made.");
            redrawBoard();
        } catch (Exception e) {
            System.out.println("Invalid move or error: " + e.getMessage());
        }
    }

    private void resign() {
        System.out.print("Are you sure you want to resign? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        if (response.equals("yes")) {
            try {
                server.resign(authData, gameNumber);
                System.out.println("You have resigned.");
                System.out.println("Type 'help' for more options");
            } catch (ResponseException e) {
                System.out.println("Resign failed: " + e.getMessage());
            }
        }
    }

    private void highlightLegalMoves() {
        try {
            System.out.print("Position to check (e.g. e2): ");
            String pos = scanner.nextLine();
            ChessPosition position = new ChessPosition(8 - (pos.charAt(1) - '1'), pos.charAt(0) - 'a' + 1);

            GameData gameData = this.games.get(gameNumber);
            Collection<ChessMove> legalMoves = gameData.game().validMoves(position);

            Set<ChessPosition> highlights = new HashSet<>();
            for (ChessMove move : legalMoves) {
                highlights.add(move.getEndPosition());
            }
            highlights.add(position);

            boolean whitePerspective = role.equals("WHITE") || role.equals("OBSERVER");
            printBoard(gameData.game().getBoard(), whitePerspective, highlights);

        } catch (Exception e) {
            System.out.println("Could not highlight moves: " + e.getMessage());
        }
    }

    @Override
    public void notify(String message) {
        ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
        switch (notification.getServerMessageType()) {
            case NOTIFICATION -> printMessage(new Gson().fromJson(message, Notification.class));
            case LOAD_GAME -> boardHandle(new Gson().fromJson(message, LoadGame.class));
            case ERROR -> errorHandle(new Gson().fromJson(message, Error.class));
        }
    }

    public void errorHandle(Error error){
        System.out.println(error.getMessage());
    }

    public void boardHandle(LoadGame loadGame){
        boolean whitePerspective = role.equals("WHITE") || role.equals("OBSERVER");
        printBoard(loadGame.getGame().getBoard(), whitePerspective);
    }

    public void printMessage(Notification notification){
        System.out.println(notification.getMessage());
    }
}