package escb1;

import java.util.Set;

/**
 * Represents a single chess piece.
 *
 * @author Arlen Feng
 *
 */
public interface Piece {

    /**
     * The type of the piece.
     */
    enum Type {
        PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING
    }

    /**
     * The color of the piece.
     */
    enum Color {
        WHITE, BLACK
    }

    /**
     * Returns the location of the piece.
     *
     * @return the location of the piece
     */
    Position getPosition();

    /**
     * SHOULD NEVER BE USED IN ANY CIRCUMSTANCE! THIS METHOD DOES NOT UPDATE A
     * PIECE'S LINE OF SIGHT SINCE IT IS IMPOSSIBLE TO KNOW THE LINE OF SIGHT
     * WITHOUT ALSO KNOWING THE REST OF THE BOARD. THIS METHOD ALSO DOES NOT
     * CHANGE THE REPRESENTATION OF THE BOARD, SO THE DATA STORED IN THIS PIECE
     * AND THE ACUAL BOARD WOULD CONFLICT WITH EACH OTHER AFTER THIS METHOD
     * CALL.
     *
     * UPDATES THE DATA STORED INSIDE THE POSITION OF THE PIECE BUT DOES NOT
     * UPDATE THE LINE OF SIGHT OR CHANGE THE DATA OF THE REPRESENTATION OF THE
     * BOARD.
     *
     * BASICALLY, DO NOT EVER USE THIS METHOD!
     *
     * THE RECOMMENDED SOLUTION IS TO CALL BOARD'S UPDATESTATE() FOR ACCURATE
     * RESULTS!!!
     *
     * @param pos2
     *            final location of piece
     */
    void updatePosition(Position pos2);

    /**
     * Gets the type of the piece.
     *
     * @return the type of the piece
     */
    Type getType();

    /**
     * Gets the color of the piece.
     *
     * @return the color of the piece
     */
    Color getColor();

    /**
     * Gets the times moved.
     *
     * @return the times moved
     */
    int getTimesMoved();

    /**
     * Sets the times moved.
     *
     * @param timesMoved
     *            the new times moved
     */
    void setTimesMoved(int timesMoved);

    /**
     * Gets the line of sight. The initial line of sight for any piece is
     * represented by an empty set.
     *
     * @return a set of positions within the line of sight
     */
    Set<Position> lineOfSight();

    /**
     * Adds a position to the line of sight. Line of sight needs to be changed
     * by the client (Board) since it is not easy to conveniently know a piece's
     * line of sight without knowing the rest of the board. In the case of
     * pawns, the line of sight represents the squares which the pawns can
     * capture, not squares in which they can move normally. The initial line of
     * sight for any piece is represented by an empty set.
     *
     * @param pos
     *            the position to be added to the line of sight
     */
    void addToLineOfSight(Position pos);

    /**
     * Clears all positions from the line of sight. Line of sight needs to be
     * changed by the client (Board) since it is not easy to conveniently know a
     * piece's line of sight without knowing the rest of the board. In the case
     * of pawns, the line of sight represents the squares which the pawns can
     * capture, not squares in which they can move normally. The initial line of
     * sight for any piece is represented by an empty set.
     */
    void clearLineOfSight();
}
