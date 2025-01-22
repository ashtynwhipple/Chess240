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

    public boolean is_in_bounds(int x, int y){
        return ( x >= 1 && x < 9 && y >= 1 && y < 9);
    }

    public boolean valid_move(int x, int y){
        if (is_in_bounds(x,y)){ //if in bounds
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
        return piece != null && !piece.getTeamColor().equals(board.getPiece(position).getTeamColor()); //take into account if it is null
    }

    public void add_promotion(ChessPosition position, ChessPosition new_place, List<ChessMove> viable_moves){
        for (ChessPiece.PieceType prom: ChessPiece.PieceType.values()){
            if (prom != ChessPiece.PieceType.KING && prom != ChessPiece.PieceType.PAWN){
                viable_moves.add(new ChessMove(position, new_place, prom));
            }
        }
    }

    public void add_to_list(int newx, int newy, List<ChessMove> viable_moves){
        if (valid_move(newx, newy)) {
            ChessPosition new_place = new ChessPosition(newx, newy);
            viable_moves.add(new ChessMove(position, new_place, null));
        }
    }

    public void directions_loop(int[][] directions, List<ChessMove> viable_moves){
        for (int[] direction : directions) {

            int col = position.getColumn() + direction[0];
            int row = position.getRow() + direction[1];

            if (is_in_bounds(row, col) && is_opponent(row, col)) {
                ChessPosition new_place = new ChessPosition(row, col);
                viable_moves.add(new ChessMove(position, new_place, null));
            }

        }
    }

    public Collection<ChessMove> get_viable_moves(){
        List<ChessMove> viable_moves = new ArrayList<>();

        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        int newx;
        int newy = position.getColumn();

        //logic to go one space
        if (color == ChessGame.TeamColor.WHITE){ //don't add this space if there is something blocking it
            newx = position.getRow() + 1;
            if (valid_move(newx,newy)){
                add_to_list(newx,newy, viable_moves);
                if (position.getRow() == 2){
                    newx = position.getRow() + 2;
                    if (valid_move(newx,newy)){
                        add_to_list(newx,newy, viable_moves);
                    }
            }
                }
        } else{
            newx = position.getRow() -1;
            if (valid_move(newx,newy)){
                add_to_list(newx,newy, viable_moves);
                if (position.getRow() == 7 ){
                    newx = position.getRow() -2;
                    if (valid_move(newx,newy)){
                        add_to_list(newx,newy, viable_moves);
                    }
                }
            }
        }

        //directions
        int[][] w_directions = {
                {1, 1},
                {-1,1,},
        };
        int[][] b_directions = {
                {1, -1},
                {-1,-1,},
        };

        //add places if they want to kill and go diagonal
        if(color == ChessGame.TeamColor.WHITE) {
            directions_loop(w_directions,viable_moves);
        }else if(color == ChessGame.TeamColor.BLACK) {
            directions_loop(b_directions,viable_moves);
        }

        //add promotion pieces
        List<ChessMove> promotionMoves = new ArrayList<>();
        for (ChessMove move: viable_moves){
            if (move.getEndPosition().getRow() == 1 || move.getEndPosition().getRow() == 8) {
                add_promotion(move.getStartPosition(), move.getEndPosition(), promotionMoves);
            }
        }
        viable_moves.addAll(promotionMoves);

        return viable_moves;
    }

}
