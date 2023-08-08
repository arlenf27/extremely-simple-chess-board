package escb1;

/**
 * Represents the position of a piece.
 *
 * @author Arlen Feng
 *
 */
public class Position {

    private int x;
    private int y;

    public Position(int y, int x) {
        this.y = y;
        this.x = x;
    }

    /**
     * Gets the row of the piece.
     *
     * @return the row of the piece
     */
    public int getRow() {
        return this.y;
    }

    /**
     * Gets the column of the piece.
     *
     * @return the column of the piece
     */
    public int getColumn() {
        return this.x;
    }

    /**
     * Sets the row of the piece.
     *
     * @param x
     *            the row of the piece
     */
    public void setRow(int y) {
        this.y = y;
    }

    /**
     * Sets the column of the piece.
     *
     * @param x
     *            the column of the piece
     */
    public void setColumn(int x) {
        this.x = x;
    }

    /**
     * Returns the row and then the column of the position.
     */
    @Override
    public String toString() {
        return "(" + this.y + ", " + this.x + ")";
    }
}
