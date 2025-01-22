package chess.movecalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalculator {

    private ChessBoard board;
    private ChessPosition position;

    public KnightMovesCalculator(ChessBoard board, ChessPosition position) {
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
        return !piece.getTeamColor().equals(board.getPiece(position).getTeamColor());
    }

    public Collection<ChessMove> get_viable_moves(){
        List<ChessMove> viable_moves = new ArrayList<>();

        int[] dx = {1,-1,1,-1,2,2,-2,-2};
        int[] dy = {2,2,-2,-2,1,-1,1,-1};

        for (int i = 0; i < 8; i++){
            int newx = position.getRow() + dx[i];
            int newy = position.getColumn() + dy[i];

            if (valid_move(newx, newy)) {
                ChessPosition new_place = new ChessPosition(newx, newy);
                viable_moves.add(new ChessMove(position, new_place, null));
            }
        }

        return viable_moves;
    }

}
