package escb1;

import java.util.Collection;

/**
 * Represents a chess board.
 *
 * @author Arlen Feng
 *
 */
public interface Board {
    /**
     * Ensures that the board is updated. The piece at pos1 is moved to pos2.
     *
     * @requires Board at pos1 contains a piece
     *
     * @param pos1
     *            initial location of piece
     * @param pos2
     *            final location of piece
     * @param color
     *            the color of the piece
     * @param ignoreKingInCheck
     *            an option to move a piece regardless of whether the king is in
     *            check or not
     * @throws KingInCheckException
     *             if the king is in check after the move
     * @return the piece that is being replaced (if something is taken) or null
     *         (if nothing is taken)
     */
    Piece updateState(Position pos1, Position pos2, Piece.Color color,
            boolean ignoreKingInCheck) throws KingInCheckException;

    /**
     * Promotes a pawn to another piece.
     *
     * @param pos1
     *            initial location of piece
     * @param pos2
     *            final location of piece
     * @param type
     *            the type to promote to
     * @param color
     *            the color of the piece
     * @throws KingInCheckException
     *             if the king is in check after the move
     * @return the piece that is being replaced (if something is taken) or null
     *         (if nothing is taken)
     */
    Piece promote(Position pos1, Position pos2, Piece.Type type,
            Piece.Color color) throws KingInCheckException;

    /**
     * Short Castle.
     *
     * @param color
     *            the color of the pieces
     * @throws KingInCheckException
     *             if the king is in check after the move
     */
    void shortCastle(Piece.Color color) throws KingInCheckException;

    /**
     * Long Castle.
     *
     * @param color
     *            the color of the pieces
     * @throws KingInCheckException
     *             if the king is in check after the move
     */
    void longCastle(Piece.Color color) throws KingInCheckException;

    /**
     *
     * @param pos1
     *            initial location of piece
     * @param pos2
     *            final locatin of piece
     * @param color
     *            the color of the piece
     * @param ignoreKingInCheck
     *            an option to move a piece regardless of whether the king is in
     *            check or not
     * @throws KingInCheckException
     *             if the king is in check after the move
     * @return the piece that is taken
     */
    Piece enpassant(Position pos1, Position pos2, Piece.Color color,
            boolean ignoreKingInCheck) throws KingInCheckException;

    /**
     * Gets the board as a 2-D array.
     *
     * @return the current state of the board
     */
    Piece[][] getBoardAsArray();

    /**
     * Gets the current move;
     *
     * @return the current move
     */
    int getMove();

    /**
     * Gets all positions of a type of piece.
     *
     * @param type
     *            the type of the piece
     * @param color
     *            the color of the piece
     * @return a collection of possible positions
     */
    Collection<Position> getPieces(Piece.Color color, Piece.Type type);

    /**
     * Prints the board in slightly different "table-like" format.
     *
     * @return a representation of the board in a slightly different format
     */
    String toStringTable();

    /**
     * Updates the line of sights for all pieces on the board based on the
     * current position.
     */
    void updateLineOfSights();

    /**
     * Checks whether the king is in check or not.
     *
     * @param color
     *            the color of the king
     * @return true if the king is in check
     */
    boolean kingInCheck(Piece.Color color);

    /**
     * Checks whether it is checkmate or stalemate.
     *
     * @param color
     *            the color of the side
     * @param checkmate
     *            if true, then checks for checkmate, otherwise check for
     *            stalemate
     * @param lastMove
     *            the last move (used for en passant checks)
     * @return true if the king is checkmated or stalemated
     */
    boolean checkmateOrStalemate(Piece.Color color, boolean checkmate,
            String lastMove);

    /**
     * Returns the pieces moved.
     *
     * @return the pieces moved
     */
    int getPiecesMoved();

    /**
     * Sets the pieces moved.
     *
     * @param pm
     *            the new pieces moved.
     */
    void setPiecesMoved(int pm);

    /**
     * True if there is insufficient material.
     *
     * @return true if there is insufficient material
     */
    boolean insufficientMaterialDraw();
}
