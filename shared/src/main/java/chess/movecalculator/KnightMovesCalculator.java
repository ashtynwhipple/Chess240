package chess.movecalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalculator extends PieceMovesCalculator{

    public KnightMovesCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    int[] dx = {1,-1,1,-1,2,2,-2,-2};
    int[] dy = {2,2,-2,-2,1,-1,1,-1};

    public Collection<ChessMove> getViableMoves(){return getKingKnightViableMoves(dx,dy);}

}
