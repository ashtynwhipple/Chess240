package chess.movecalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition position;

    public QueenMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public boolean valid_move(int x, int y){
        if ( x >= 1 && x < 9 && y >= 1 && y < 9){ //if in bounds
            ChessPosition place_piece_position = new ChessPosition(x,y); //get piece position
            ChessPiece piece = board.getPiece(place_piece_position); //find piece at position
            return piece == null || is_opponent(x,y); //return true if there is no piece there or if the opponent is there
        }
        else{
            return false;
        }
    }

    public boolean is_opponent(int x,int y){
        ChessPosition place_piece_position = new ChessPosition(x,y);
        ChessPiece piece = board.getPiece(place_piece_position);
        return piece != null && !piece.getTeamColor().equals(board.getPiece(position).getTeamColor()); //take into account if it is null
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

                if (!valid_move(row,col)){
                    break;
                }

                ChessPosition new_place = new ChessPosition(row, col);
                viable_moves.add(new ChessMove(position, new_place, null));

                if (is_opponent(row, col)){
                    break;
                }

            }

        }

        return viable_moves;
    }

}
