package escb1;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import escb1.Piece.Color;
import escb1.Piece.Type;

/**
 *
 * @author Arlen Feng
 *
 */
public class Board1 implements Board {

    private Piece[][] board;
    private int piecesMoved;

    /**
     * The initial representation of the board sets up all pieces corresponding
     * to the start of a normal chess game.
     */
    public Board1() {
        this.board = new Piece[8][8];
        this.board[7][0] = new Piece1(Piece.Color.WHITE, Piece.Type.ROOK,
                new Position(7, 0));
        this.board[7][1] = new Piece1(Piece.Color.WHITE, Piece.Type.KNIGHT,
                new Position(7, 1));
        this.board[7][2] = new Piece1(Piece.Color.WHITE, Piece.Type.BISHOP,
                new Position(7, 2));
        this.board[7][3] = new Piece1(Piece.Color.WHITE, Piece.Type.QUEEN,
                new Position(7, 3));
        this.board[7][4] = new Piece1(Piece.Color.WHITE, Piece.Type.KING,
                new Position(7, 4));
        this.board[7][5] = new Piece1(Piece.Color.WHITE, Piece.Type.BISHOP,
                new Position(7, 5));
        this.board[7][6] = new Piece1(Piece.Color.WHITE, Piece.Type.KNIGHT,
                new Position(7, 6));
        this.board[7][7] = new Piece1(Piece.Color.WHITE, Piece.Type.ROOK,
                new Position(7, 7));
        this.board[6][0] = new Piece1(Piece.Color.WHITE, Piece.Type.PAWN,
                new Position(6, 0));
        this.board[6][1] = new Piece1(Piece.Color.WHITE, Piece.Type.PAWN,
                new Position(6, 1));
        this.board[6][2] = new Piece1(Piece.Color.WHITE, Piece.Type.PAWN,
                new Position(6, 2));
        this.board[6][3] = new Piece1(Piece.Color.WHITE, Piece.Type.PAWN,
                new Position(6, 3));
        this.board[6][4] = new Piece1(Piece.Color.WHITE, Piece.Type.PAWN,
                new Position(6, 4));
        this.board[6][5] = new Piece1(Piece.Color.WHITE, Piece.Type.PAWN,
                new Position(6, 5));
        this.board[6][6] = new Piece1(Piece.Color.WHITE, Piece.Type.PAWN,
                new Position(6, 6));
        this.board[6][7] = new Piece1(Piece.Color.WHITE, Piece.Type.PAWN,
                new Position(6, 7));

        this.board[0][0] = new Piece1(Piece.Color.BLACK, Piece.Type.ROOK,
                new Position(0, 0));
        this.board[0][1] = new Piece1(Piece.Color.BLACK, Piece.Type.KNIGHT,
                new Position(0, 1));
        this.board[0][2] = new Piece1(Piece.Color.BLACK, Piece.Type.BISHOP,
                new Position(0, 2));
        this.board[0][3] = new Piece1(Piece.Color.BLACK, Piece.Type.QUEEN,
                new Position(0, 3));
        this.board[0][4] = new Piece1(Piece.Color.BLACK, Piece.Type.KING,
                new Position(0, 4));
        this.board[0][5] = new Piece1(Piece.Color.BLACK, Piece.Type.BISHOP,
                new Position(0, 5));
        this.board[0][6] = new Piece1(Piece.Color.BLACK, Piece.Type.KNIGHT,
                new Position(0, 6));
        this.board[0][7] = new Piece1(Piece.Color.BLACK, Piece.Type.ROOK,
                new Position(0, 7));
        this.board[1][0] = new Piece1(Piece.Color.BLACK, Piece.Type.PAWN,
                new Position(1, 0));
        this.board[1][1] = new Piece1(Piece.Color.BLACK, Piece.Type.PAWN,
                new Position(1, 1));
        this.board[1][2] = new Piece1(Piece.Color.BLACK, Piece.Type.PAWN,
                new Position(1, 2));
        this.board[1][3] = new Piece1(Piece.Color.BLACK, Piece.Type.PAWN,
                new Position(1, 3));
        this.board[1][4] = new Piece1(Piece.Color.BLACK, Piece.Type.PAWN,
                new Position(1, 4));
        this.board[1][5] = new Piece1(Piece.Color.BLACK, Piece.Type.PAWN,
                new Position(1, 5));
        this.board[1][6] = new Piece1(Piece.Color.BLACK, Piece.Type.PAWN,
                new Position(1, 6));
        this.board[1][7] = new Piece1(Piece.Color.BLACK, Piece.Type.PAWN,
                new Position(1, 7));
        this.piecesMoved = 0;
        this.updateLineOfSights();
    }

