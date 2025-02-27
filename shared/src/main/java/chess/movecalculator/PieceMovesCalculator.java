package chess.movecalculator;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

public abstract class PieceMovesCalculator {

    protected final ChessBoard board;
    protected final ChessPosition position;

    public PieceMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public boolean isOpponent(int x, int y){
        ChessPosition place_piece_position = new ChessPosition(x,y);
        ChessPiece piece = board.getPiece(place_piece_position);
        if (piece != null){
            return !piece.getTeamColor().equals(board.getPiece(position).getTeamColor());
        }
        else {
            return false;
        }
    }

    public boolean validMove(int x, int y){
        if ( x >= 1 && x < 9 && y >= 1 && y < 9){
            ChessPosition place_piece_position = new ChessPosition(x,y);
            ChessPiece piece = board.getPiece(place_piece_position);
            return piece == null || isOpponent(x,y);
        }
        else{
            return false;
        }
    }
}
