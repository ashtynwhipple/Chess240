package ui;

import chess.*;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import server.NotificationHandler;
import server.ServerFacade;
import server.WebSocketFacade;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

import java.util.*;

public class GamePlayUI extends BoardAccess implements NotificationHandler {
    private final Scanner scanner;
    private final ServerFacade server;
    private final AuthData authData;
    private final String role;
    private boolean running;
    private final GameData game;

    public GamePlayUI(AuthData authData, ServerFacade server, GameData game, String role) {
        super(authData, server);
        this.scanner = new Scanner(System.in);
        this.server = server;
        this.authData = authData;
        this.game = game;
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
                    System.out.println("Invalid option. Type 'help' for more options.");
            }
        }
    }

    private void redrawBoard() {
        boolean whitePerspective = role.equals("WHITE") || role.equals("OBSERVER");

        if (game == null || game.game().getBoard() == null) {
            System.out.println("Error: Invalid game ID. Returning to menu...");
            running = false;
            new PostLoginUI(authData, server).run();
        } else {
            printBoard(game.game().getBoard(), whitePerspective);
        }
    }

    private void leaveGame() {
        System.out.println("Leaving game...");
        try{
            WebSocketFacade facade = new WebSocketFacade(server.getServerUrl(), this);
            facade.leave(authData.authToken(), game.gameID());
        } catch (ResponseException e) {
            System.out.println("Leave game failed: " + e.getMessage());
        }
        running = false;
        new PostLoginUI(authData, server).run();
    }

    private void makeMove() {

        String prompt = "Enter Move:";
        System.out.printf("\n[%s] >>> ", prompt);
        Scanner scanner = new Scanner(System.in);
        String[] input = scanner.nextLine().split(" ");

        if (input.length >= 3 && input[1].matches("[a-h][1-8]") && input[2].matches("[a-h][1-8]")) {
            ChessPosition from = new ChessPosition(input[1].charAt(1) - '0', input[1].charAt(0) - ('a'-1));
            ChessPosition to = new ChessPosition(input[2].charAt(1) - '0',input[2].charAt(0) - ('a'-1));

            ChessPiece.PieceType promotion = null;
            if (input.length == 4) {
                promotion = getPieceType(input[3]);
                if (promotion == null) {
                    System.out.println("Please provide proper promotion piece name (ex: 'knight')");
                    printMakeMove();
                }
            }

            try{
                WebSocketFacade facade = new WebSocketFacade(server.getServerUrl(), this);
                facade.makeMove(authData.authToken(), game.gameID(), new ChessMove(from, to, promotion));
            } catch (ResponseException e) {
                System.out.println("Error: " + e.getMessage());
            }

        }
        else {
            printMakeMove();
        }
    }

    public ChessPiece.PieceType getPieceType(String name) {
        return switch (name.toUpperCase()) {
            case "QUEEN" -> ChessPiece.PieceType.QUEEN;
            case "BISHOP" -> ChessPiece.PieceType.BISHOP;
            case "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
            case "ROOK" -> ChessPiece.PieceType.ROOK;
            case "PAWN" -> ChessPiece.PieceType.PAWN;
            default -> null;
        };
    }

    private void printMakeMove() {
        System.out.println("move <from> <to> <promotion_piece> - make a move (Promotion piece should only be used when a move will promote a pawn)");
    }

    private void resign() {
        System.out.print("Are you sure you want to resign? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        if (response.equals("yes")) {
            try {
                WebSocketFacade facade = new WebSocketFacade(server.getServerUrl(), this);
                facade.resign(authData.authToken(), game.gameID());
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

            Collection<ChessMove> legalMoves = game.game().validMoves(position);

            Set<ChessPosition> highlights = new HashSet<>();
            for (ChessMove move : legalMoves) {
                highlights.add(move.getEndPosition());
            }
            highlights.add(position);

            boolean whitePerspective = role.equals("WHITE") || role.equals("OBSERVER");
            printBoard(game.game().getBoard(), whitePerspective, highlights);

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
            case ERROR -> errorHandle(new Gson().fromJson(message, ErrorMessage.class));
        }
    }

    public void errorHandle(ErrorMessage errorMessage){
        System.out.println(errorMessage.getErrorMessage());
    }

    public void boardHandle(LoadGame loadGame){
        boolean whitePerspective = role.equals("WHITE") || role.equals("OBSERVER");
        printBoard(loadGame.getGame().getBoard(), whitePerspective);
    }

    public void printMessage(Notification notification){
        System.out.println(notification.getMessage());
    }

}