    @Override
    public Piece updateState(Position pos1, Position pos2, Piece.Color color,
            boolean ignoreKingInCheck) throws KingInCheckException {
        Piece newPiece = new Piece1(
                this.board[pos1.getRow()][pos1.getColumn()].getColor(),
                this.board[pos1.getRow()][pos1.getColumn()].getType(), pos2);
        newPiece.setTimesMoved(
                this.board[pos1.getRow()][pos1.getColumn()].getTimesMoved()
                        + 1);
        Piece pieceReplaced = null;
        if (this.board[pos2.getRow()][pos2.getColumn()] != null) {
            pieceReplaced = new Piece1(
                    this.board[pos2.getRow()][pos2.getColumn()].getColor(),
                    this.board[pos2.getRow()][pos2.getColumn()].getType(),
                    pos2);
            pieceReplaced
                    .setTimesMoved(this.board[pos2.getRow()][pos2.getColumn()]
                            .getTimesMoved());
        }
        this.board[pos1.getRow()][pos1.getColumn()] = null;
        this.board[pos2.getRow()][pos2.getColumn()] = newPiece;
        this.piecesMoved++;
        this.updateLineOfSights();
        if (this.kingInCheck(color) && !ignoreKingInCheck) {
            /*
             * Reversing the Process
             */
            Piece formerPiece = new Piece1(newPiece.getColor(),
                    newPiece.getType(), pos1);
            this.board[pos1.getRow()][pos1.getColumn()] = formerPiece;
            this.board[pos2.getRow()][pos2.getColumn()] = pieceReplaced;
            this.piecesMoved--;
            this.updateLineOfSights();
            throw new KingInCheckException(
                    "The Input is Invalid Since the King is In Check After Move - Please try again...");
        }
        return pieceReplaced;
    }

    @Override
    public Piece[][] getBoardAsArray() {
        return this.board;
    }

    @Override
    public int getMove() {
        return this.piecesMoved / 2 + 1;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(this.board);
    }

    @Override
    public Collection<Position> getPieces(Color color, Type type) {
        HashSet<Position> result = new HashSet<Position>();
        for (Piece[] currentRow : this.board) {
            for (Piece current : currentRow) {
                if (current != null && current.getColor() == color
                        && current.getType() == type) {
                    result.add(current.getPosition());
                }
            }
        }
        return result;
    }

    @Override
    public String toStringTable() {
        String result = "\t";
        for (Piece[] currentRow : this.board) {
            for (Piece current : currentRow) {
                if (current != null) {
                    result = result.concat(current + "\t");
                } else {
                    result = result.concat("<EMPTY>" + "\t\t");
                }
            }
            result = result.concat("\n\n\t");
        }
        return result;
    }

