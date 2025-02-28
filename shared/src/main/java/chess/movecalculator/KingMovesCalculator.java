package chess.movecalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalculator extends PieceMovesCalculator{

    public KingMovesCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }
    public Collection<ChessMove> getViableMoves(){
        List<ChessMove> viable_moves = new ArrayList<>();

        int[] dx = {-1,-1,-1,0,0,1,1,1};
        int[] dy = {-1,0,1,-1,1,-1,0,1};

//        List<Integer> dx2 = new int[]{};

        for (int i = 0; i < 8; i++){
            int new_x = position.getRow() + dx[i];
            int new_y = position.getColumn() + dy[i];

            if (validMove(new_x, new_y)) {
                ChessPosition new_place = new ChessPosition(new_x, new_y);
                viable_moves.add(new ChessMove(position, new_place, null));
            }
        }
        return viable_moves;
    }

}
