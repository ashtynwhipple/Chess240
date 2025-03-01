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

    public void addPromotion(ChessPosition position, ChessPosition newPlace, List<ChessMove> viableMoves){
        for (ChessPiece.PieceType prom: ChessPiece.PieceType.values()){
            if (prom != ChessPiece.PieceType.KING && prom != ChessPiece.PieceType.PAWN){
                viableMoves.add(new ChessMove(position, newPlace, prom));
            }
        }
    }

    public void addToList(int newX, int newY, List<ChessMove> viableMoves){
        if (validMove(newX, newY)) {
            ChessPosition newPlace = new ChessPosition(newX, newY);
            viableMoves.add(new ChessMove(position, newPlace, null));
        }
    }

    public void directionsLoop(int[][] directions, List<ChessMove> viableMoves){
        for (int[] direction : directions) {

            int col = position.getColumn() + direction[0];
            int row = position.getRow() + direction[1];

            if (isInBounds(row, col) && isOpponent(row, col)) {
                ChessPosition newPlace = new ChessPosition(row, col);
                viableMoves.add(new ChessMove(position, newPlace, null));
            }

        }
    }

    public Collection<ChessMove> getViableMoves(){
        List<ChessMove> viableMoves = new ArrayList<>();

        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        int newX;
        int newY = position.getColumn();

        if (color == ChessGame.TeamColor.WHITE){ //don't add this space if there is something blocking it
            newX = position.getRow() + 1;
            if (validMove(newX,newY) && !isOpponent(newX,newY)){
                addToList(newX,newY, viableMoves);
                if (position.getRow() == 2){
                    newX = position.getRow() + 2;
                    if (validMove(newX,newY) && !isOpponent(newX,newY)){
                        addToList(newX,newY, viableMoves);
                    }
            }
                }
        } else{
            newX = position.getRow() -1;
            if (validMove(newX,newY) && !isOpponent(newX,newY)){
                addToList(newX,newY, viableMoves);
                if (position.getRow() == 7 ){
                    newX = position.getRow() -2;
                    if (validMove(newX,newY) && !isOpponent(newX,newY)){
                        addToList(newX,newY, viableMoves);
                    }
                }
            }
        }

        //directions
        int[][] wDirections = {
                {1, 1},
                {-1,1,},
        };
        int[][] bDirections = {
                {1, -1},
                {-1,-1,},
        };

        if(color == ChessGame.TeamColor.WHITE) {
            directionsLoop(wDirections,viableMoves);
        }else if(color == ChessGame.TeamColor.BLACK) {
            directionsLoop(bDirections,viableMoves);
        }

        List<ChessMove> promotionMoves = new ArrayList<>();
        for (ChessMove move: viableMoves){
            if (move.getEndPosition().getRow() == 1 || move.getEndPosition().getRow() == 8) {
                addPromotion(move.getStartPosition(), move.getEndPosition(), promotionMoves);
            }
        }
        viableMoves.addAll(promotionMoves);

        viableMoves.removeIf(move -> move.getEndPosition().getRow() == 1 && move.getPromotionPiece() == null ||
                move.getEndPosition().getRow() == 8 && move.getPromotionPiece() == null);

        return viableMoves;
    }

}