    @Override
    public void updateLineOfSights() {
        for (Piece[] currentRow : this.board) {
            for (Piece current : currentRow) {
                if (current != null) {
                    current.clearLineOfSight();
                    int row = current.getPosition().getRow();
                    int col = current.getPosition().getColumn();
                    if (current.getType() == Piece.Type.PAWN) {
                        if (!(current.getColor() == Piece.Color.WHITE
                                && row == 0)
                                && !(current.getColor() == Piece.Color.BLACK
                                        && row == 7)) {
                            int forward = -1;
                            if (current.getColor() == Piece.Color.BLACK) {
                                forward = 1;
                            }
                            if (col == 0) {
                                current.addToLineOfSight(
                                        new Position(row + forward, col + 1));
                            } else if (col == 7) {
                                current.addToLineOfSight(
                                        new Position(row + forward, col - 1));
                            } else {
                                current.addToLineOfSight(
                                        new Position(row + forward, col + 1));
                                current.addToLineOfSight(
                                        new Position(row + forward, col - 1));
                            }
                        }
                    } else if (current.getType() == Piece.Type.QUEEN) {
                        this.addAPathToLineOfSight(current, row, col, 1, 1);
                        this.addAPathToLineOfSight(current, row, col, 1, 0);
                        this.addAPathToLineOfSight(current, row, col, 1, -1);
                        this.addAPathToLineOfSight(current, row, col, -1, 1);
                        this.addAPathToLineOfSight(current, row, col, -1, 0);
                        this.addAPathToLineOfSight(current, row, col, -1, -1);
                        this.addAPathToLineOfSight(current, row, col, 0, 1);
                        this.addAPathToLineOfSight(current, row, col, 0, -1);
                    } else if (current.getType() == Piece.Type.KING) {
                        if (row + 1 <= 7) {
                            current.addToLineOfSight(
                                    new Position(row + 1, col));
                        }
                        if (row + 1 <= 7 && col + 1 <= 7) {
                            current.addToLineOfSight(
                                    new Position(row + 1, col + 1));
                        }
                        if (row + 1 <= 7 && col - 1 >= 0) {
                            current.addToLineOfSight(
                                    new Position(row + 1, col - 1));
                        }
                        if (row - 1 >= 0) {
                            current.addToLineOfSight(
                                    new Position(row - 1, col));
                        }
                        if (row - 1 >= 0 && col + 1 <= 7) {
                            current.addToLineOfSight(
                                    new Position(row - 1, col + 1));
                        }
                        if (row - 1 >= 0 && col - 1 >= 0) {
                            current.addToLineOfSight(
                                    new Position(row - 1, col - 1));
                        }
                        if (col + 1 <= 7) {
                            current.addToLineOfSight(
                                    new Position(row, col + 1));
                        }
                        if (col - 1 >= 0) {
                            current.addToLineOfSight(
                                    new Position(row, col - 1));
                        }
                    } else if (current.getType() == Piece.Type.KNIGHT) {
                        if (row + 2 <= 7 && col + 1 <= 7) {
                            current.addToLineOfSight(
                                    new Position(row + 2, col + 1));
                        }
                        if (row + 2 <= 7 && col - 1 >= 0) {
                            current.addToLineOfSight(
                                    new Position(row + 2, col - 1));
                        }
                        if (row + 1 <= 7 && col + 2 <= 7) {
                            current.addToLineOfSight(
                                    new Position(row + 1, col + 2));
                        }
                        if (row + 1 <= 7 && col - 2 >= 0) {
                            current.addToLineOfSight(
                                    new Position(row + 1, col - 2));
                        }
                        if (row - 1 >= 0 && col + 2 <= 7) {
                            current.addToLineOfSight(
                                    new Position(row - 1, col + 2));
                        }
                        if (row - 1 >= 0 && col - 2 >= 0) {
                            current.addToLineOfSight(
                                    new Position(row - 1, col - 2));
                        }
                        if (row - 2 >= 0 && col + 1 <= 7) {
                            current.addToLineOfSight(
                                    new Position(row - 2, col + 1));
                        }
                        if (row - 2 >= 0 && col - 1 >= 0) {
                            current.addToLineOfSight(
                                    new Position(row - 2, col - 1));
                        }
                    } else if (current.getType() == Piece.Type.BISHOP) {
                        this.addAPathToLineOfSight(current, row, col, 1, 1);
                        this.addAPathToLineOfSight(current, row, col, 1, -1);
                        this.addAPathToLineOfSight(current, row, col, -1, 1);
                        this.addAPathToLineOfSight(current, row, col, -1, -1);
                    } else if (current.getType() == Piece.Type.ROOK) {
                        this.addAPathToLineOfSight(current, row, col, 1, 0);
                        this.addAPathToLineOfSight(current, row, col, -1, 0);
                        this.addAPathToLineOfSight(current, row, col, 0, 1);
                        this.addAPathToLineOfSight(current, row, col, 0, -1);
                    }
                }
            }
        }
    }

