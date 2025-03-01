package chess.movecalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator extends PieceMovesCalculator{

    public BishopMovesCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    int[][] directions = {
                {1, 1},
                {-1, -1},
                {-1, 1},
                {1, -1},
        };

    public Collection<ChessMove> getViableMoves(){
        return bishopQueenGetViableMoves(directions);
    }

}
