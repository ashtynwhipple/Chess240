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

    public Collection<ChessMove> getViableMoves(){
        List<ChessMove> viableMoves = new ArrayList<>();

        int[] dx = {1,-1,1,-1,2,2,-2,-2};
        int[] dy = {2,2,-2,-2,1,-1,1,-1};

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
