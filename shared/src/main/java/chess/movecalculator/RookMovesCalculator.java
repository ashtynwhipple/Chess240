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

    public Collection<ChessMove> getViableMoves(){
        List<ChessMove> viableMoves = new ArrayList<>();

        int[][] directions = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1},
        };

        for (int[] direction: directions){

            int col = position.getColumn();
            int row = position.getRow();

            while (true){

                row += direction[0];
                col += direction[1];

                if (!validMove(row,col)){
                    break;
                }

                ChessPosition newPlace = new ChessPosition(row, col);

                viableMoves.add(new ChessMove(position, newPlace, null));

                if (isOpponent(row, col)){
                    break;
                }

            }

        }

        return viableMoves;
    }

}
