package chess.movecalculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator {

    private ChessBoard board;
    private ChessPosition position;

    public PawnMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public boolean isinbounds(int x, int y){
        return ( x >= 0 && x < 8 && y >= 0 && y < 8);
    }

    public boolean valid_move(int x, int y){
        if (isinbounds(x,y)){ //if in bounds
            ChessPosition place_piece_position = new ChessPosition(x,y); //get piece position
            ChessPiece piece = board.getPiece(place_piece_position); //find piece at position
            return piece == null; //return true if there is no piece there
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

    // public // create function to loop through promotion pieces and then return void after adding to collection

    public Collection<ChessMove> get_viable_moves(){
        List<ChessMove> viable_moves = new ArrayList<>();

        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        // create a new variable for direction for white and black

        if ((position.getRow() == 1 && color == ChessGame.TeamColor.WHITE) || (position.getRow() == 6 && color == ChessGame.TeamColor.BLACK)){
            int newx;
            if (color == ChessGame.TeamColor.WHITE){
                newx = position.getRow() + 1;
            } else {
                newx = position.getRow() -1;
            }
            int newy = position.getColumn();

            if (valid_move(newx, newy)) {
                ChessPosition new_place = new ChessPosition(newx, newy);
                viable_moves.add(new ChessMove(position, new_place, null));
            }
        }

        // check diagonal for white
        int[] white_dx = {1,1};
        int[] white_dy = {1,-1};

        for (int i = 0; i < 2; i++){

            int newx = position.getRow() + white_dx[i];
            int newy = position.getColumn() + white_dy[i];

            if (isinbounds(newx,newy) && is_opponent(newx,newy)){
                ChessPosition new_place = new ChessPosition(newx, newy);
                viable_moves.add(new ChessMove(position, new_place, null)); // loop through possible promotion
            }
        }

        // do the same for black pieces
        // change the method for

        return viable_moves;
    }

}