    /**
     * Adds all line of sights on a path in ONLY ONE DIRECTION. Note: verDir and
     * horDir can only be either 1, -1, or 0.
     *
     * @param current
     *            the current piece
     * @param row
     *            the row of the current piece
     * @param col
     *            the column of the current piece
     * @param verDir
     *            the vertical direction (-1 for negative/down the board, 1 for
     *            positive/up the board, 0 for no change)
     * @param horDir
     *            the horizontal direction (-1 for negative/to the left, 1 for
     *            positive/to the right, 0 for no change)
     */
    private void addAPathToLineOfSight(Piece current, int row, int col,
            int verDir, int horDir) {
        int squaresMovedVertically = -verDir;
        int squaresMovedHorizontally = horDir;
        boolean continueDownPath = true;
        while ((row + squaresMovedVertically >= 0
                && row + squaresMovedVertically <= 7)
                && (col + squaresMovedHorizontally >= 0
                        && col + squaresMovedHorizontally <= 7)
                && continueDownPath) {
            current.addToLineOfSight(new Position(row + squaresMovedVertically,
                    col + squaresMovedHorizontally));
            if (this.board[row + squaresMovedVertically][col
                    + squaresMovedHorizontally] != null) {
                continueDownPath = false;
            }
            squaresMovedVertically -= verDir;
            squaresMovedHorizontally += horDir;
        }
    }

