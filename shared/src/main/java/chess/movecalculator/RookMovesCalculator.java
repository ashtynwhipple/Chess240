package chess.movecalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMovesCalculator extends PieceMovesCalculator{


    public RookMovesCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    int[][] directions = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1},
    };

    public Collection<ChessMove> getViableMoves(){
        return bishopQueenRookGetViableMoves(directions);
    }

}
