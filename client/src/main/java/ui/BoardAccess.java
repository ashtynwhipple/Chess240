package ui;

import chess.*;
import exception.ResponseException;
import model.*;

import server.ServerFacade;

import java.util.*;

public class BoardAccess {

    protected List<GameData> games;
    protected ServerFacade server;
    protected AuthData authData;

    public BoardAccess(AuthData authData, ServerFacade server) {
        this.authData = authData;
        this.server = server;
        this.games = new ArrayList<>();
    }

    public void addGames() {
        try {
            ListGameData gameList = server.listGames(authData);
            games.clear();  // Clear old games first
            games.addAll(gameList.games());
        } catch (ResponseException e) {
            System.out.println("listGames failed: " + e.getMessage());
        }
    }

    public void printBoard(ChessBoard board, boolean whitePerspective) {
        printBoard(board, whitePerspective, Set.of());
    }

    public void printBoard(ChessBoard board, boolean whitePerspective, Set<ChessPosition> highlights) {
        String reset = "\u001B[0m";
        String darkSquare = "\u001B[47m";
        String lightSquare = "\u001B[40m";
        String whitePieceColor = "\u001B[31m";
        String blackPieceColor = "\u001B[34m";
        String highlightColor = "\u001B[42m";

        if (whitePerspective){
            System.out.println("\n   a   b   c   d   e   f   g   h ");
        } else{
            System.out.println("\n   h   g   f   e   d   c   b   a ");
        }

        System.out.println("  ---------------------------------");

        for (int row = 0; row < 8; row++) {
            int printRow = whitePerspective ? (8 - row) : (row + 1);
            System.out.print(printRow + "| ");

            for (int col = 1; col < 9; col++) {
                int printCol = whitePerspective ? (col) : (9 - col);
                ChessPosition pos = new ChessPosition(whitePerspective ? (8 - row) : (1 + row), printCol);
                boolean isDark = (row + col) % 2 != 0;
                String squareColor = highlights.contains(pos) ? highlightColor : (isDark ? darkSquare : lightSquare);

                ChessPiece piece = board.getPiece(pos);
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
                System.out.print(squareColor + pieceColor + " " + pieceSymbol + " " + reset + " ");
            }
            System.out.println("|");
        }
        System.out.println("  ---------------------------------");
    }
}
