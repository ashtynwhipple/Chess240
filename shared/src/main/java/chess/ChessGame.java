package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ChessPiece piece = board.getPiece(startPosition);

        if (piece == null) {
            return null;
        }

        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();

        for (ChessMove move : possibleMoves) {
            ChessPiece replaced = board.getPiece(move.getEndPosition());
            ChessPiece original = board.getPiece(move.getStartPosition());
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
            board.addPiece(move.getStartPosition(), null);
            if (!isSquareAttacked(board.findKing(piece.getTeamColor()), piece.getTeamColor())) {
                validMoves.add(move); // Only keep moves that don't leave the king in check
            }
            board.addPiece(move.getEndPosition(), replaced);
            board.addPiece(move.getStartPosition(), original);
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPiece piece = board.getPiece(move.getStartPosition());

        if (piece != null && piece.getTeamColor() != teamTurn){
            throw new InvalidMoveException("wrong team color");
        }

        Collection<ChessMove> valid_moves = validMoves(move.getStartPosition());

        if (valid_moves == null || !valid_moves.contains(move)){
            throw new InvalidMoveException("Move is not Valid");
        }

        board.addPiece(move.getEndPosition(), piece);
        board.addPiece(move.getStartPosition(), null);

        if (move.getPromotionPiece() != null) {
            board.addPiece(move.getEndPosition(), new ChessPiece(teamTurn, move.getPromotionPiece()));
        }

        // change team color
        if (teamTurn == TeamColor.WHITE){
            teamTurn = TeamColor.BLACK;
        } else{
            teamTurn = TeamColor.WHITE;
        }
    }


    public boolean isSquareAttacked(ChessPosition position, ChessGame.TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition oppPosition = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(oppPosition);
                if (opponentCanAttackSpot(position, teamColor, piece, oppPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean opponentCanAttackSpot(ChessPosition position, TeamColor teamColor, ChessPiece piece, ChessPosition oppPosition) {
        if (piece != null && piece.getTeamColor() != teamColor) {
            Collection<ChessMove> moves = piece.pieceMoves(board, oppPosition);
            for (ChessMove move : moves) {
                if (move.getEndPosition().equals(position)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = board.findKing(teamColor);
        return isSquareAttacked(kingPosition, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;

        }
        for (ChessPosition position: board.getAllPositions(teamColor)){
            Collection<ChessMove> validMoves = validMoves(position);
            if (!validMoves.isEmpty()){
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // check for all positions and pieces possible if the valid moves is empty. if so, return true
        if (isInCheck(teamColor)){
            return false;
        }

        for (ChessPosition position: board.getAllPositions(teamColor)){
            Collection<ChessMove> validMoves = validMoves(position);
            if (!(validMoves.isEmpty())){
                return false;
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
