package chess.movecalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator extends PieceMovesCalculator{
    public QueenMovesCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> get_viable_moves(){
        List<ChessMove> viable_moves = new ArrayList<>();

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

        for (int[] direction: directions){

            int col = position.getColumn();
            int row = position.getRow();

            while (true){

                row += direction[0];
                col += direction[1];

                if (!validMove(row,col)){
                    break;
                }

                ChessPosition new_place = new ChessPosition(row, col);
                viable_moves.add(new ChessMove(position, new_place, null));

                if (isOpponent(row, col)){
                    break;
                }

            }

        }

        return viable_moves;
    }

}