    @Override
    public boolean kingInCheck(Color color) {
        Position positionOfKing = (Position) ((this
                .getPieces(color, Piece.Type.KING).toArray())[0]);
        for (Piece[] currentRow : this.board) {
            for (Piece current : currentRow) {
                if (current != null && current.getColor() != color) {
                    Iterator<Position> i = current.lineOfSight().iterator();
                    while (i.hasNext()) {
                        Position next = i.next();
                        if (next.getRow() == positionOfKing.getRow() && next
                                .getColumn() == positionOfKing.getColumn()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean checkmateOrStalemate(Color color, boolean checkmate,
            String lastMove) {
        if ((checkmate && this.kingInCheck(color))
                || (!checkmate && !this.kingInCheck(color))) {
            int temp = this.piecesMoved;
            for (Piece[] currentRow : this.board) {
                for (Piece current : currentRow) {
                    if (current != null && current.getColor() == color) {
                        int row = current.getPosition().getRow();
                        int col = current.getPosition().getColumn();
                        if (current.getType() == Piece.Type.PAWN) {
                            if (!(current.getColor() == Piece.Color.WHITE
                                    && row == 0)
                                    && !(current.getColor() == Piece.Color.BLACK
                                            && row == 7)) {
                                int forward = -1;
                                if (current.getColor() == Piece.Color.BLACK) {
                                    forward = 1;
                                }
                                if (col != 0
                                        && this.board[row + forward][col
                                                - 1] != null
                                        && this.board[row + forward][col - 1]
                                                .getColor() != color) {
                                    if (!this
                                            .moveFails(new Position(row, col),
                                                    new Position(row + forward,
                                                            col - 1),
                                                    color, temp)) {
                                        return false;
                                    }
                                }
                                if (col != 7
                                        && this.board[row + forward][col
                                                + 1] != null
                                        && this.board[row + forward][col + 1]
                                                .getColor() != color) {
                                    if (!this
                                            .moveFails(new Position(row, col),
                                                    new Position(row + forward,
                                                            col + 1),
                                                    color, temp)) {
                                        return false;
                                    }
                                }
                                if (current.getTimesMoved() == 0
                                        && this.board[row
                                                + (forward * 2)][col] == null
                                        && this.board[row
                                                + forward][col] == null) {
                                    if (!this.moveFails(new Position(row, col),
                                            new Position(row + (forward * 2),
                                                    col),
                                            color, temp)) {
                                        return false;
                                    }
                                }
                                if (this.board[row + forward][col] == null) {
                                    if (!this.moveFails(new Position(row, col),
                                            new Position(row + forward, col),
                                            color, temp)) {
                                        return false;
                                    }
                                }
                                if (col != 0 && this.board[row][col - 1] != null
                                        && this.board[row][col - 1]
                                                .getColor() != color
                                        && lastMove.length() == 2
                                        && 8 - Character.getNumericValue(
                                                lastMove.charAt(1)) == row
                                        && lastMove.charAt(0) - 97 == col - 1
                                        && this.board[row][col - 1]
                                                .getTimesMoved() == 1) {
                                    if (!this
                                            .enpassantFails(
                                                    new Position(row, col),
                                                    new Position(row + forward,
                                                            col - 1),
                                                    color, temp)) {
                                        return false;
                                    }
                                }
                                if (col != 7 && this.board[row][col + 1] != null
                                        && this.board[row][col + 1]
                                                .getColor() != color
                                        && lastMove.length() == 2
                                        && 8 - Character.getNumericValue(
                                                lastMove.charAt(1)) == row
                                        && lastMove.charAt(0) - 97 == col + 1
                                        && this.board[row][col + 1]
                                                .getTimesMoved() == 1) {
                                    if (!this
                                            .enpassantFails(
                                                    new Position(row, col),
                                                    new Position(row + forward,
                                                            col + 1),
                                                    color, temp)) {
                                        return false;
                                    }
                                }
                            }
                        } else {
                            for (Position pos2 : current.lineOfSight()) {
                                int row2 = pos2.getRow();
                                int col2 = pos2.getColumn();
                                if (this.board[row2][col2] == null
                                        || (this.board[row2][col2] != null
                                                && this.board[row2][col2]
                                                        .getColor() != color)) {
                                    if (!this.moveFails(new Position(row, col),
                                            new Position(row2, col2), color,
                                            temp)) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.piecesMoved = temp - 1;
            return true;
        }
        return false;
    }

    /**
     * Checks if a pseudo-legal move fails as a result of a check.
     *
     * @param pos1
     *            initial location of piece
     * @param pos2
     *            final location to test
     * @param color
     *            the color of the piece
     * @param temp
     *            original count of pieces moved
     * @return true if the move fails and false otherwise
     */
    private boolean moveFails(Position pos1, Position pos2, Color color,
            int temp) {
        try {
            Piece pieceTaken = this.updateState(pos1, pos2, color, false);
            this.updateState(pos2, pos1, color, true);
            if (pieceTaken != null) {
                this.board[pos2.getRow()][pos2.getColumn()] = pieceTaken;
            }
            this.piecesMoved = temp;
            return false;
        } catch (KingInCheckException e) {
            return true;
        }
    }

    /**
     *
     * @param pos1
     *            initial location of piece
     * @param pos2
     *            final location to test
     * @param color
     *            the color of the piece
     * @param temp
     *            original count of pieces moved
     * @return true if en passant fails and false otherwise
     */
    private boolean enpassantFails(Position pos1, Position pos2, Color color,
            int temp) {
        try {
            Piece pieceTaken = this.enpassant(pos1, pos2, color, false);
            this.updateState(pos2, pos1, color, true);
            int backwards = 1;
            if (color == Piece.Color.BLACK) {
                backwards = -1;
            }
            if (pieceTaken != null) {
                this.board[pos2.getRow() + backwards][pos2
                        .getColumn()] = pieceTaken;
            }
            this.piecesMoved = temp;
            return false;
        } catch (KingInCheckException e) {
            return true;
        }
    }

    @Override
    public Piece promote(Position pos1, Position pos2, Type type, Color color)
            throws KingInCheckException {
        Piece newPiece = new Piece1(color, type, pos2);
        newPiece.setTimesMoved(
                this.board[pos1.getRow()][pos1.getColumn()].getTimesMoved()
                        + 1);
        Piece pieceReplaced = null;
        if (this.board[pos2.getRow()][pos2.getColumn()] != null) {
            pieceReplaced = new Piece1(
                    this.board[pos2.getRow()][pos2.getColumn()].getColor(),
                    this.board[pos2.getRow()][pos2.getColumn()].getType(),
                    pos2);
            pieceReplaced
                    .setTimesMoved(this.board[pos2.getRow()][pos2.getColumn()]
                            .getTimesMoved());
        }
        this.board[pos1.getRow()][pos1.getColumn()] = null;
        this.board[pos2.getRow()][pos2.getColumn()] = newPiece;
        this.piecesMoved++;
        this.updateLineOfSights();
        if (this.kingInCheck(color)) {
            Piece formerPiece = new Piece1(newPiece.getColor(), Piece.Type.PAWN,
                    pos1);
            this.board[pos1.getRow()][pos1.getColumn()] = formerPiece;
            this.board[pos2.getRow()][pos2.getColumn()] = pieceReplaced;
            this.piecesMoved--;
            this.updateLineOfSights();
            throw new KingInCheckException(
                    "The Input is Invalid Since the King is In Check After Move - Please try again...");
        }
        return pieceReplaced;
    }

    @Override
    public void shortCastle(Color color) throws KingInCheckException {
        Position kPos1 = new Position(7, 4);
        Position rPos1 = new Position(7, 7);
        Position kPos2 = new Position(7, 6);
        Position rPos2 = new Position(7, 5);
        if (color == Piece.Color.BLACK) {
            kPos1 = new Position(0, 4);
            rPos1 = new Position(0, 7);
            kPos2 = new Position(0, 6);
            rPos2 = new Position(0, 5);
        }
        Piece newKing = new Piece1(color, Piece.Type.KING, kPos2);
        newKing.setTimesMoved(
                this.board[kPos1.getRow()][kPos1.getColumn()].getTimesMoved()
                        + 1);
        this.board[kPos1.getRow()][kPos1.getColumn()] = null;
        this.board[kPos2.getRow()][kPos2.getColumn()] = newKing;
        Piece newRook = new Piece1(color, Piece.Type.ROOK, rPos2);
        newRook.setTimesMoved(
                this.board[rPos1.getRow()][rPos1.getColumn()].getTimesMoved()
                        + 1);
        this.board[rPos1.getRow()][rPos1.getColumn()] = null;
        this.board[rPos2.getRow()][rPos2.getColumn()] = newRook;
        this.piecesMoved++;
        this.updateLineOfSights();
        if (this.kingInCheck(color)) {
            Piece formerKing = new Piece1(color, Piece.Type.KING, kPos1);
            this.board[kPos1.getRow()][kPos1.getColumn()] = formerKing;
            this.board[kPos2.getRow()][kPos2.getColumn()] = null;
            Piece formerRook = new Piece1(color, Piece.Type.ROOK, rPos1);
            this.board[rPos1.getRow()][rPos1.getColumn()] = formerRook;
            this.board[rPos2.getRow()][rPos2.getColumn()] = null;
            this.piecesMoved--;
            this.updateLineOfSights();
            throw new KingInCheckException(
                    "The Input is Invalid Since the King is In Check After Move - Please try again...");
        }
    }

    @Override
    public void longCastle(Color color) throws KingInCheckException {
        Position kPos1 = new Position(7, 4);
        Position rPos1 = new Position(7, 0);
        Position kPos2 = new Position(7, 2);
        Position rPos2 = new Position(7, 3);
        if (color == Piece.Color.BLACK) {
            kPos1 = new Position(0, 4);
            rPos1 = new Position(0, 0);
            kPos2 = new Position(0, 2);
            rPos2 = new Position(0, 3);
        }
        Piece newKing = new Piece1(color, Piece.Type.KING, kPos2);
        newKing.setTimesMoved(
                this.board[kPos1.getRow()][kPos1.getColumn()].getTimesMoved()
                        + 1);
        this.board[kPos1.getRow()][kPos1.getColumn()] = null;
        this.board[kPos2.getRow()][kPos2.getColumn()] = newKing;
        Piece newRook = new Piece1(color, Piece.Type.ROOK, rPos2);
        newRook.setTimesMoved(
                this.board[rPos1.getRow()][rPos1.getColumn()].getTimesMoved()
                        + 1);
        this.board[rPos1.getRow()][rPos1.getColumn()] = null;
        this.board[rPos2.getRow()][rPos2.getColumn()] = newRook;
        this.piecesMoved++;
        this.updateLineOfSights();
        if (this.kingInCheck(color)) {
            Piece formerKing = new Piece1(color, Piece.Type.KING, kPos1);
            this.board[kPos1.getRow()][kPos1.getColumn()] = formerKing;
            this.board[kPos2.getRow()][kPos2.getColumn()] = null;
            Piece formerRook = new Piece1(color, Piece.Type.ROOK, rPos1);
            this.board[rPos1.getRow()][rPos1.getColumn()] = formerRook;
            this.board[rPos2.getRow()][rPos2.getColumn()] = null;
            this.piecesMoved--;
            this.updateLineOfSights();
            throw new KingInCheckException(
                    "The Input is Invalid Since the King is In Check After Move - Please try again...");
        }
    }

    @Override
    public Piece enpassant(Position pos1, Position pos2, Color color,
            boolean ignoreKingInCheck) throws KingInCheckException {
        Piece newPiece = new Piece1(
                this.board[pos1.getRow()][pos1.getColumn()].getColor(),
                Piece.Type.PAWN, pos2);
        newPiece.setTimesMoved(
                this.board[pos1.getRow()][pos1.getColumn()].getTimesMoved()
                        + 1);
        Piece pieceReplaced = null;
        int backwards = 1;
        if (color == Piece.Color.BLACK) {
            backwards = -1;
        }
        if (this.board[pos2.getRow() + backwards][pos2.getColumn()] != null) {
            pieceReplaced = new Piece1(
                    this.board[pos2.getRow() + backwards][pos2.getColumn()]
                            .getColor(),
                    this.board[pos2.getRow() + backwards][pos2.getColumn()]
                            .getType(),
                    new Position(pos2.getRow() + backwards, pos2.getColumn()));
            pieceReplaced.setTimesMoved(
                    this.board[pos2.getRow() + backwards][pos2.getColumn()]
                            .getTimesMoved());
        }
        this.board[pos1.getRow()][pos1.getColumn()] = null;
        this.board[pos2.getRow()][pos2.getColumn()] = newPiece;
        this.board[pos2.getRow() + backwards][pos2.getColumn()] = null;
        this.piecesMoved++;
        this.updateLineOfSights();
        if (this.kingInCheck(color) && !ignoreKingInCheck) {
            /*
             * Reversing the Process
             */
            Piece formerPiece = new Piece1(newPiece.getColor(),
                    newPiece.getType(), pos1);
            this.board[pos1.getRow()][pos1.getColumn()] = formerPiece;
            this.board[pos2.getRow() + backwards][pos2
                    .getColumn()] = pieceReplaced;
            this.board[pos2.getRow()][pos2.getColumn()] = null;
            this.piecesMoved--;
            this.updateLineOfSights();
            throw new KingInCheckException(
                    "The Input is Invalid Since the King is In Check After Move - Please try again...");
        }
        return pieceReplaced;
    }

    @Override
    public int getPiecesMoved() {
        return this.piecesMoved;
    }

    @Override
    public void setPiecesMoved(int pm) {
        this.piecesMoved = pm;
    }

    @Override
    public boolean insufficientMaterialDraw() {
        Collection<Position> allBlackRooks = this.getPieces(Piece.Color.BLACK,
                Piece.Type.ROOK);
        Collection<Position> allBlackBishops = this.getPieces(Piece.Color.BLACK,
                Piece.Type.BISHOP);
        Collection<Position> allBlackKnights = this.getPieces(Piece.Color.BLACK,
                Piece.Type.KNIGHT);
        Collection<Position> allBlackQueens = this.getPieces(Piece.Color.BLACK,
                Piece.Type.QUEEN);
        Collection<Position> allBlackPawns = this.getPieces(Piece.Color.BLACK,
                Piece.Type.PAWN);
        Collection<Position> allWhiteRooks = this.getPieces(Piece.Color.WHITE,
                Piece.Type.ROOK);
        Collection<Position> allWhiteBishops = this.getPieces(Piece.Color.WHITE,
                Piece.Type.BISHOP);
        Collection<Position> allWhiteKnights = this.getPieces(Piece.Color.WHITE,
                Piece.Type.KNIGHT);
        Collection<Position> allWhiteQueens = this.getPieces(Piece.Color.WHITE,
                Piece.Type.QUEEN);
        Collection<Position> allWhitePawns = this.getPieces(Piece.Color.WHITE,
                Piece.Type.PAWN);
        if ((allBlackRooks.size() == 0 && allBlackBishops.size() == 0
                && allBlackKnights.size() == 0 && allBlackQueens.size() == 0
                && allBlackPawns.size() == 0 && allWhiteRooks.size() == 0
                && allWhitePawns.size() == 0 && allWhiteQueens.size() == 0
                && ((allWhiteBishops.size() == 1 && allWhiteKnights.size() == 0)
                        || (allWhiteBishops.size() == 0
                                && allWhiteKnights.size() == 1)))
                || (allWhiteRooks.size() == 0 && allWhiteBishops.size() == 0
                        && allWhiteKnights.size() == 0
                        && allWhiteQueens.size() == 0
                        && allWhitePawns.size() == 0
                        && allBlackRooks.size() == 0
                        && allBlackPawns.size() == 0
                        && allBlackQueens.size() == 0
                        && ((allBlackBishops.size() == 1
                                && allBlackKnights.size() == 0)
                                || (allBlackBishops.size() == 0
                                        && allBlackKnights.size() == 1)))
                || (allBlackRooks.size() == 0 && allBlackBishops.size() == 0
                        && allBlackKnights.size() == 0
                        && allBlackQueens.size() == 0
                        && allBlackPawns.size() == 0
                        && allWhiteRooks.size() == 0
                        && allWhiteBishops.size() == 0
                        && allWhiteKnights.size() == 0
                        && allWhiteQueens.size() == 0
                        && allWhitePawns.size() == 0)
                || (allBlackRooks.size() == 0 && allBlackBishops.size() == 1
                        && allBlackKnights.size() == 0
                        && allBlackQueens.size() == 0
                        && allBlackPawns.size() == 0
                        && allWhiteRooks.size() == 0
                        && allWhiteBishops.size() == 1
                        && allWhiteKnights.size() == 0
                        && allWhiteQueens.size() == 0
                        && allWhitePawns.size() == 0
                        && ((((Position) (allBlackBishops.toArray()[0]))
                                .getRow()
                                % 2 == ((Position) (allBlackBishops
                                        .toArray()[0])).getColumn() % 2
                                && ((Position) (allWhiteBishops.toArray()[0]))
                                        .getRow()
                                        % 2 == ((Position) (allWhiteBishops
                                                .toArray()[0])).getColumn() % 2)
                                || (((Position) (allBlackBishops.toArray()[0]))
                                        .getRow()
                                        % 2 != ((Position) (allBlackBishops
                                                .toArray()[0])).getColumn() % 2
                                        && ((Position) (allWhiteBishops
                                                .toArray()[0])).getRow()
                                                % 2 != ((Position) (allWhiteBishops
                                                        .toArray()[0]))
                                                                .getColumn()
                                                        % 2)))) {
            return true;
        }
        return false;
    }
}
