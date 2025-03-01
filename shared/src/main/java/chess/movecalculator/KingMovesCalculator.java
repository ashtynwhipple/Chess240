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
        List<ChessMove> viableMoves = new ArrayList<>();

        int[] dx = {-1,-1,-1,0,0,1,1,1};
        int[] dy = {-1,0,1,-1,1,-1,0,1};

//        List<Integer> dx2 = new int[]{};

        for (int i = 0; i < 8; i++){
            int newX = position.getRow() + dx[i];
            int newY = position.getColumn() + dy[i];

            if (validMove(newX, newY)) {
                ChessPosition newPlace = new ChessPosition(newX, newY);
                viableMoves.add(new ChessMove(position, newPlace, null));
            }
        }
        return viableMoves;
    }

}
