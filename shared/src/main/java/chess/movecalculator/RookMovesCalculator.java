package chess.movecalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMovesCalculator {

    private ChessBoard board;
    private ChessPosition position;

    public RookMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public boolean valid_move(int x, int y){
        if ( x >= 0 && x < 8 && y >= 0 && y < 8){ //if in bounds
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

        for (int i = position.getRow(); i <= 7 - position.getRow(); i++) {
            int newx = i;
            int newy = position.getColumn();

            if (!valid_move(newx, newy)){
                break;
            }
            ChessPosition new_place = new ChessPosition(newx, newy);
            viable_moves.add(new ChessMove(position, new_place, null));
            if (is_opponent(newx, newy)){
                break;
            }
        }


        for (int i = position.getRow(); i >= 0; i--){
            int newx = i;
            int newy = position.getColumn();

            if (!valid_move(newx, newy)){
                break;
            }
            ChessPosition new_place = new ChessPosition(newx, newy);
            viable_moves.add(new ChessMove(position, new_place, null));
            if (is_opponent(newx, newy)){
                break;
            }
        }

        for (int i = position.getColumn(); i <= 7 - position.getColumn(); i++){
            int newx = position.getRow();
            int newy = i;

            if (!valid_move(newx, newy)){
                break;
            }
            ChessPosition new_place = new ChessPosition(newx, newy);
            viable_moves.add(new ChessMove(position, new_place, null));
            if (is_opponent(newx, newy)){
                break;
            }
        }

        for (int i = position.getColumn(); i >= 0; i--){
            int newx = position.getRow();
            int newy = i;

            if (!valid_move(newx, newy)){
                break;
            }
            ChessPosition new_place = new ChessPosition(newx, newy);
            viable_moves.add(new ChessMove(position, new_place, null));
            if (is_opponent(newx, newy)){
                break;
            }

        }

        return viable_moves;
    }

}
