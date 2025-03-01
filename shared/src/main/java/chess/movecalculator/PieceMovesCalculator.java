package chess.movecalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class PieceMovesCalculator {

    protected final ChessBoard board;
    protected final ChessPosition position;

    public PieceMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }
    public Collection<ChessMove> bishopQueenGetViableMoves(int[][] directions) {
        List<ChessMove> viableMoves = new ArrayList<>();

        for (int[] direction : directions) {

            int col = position.getColumn();
            int row = position.getRow();

            while (true) {

                row += direction[0];
                col += direction[1];

                if (!validMove(row, col)) {
                    break;
                }

                ChessPosition newPlace = new ChessPosition(row, col);
                viableMoves.add(new ChessMove(position, newPlace, null));

                if (isOpponent(row, col)) {
                    break;
                }

            }

        }
        return viableMoves;
    }
    public boolean isOpponent(int x, int y){
        ChessPosition placePiecePosition = new ChessPosition(x,y);
        ChessPiece piece = board.getPiece(placePiecePosition);
        if (piece != null){
            return !piece.getTeamColor().equals(board.getPiece(position).getTeamColor());
        }
        else {
            return false;
        }
    }

    public boolean validMove(int x, int y){
        if ( x >= 1 && x < 9 && y >= 1 && y < 9){
            ChessPosition placePiecePosition = new ChessPosition(x,y);
            ChessPiece piece = board.getPiece(placePiecePosition);
            return piece == null || isOpponent(x,y);
        }
        else{
            return false;
        }
    }
}
