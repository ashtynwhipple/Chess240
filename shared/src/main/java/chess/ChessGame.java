package chess;

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

        // there are so many more things I need to impelement here for this method but for now this will work

        // throw invalidmoveexception if isincheck
        if (isInCheck(teamTurn)){
            return null;
        }

        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        return piece.pieceMoves(board, startPosition);
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

        board.addPiece(move.getEndPosition(), piece); //add piece
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

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king_position = board.find_king(teamColor);
        return board.is_square_attacked(king_position, teamColor);
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

        // this feels suuuuper inefficient, I am going to try and think of a different way to do this

        // write method to check if for all positions and possible moves by player, the king is still in position to be killed
        ChessPosition king_position = board.find_king(teamColor);

        for (ChessPosition position: board.get_all_positions(teamColor)){ // I think I need to for loops here, one to go through the possible positions and another one to go move through the moves and see if king is still in danger.
            Collection<ChessMove> valid_moves = validMoves(position);
            if (!(valid_moves == null)){
                for (ChessMove move: valid_moves){
                    ChessBoard copy = new ChessBoard();
                    for (int row = 1; row <= 8; row++) {
                        for (int col = 1; col <= 8; col++) {
                            ChessPosition new_pos = new ChessPosition(row, col);
                            ChessPiece piece = board.getPiece(new_pos);
                            if (piece != null) {
                                copy.addPiece(new_pos, new ChessPiece(piece.getTeamColor(), piece.getPieceType()));
                            }
                        }
                    }
                    //somehow make the moves without actually making them or at least go back right away

                    // I think I can also do a way where I actually make the move, see what happens, then put it back... might be more efficient

                    if (!copy.is_square_attacked(king_position, teamColor)) { //check if king is still in danger
                        return false;
                    }
                }
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
        for (ChessPosition position: board.get_all_positions(teamColor)){
            Collection<ChessMove> valid_moves = validMoves(position);
            if (!valid_moves.isEmpty()){
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
