package chess.movecalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator{
    public QueenMovesCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    int[][] directions = {
            {1, 1},
            {-1, -1},
            {-1, 1},
            {1, -1},
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1},
    };

    public Collection<ChessMove> getViableMoves(){
        return bishopQueenRookGetViableMoves(directions);
    }

}
