package chess.movecalculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator extends PieceMovesCalculator{

    public PawnMovesCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public boolean isInBounds(int x, int y){
        return ( x >= 1 && x < 9 && y >= 1 && y < 9);
    }

    public void addPromotion(ChessPosition position, ChessPosition new_place, List<ChessMove> viable_moves){
        for (ChessPiece.PieceType prom: ChessPiece.PieceType.values()){
            if (prom != ChessPiece.PieceType.KING && prom != ChessPiece.PieceType.PAWN){
                viable_moves.add(new ChessMove(position, new_place, prom));
            }
        }
    }

    public void addToList(int new_x, int new_y, List<ChessMove> viable_moves){
        if (validMove(new_x, new_y)) {
            ChessPosition new_place = new ChessPosition(new_x, new_y);
            viable_moves.add(new ChessMove(position, new_place, null));
        }
    }

    public void directionsLoop(int[][] directions, List<ChessMove> viable_moves){
        for (int[] direction : directions) {

            int col = position.getColumn() + direction[0];
            int row = position.getRow() + direction[1];

            if (isInBounds(row, col) && isOpponent(row, col)) {
                ChessPosition new_place = new ChessPosition(row, col);
                viable_moves.add(new ChessMove(position, new_place, null));
            }

        }
    }

    public Collection<ChessMove> getViableMoves(){
        List<ChessMove> viable_moves = new ArrayList<>();

        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        int new_x;
        int new_y = position.getColumn();

        if (color == ChessGame.TeamColor.WHITE){ //don't add this space if there is something blocking it
            new_x = position.getRow() + 1;
            if (validMove(new_x,new_y) && !isOpponent(new_x,new_y)){
                addToList(new_x,new_y, viable_moves);
                if (position.getRow() == 2){
                    new_x = position.getRow() + 2;
                    if (validMove(new_x,new_y) && !isOpponent(new_x,new_y)){
                        addToList(new_x,new_y, viable_moves);
                    }
            }
                }
        } else{
            new_x = position.getRow() -1;
            if (validMove(new_x,new_y) && !isOpponent(new_x,new_y)){
                addToList(new_x,new_y, viable_moves);
                if (position.getRow() == 7 ){
                    new_x = position.getRow() -2;
                    if (validMove(new_x,new_y) && !isOpponent(new_x,new_y)){
                        addToList(new_x,new_y, viable_moves);
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

        if(color == ChessGame.TeamColor.WHITE) {
            directionsLoop(w_directions,viable_moves);
        }else if(color == ChessGame.TeamColor.BLACK) {
            directionsLoop(b_directions,viable_moves);
        }

        List<ChessMove> promotionMoves = new ArrayList<>();
        for (ChessMove move: viable_moves){
            if (move.getEndPosition().getRow() == 1 || move.getEndPosition().getRow() == 8) {
                addPromotion(move.getStartPosition(), move.getEndPosition(), promotionMoves);
            }
        }
        viable_moves.addAll(promotionMoves);

        viable_moves.removeIf(move -> move.getEndPosition().getRow() == 1 && move.getPromotionPiece() == null ||
                move.getEndPosition().getRow() == 8 && move.getPromotionPiece() == null);

        return viable_moves;
    }

}
