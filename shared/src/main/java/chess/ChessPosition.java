package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int column;
    private int row;

    @Override
    public String toString() {
        return "(" + column + "," + row + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return column == that.column && row == that.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }

    public ChessPosition(int row, int col) {
        this.column = col;
        this.row = row;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {

        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {

        return this.column;

    }
}
