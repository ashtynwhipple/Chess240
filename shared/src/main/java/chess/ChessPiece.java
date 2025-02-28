package chess;

import chess.movecalculator.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        PieceType type = getPieceType();

        if(type == PieceType.KING) { //PASSED
            KingMovesCalculator calculator = new KingMovesCalculator(board, myPosition);
            return calculator.get_viable_moves();
        }else if (type == PieceType.PAWN) { //currently in progress
            PawnMovesCalculator calculator = new PawnMovesCalculator(board, myPosition);
            return calculator.getViableMoves();
        }else if (type == PieceType.ROOK) { //PASSED but I don't know why, I had to change the bounds
            RookMovesCalculator calculator = new RookMovesCalculator(board, myPosition);
            return calculator.get_viable_moves();
        }else if (type == PieceType.BISHOP) { //PASSED
            BishopMovesCalculator calculator = new BishopMovesCalculator(board, myPosition);
            return calculator.get_viable_moves();
        }else if (type == PieceType.KNIGHT) { //PASSED
            KnightMovesCalculator calculator = new KnightMovesCalculator(board, myPosition);
            return calculator.getViableMoves();
        }else if (type == PieceType.QUEEN) { //PASSED
            QueenMovesCalculator calculator = new QueenMovesCalculator(board, myPosition);
            return calculator.get_viable_moves();
        }
        return null;
    }
}
