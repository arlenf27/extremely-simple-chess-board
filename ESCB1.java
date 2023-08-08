package escb1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Simulates a chess board. User can move any piece anywhere on the board. Input
 * is taken as a String using Algebraic Notation.
 *
 * @author Arlen Feng
 *
 */
public class ESCB1 {

    /**
     * Parses the string input entered in the form of algebraic notation and
     * sets pos1 and pos2 to valid locations if possible.
     *
     * @param newBoard
     *            the chess board
     * @param color
     *            the color of the piece
     * @param listOfErrors
     *            a list of errors to add to if the string has errors
     * @param s
     *            the string input
     * @param pos1
     *            initial position to update
     * @param pos2
     *            final position to update
     * @param inputRow
     *            current row of the piece (can be null if input does not
     *            provide one)
     * @param inputColumn
     *            current column of the piece (can be null if input does not
     *            provide one)
     * @param capture
     *            whether or not a piece is to be taken
     * @param promote
     *            whether or not a piece is to be promoted
     * @param lastMove
     *            the last move (used to determine the validity of en passant)
     * @return a string that denotes one of several special moves such as
     *         "shortcastle", "longcastle", "check", "checkmate", "promote",
     *         "shortcastlecheck", "longcastlecheck", "promote?check",
     *         "shortcastlecheckmate", "longcastlecheckmate",
     *         "promote?checkmate", "enpassant", "enpassantcheck",
     *         "enpassantcheckmate", or "" for normal moves or captures; if a
     *         move is a promotion, then include the first initial of the type
     *         of piece to convert to (ex: "promoteQ+checkmate") such that the
     *         initial is always at the 7th index of the string
     */
    public static String parseInput(Board newBoard, Piece.Color color,
            HashSet<String> listOfErrors, String s, Position pos1,
            Position pos2, Integer inputRow, Integer inputColumn,
            boolean capture, boolean promote, String lastMove) {
        /*
         * Checks whether this is a "special" move or not.
         */
        if (!s.contains("x") && !s.contains("+") && !s.contains("#")
                && !s.contains("0-0") && !s.contains("0-0-0")
                && !s.contains("=")) {
            /*
             * If the length of the input is 2 (e.g. "e4"), then it must be a
             * pawn move.
             */
            if (s.length() == 2) {
                int finalRow = 8 - Character.getNumericValue(s.charAt(1));
                int finalColumn = s.charAt(0) - 97;
                if (finalRow < 0 || finalRow > 7 || finalColumn < 0
                        || finalColumn > 7) {
                    System.err.println(
                            "Final Destination on Board Must be a Valid Location on Board - Please try again...");
                    return "";
                }
                /*
                 * Checks if the destination is empty or not. If it is already
                 * taken, then the pawn cannot move there since it can only take
                 * diagonally.
                 */
                if (newBoard.getBoardAsArray()[finalRow][finalColumn] == null) {
                    /*
                     * Checks if there is a pawn one row back to move.
                     */
                    int squaresToMoveBack = 1;
                    if (color == Piece.Color.BLACK) {
                        squaresToMoveBack = -1;
                    }
                    if (newBoard.getBoardAsArray()[finalRow
                            + squaresToMoveBack][finalColumn] != null
                            && newBoard.getBoardAsArray()[finalRow
                                    + squaresToMoveBack][finalColumn]
                                            .getColor() == color
                            && newBoard.getBoardAsArray()[finalRow
                                    + squaresToMoveBack][finalColumn]
                                            .getType() == Piece.Type.PAWN) {
                        if ((color == Piece.Color.BLACK && finalRow != 7)
                                || (color == Piece.Color.WHITE && finalRow != 0)
                                || promote) {
                            pos1.setRow(finalRow + squaresToMoveBack);
                            pos1.setColumn(finalColumn);
                            pos2.setRow(finalRow);
                            pos2.setColumn(finalColumn);
                        } else {
                            listOfErrors.add(
                                    "Pawn must promote if it reaches the end of the board");
                        }
                        /*
                         * If not, then check if there is a pawn two rows back
                         * AND it has not been moved yet.
                         */
                    } else if (newBoard.getBoardAsArray()[finalRow
                            + 2 * squaresToMoveBack][finalColumn] != null
                            && newBoard.getBoardAsArray()[finalRow
                                    + 2 * squaresToMoveBack][finalColumn]
                                            .getColor() == color
                            && newBoard.getBoardAsArray()[finalRow
                                    + 2 * squaresToMoveBack][finalColumn]
                                            .getType() == Piece.Type.PAWN
                            && ((color == Piece.Color.WHITE && finalRow == 4)
                                    || (color == Piece.Color.BLACK
                                            && finalRow == 3))) {
                        if (newBoard.getBoardAsArray()[finalRow
                                + squaresToMoveBack][finalColumn] == null) {
                            pos1.setRow(finalRow + 2 * squaresToMoveBack);
                            pos1.setColumn(finalColumn);
                            pos2.setRow(finalRow);
                            pos2.setColumn(finalColumn);
                        } else {
                            listOfErrors.add(
                                    "No Pawn Available to Move to Destination Square");
                        }
                    } else {
                        listOfErrors.add(
                                "No Pawn Available to Move to Destination Square");
                    }
                } else {
                    listOfErrors.add("Destination Square is Taken");
                }
            } else if (s.length() == 3) {
                /*
                 * If this is not a "special" move and its length is 3, then it
                 * must be a move made by a piece that is not a pawn. Nothing is
                 * taken. Checks if destination square is empty, if the move is
                 * technically legal, and if there are no pieces blocking the
                 * intended path (non-applicable for knights).
                 */
                int finalRow = 8 - Character.getNumericValue(s.charAt(2));
                int finalColumn = s.charAt(1) - 97;
                if (finalRow < 0 || finalRow > 7 || finalColumn < 0
                        || finalColumn > 7) {
                    System.err.println(
                            "Final Destination on Board Must be a Valid Location on Board - Please try again...");
                    return "";
                }
                Piece.Type type = null;
                int availableOriginalPositions = 0;
                if (s.charAt(0) == 'K') {
                    type = Piece.Type.KING;
                } else if (s.charAt(0) == 'Q') {
                    type = Piece.Type.QUEEN;
                } else if (s.charAt(0) == 'N') {
                    type = Piece.Type.KNIGHT;
                } else if (s.charAt(0) == 'B') {
                    type = Piece.Type.BISHOP;
                } else if (s.charAt(0) == 'R') {
                    type = Piece.Type.ROOK;
                }
                if ((!capture && newBoard
                        .getBoardAsArray()[finalRow][finalColumn] == null)
                        || (capture && newBoard
                                .getBoardAsArray()[finalRow][finalColumn] != null
                                && newBoard
                                        .getBoardAsArray()[finalRow][finalColumn]
                                                .getColor() != color)) {
                    Collection<Position> possiblePositionsOfPiece = newBoard
                            .getPieces(color, type);
                    for (Position current : possiblePositionsOfPiece) {
                        if ((inputRow != null && inputColumn == null
                                && current.getRow() == inputRow)
                                || (inputColumn != null && inputRow == null
                                        && current.getColumn() == inputColumn)
                                || (inputRow != null && inputColumn != null
                                        && current.getRow() == inputRow
                                        && current.getColumn() == inputColumn)
                                || (inputRow == null && inputColumn == null)) {
                            int currentRow = current.getRow();
                            int currentColumn = current.getColumn();
                            Collection<Position> sights = newBoard
                                    .getBoardAsArray()[currentRow][currentColumn]
                                            .lineOfSight();
                            for (Position sight : sights) {
                                if (finalRow == sight.getRow()
                                        && finalColumn == sight.getColumn()) {
                                    pos1.setRow(currentRow);
                                    pos1.setColumn(currentColumn);
                                    availableOriginalPositions++;
                                }
                            }
                        }
                    }
                } else {
                    if (!capture) {
                        listOfErrors.add("Destination Square is Taken");
                    } else {
                        listOfErrors.add("No Piece to capture");
                    }
                }
                if (type != null) {
                    if (availableOriginalPositions == 1) {
                        pos2.setRow(finalRow);
                        pos2.setColumn(finalColumn);
                    } else if (availableOriginalPositions == 0) {
                        if (!capture) {
                            listOfErrors.add("No "
                                    + type.toString().substring(0, 1)
                                    + type.toString().substring(1).toLowerCase()
                                    + " Available to Move to Destination Square");
                        } else {
                            listOfErrors.add("No "
                                    + type.toString().substring(0, 1)
                                    + type.toString().substring(1).toLowerCase()
                                    + " Available to Take on Destination Square");
                        }
                    } else {
                        listOfErrors.add("Multiple "
                                + type.toString().substring(0, 1)
                                + type.toString().substring(1).toLowerCase()
                                + " Can Move to the Same Destination Square");
                    }
                } else {
                    listOfErrors.add("Piece must be of a valid type");
                }
            } else if (s.length() == 4) {
                /*
                 * If the length of the string is 4 and there are no "special"
                 * characters, then there must be at least two pieces that can
                 * move to the same destination; either the rank or file is
                 * explicitly stated after the piece initial.
                 */
                if (s.charAt(1) >= 97 && s.charAt(1) <= 104) {
                    int currentColumn = s.charAt(1) - 97;
                    s = s.substring(0, 1) + s.substring(2);
                    parseInput(newBoard, color, listOfErrors, s, pos1, pos2,
                            null, currentColumn, capture, promote, lastMove);
                } else if (s.charAt(1) >= 1 && s.charAt(1) <= 8) {
                    int currentRow = 8 - Character.getNumericValue(s.charAt(1));
                    s = s.substring(0, 1) + s.substring(2);
                    parseInput(newBoard, color, listOfErrors, s, pos1, pos2,
                            currentRow, null, capture, promote, lastMove);
                }
            } else if (s.length() == 5) {
                /*
                 * If the length of the string is 5 and there are no "special"
                 * characters, then both the rank and file is stated to identify
                 * a piece.
                 */
                if ((s.charAt(1) >= 97 && s.charAt(1) <= 104)
                        && (s.charAt(2) >= 1 && s.charAt(2) <= 8)) {
                    int currentColumn = s.charAt(1) - 97;
                    int currentRow = 8 - Character.getNumericValue(s.charAt(2));
                    s = s.substring(0, 1) + s.substring(3);
                    parseInput(newBoard, color, listOfErrors, s, pos1, pos2,
                            currentRow, currentColumn, capture, promote,
                            lastMove);
                }
            }
        } else if (s.contains("x") && !s.contains("=") && !s.contains("+")
                && !s.contains("#") && !s.contains("0-0")
                && !s.contains("0-0-0")) {
            /*
             * If the first character of the string is a lowercase letter, then
             * it is guaranteed to be a pawn capture. The string is also
             * guaranteed to be of length 4 (ex: fxg4).
             */
            if (s.charAt(0) >= 97 && s.charAt(0) <= 104) {
                int finalRow = 8 - Character.getNumericValue(s.charAt(3));
                int finalColumn = s.charAt(2) - 97;
                int initialColumn = s.charAt(0) - 97;
                if (finalRow < 0 || finalRow > 7 || finalColumn < 0
                        || finalColumn > 7 || initialColumn < 0
                        || initialColumn > 7) {
                    System.err.println(
                            "Final Destination on Board Must be a Valid Location on Board - Please try again...");
                    return "";
                }
                int squaresToMoveBack = 1;
                if (color == Piece.Color.BLACK) {
                    squaresToMoveBack = -1;
                }
                if (newBoard.getBoardAsArray()[finalRow][finalColumn] != null
                        && newBoard.getBoardAsArray()[finalRow][finalColumn]
                                .getColor() != color) {
                    if (Math.abs(finalColumn - initialColumn) == 1
                            && newBoard.getBoardAsArray()[finalRow
                                    + squaresToMoveBack][initialColumn] != null
                            && newBoard.getBoardAsArray()[finalRow
                                    + squaresToMoveBack][initialColumn]
                                            .getColor() == color
                            && newBoard.getBoardAsArray()[finalRow
                                    + squaresToMoveBack][initialColumn]
                                            .getType() == Piece.Type.PAWN) {
                        if ((color == Piece.Color.BLACK && finalRow != 7)
                                || (color == Piece.Color.WHITE && finalRow != 0)
                                || promote) {
                            pos1.setRow(finalRow + squaresToMoveBack);
                            pos1.setColumn(initialColumn);
                            pos2.setRow(finalRow);
                            pos2.setColumn(finalColumn);
                        } else {
                            listOfErrors.add(
                                    "Pawn must promote if it reaches the end of the board");
                        }
                    } else {
                        listOfErrors.add(
                                "No Pawn Available to Move to Destination Square");
                    }
                    /*
                     * Special scenario for en passant captures
                     */
                } else if (newBoard
                        .getBoardAsArray()[finalRow][finalColumn] == null
                        && newBoard.getBoardAsArray()[finalRow
                                + squaresToMoveBack][finalColumn] != null
                        && newBoard.getBoardAsArray()[finalRow
                                + squaresToMoveBack][finalColumn]
                                        .getColor() != color
                        && lastMove.length() == 2
                        && 8 - Character
                                .getNumericValue(lastMove.charAt(1)) == finalRow
                                        + squaresToMoveBack
                        && lastMove.charAt(0) - 97 == finalColumn
                        && newBoard.getBoardAsArray()[finalRow
                                + squaresToMoveBack][finalColumn]
                                        .getTimesMoved() == 1) {
                    pos1.setRow(finalRow + squaresToMoveBack);
                    pos1.setColumn(initialColumn);
                    pos2.setRow(finalRow);
                    pos2.setColumn(finalColumn);
                    return "enpassant";
                } else {
                    listOfErrors.add("No Piece to Capture");
                }
            } else {
                int indexOfX = s.indexOf("x");
                s = s.substring(0, indexOfX) + s.substring(indexOfX + 1);
                parseInput(newBoard, color, listOfErrors, s, pos1, pos2, null,
                        null, true, promote, lastMove);
            }
        } else if (s.equals("0-0")) {
            int row = 7;
            if (color == Piece.Color.BLACK) {
                row = 0;
            }
            boolean squaresCovered = false;
            for (Piece[] currentRow : newBoard.getBoardAsArray()) {
                for (Piece current : currentRow) {
                    if (current != null && current.getColor() != color) {
                        Iterator<Position> i = current.lineOfSight().iterator();
                        while (i.hasNext()) {
                            Position next = i.next();
                            if ((next.getRow() == row && next.getColumn() == 5)
                                    || (next.getRow() == row
                                            && next.getColumn() == 6)) {
                                squaresCovered = true;
                            }
                        }
                    }
                }
            }
            if (!newBoard.kingInCheck(color)
                    && newBoard.getBoardAsArray()[row][4] != null
                    && newBoard.getBoardAsArray()[row][4]
                            .getType() == Piece.Type.KING
                    && newBoard.getBoardAsArray()[row][4].getTimesMoved() == 0
                    && newBoard.getBoardAsArray()[row][7] != null
                    && newBoard.getBoardAsArray()[row][7]
                            .getType() == Piece.Type.ROOK
                    && newBoard.getBoardAsArray()[row][7].getTimesMoved() == 0
                    && newBoard.getBoardAsArray()[row][5] == null
                    && newBoard.getBoardAsArray()[row][6] == null
                    && !squaresCovered) {
                return "shortcastle";
            } else {
                listOfErrors.add("Unable to Castle");
            }
        } else if (s.equals("0-0-0")) {
            int row = 7;
            if (color == Piece.Color.BLACK) {
                row = 0;
            }
            boolean squaresCovered = false;
            for (Piece[] currentRow : newBoard.getBoardAsArray()) {
                for (Piece current : currentRow) {
                    if (current != null && current.getColor() != color) {
                        Iterator<Position> i = current.lineOfSight().iterator();
                        while (i.hasNext()) {
                            Position next = i.next();
                            if ((next.getRow() == row && next.getColumn() == 1)
                                    || (next.getRow() == row
                                            && next.getColumn() == 2)
                                    || (next.getRow() == row
                                            && next.getColumn() == 3)) {
                                squaresCovered = true;
                            }
                        }
                    }
                }
            }
            if (!newBoard.kingInCheck(color)
                    && newBoard.getBoardAsArray()[row][4] != null
                    && newBoard.getBoardAsArray()[row][4]
                            .getType() == Piece.Type.KING
                    && newBoard.getBoardAsArray()[row][4].getTimesMoved() == 0
                    && newBoard.getBoardAsArray()[row][0] != null
                    && newBoard.getBoardAsArray()[row][0]
                            .getType() == Piece.Type.ROOK
                    && newBoard.getBoardAsArray()[row][0].getTimesMoved() == 0
                    && newBoard.getBoardAsArray()[row][1] == null
                    && newBoard.getBoardAsArray()[row][2] == null
                    && newBoard.getBoardAsArray()[row][3] == null
                    && !squaresCovered) {
                return "longcastle";
            } else {
                listOfErrors.add("Unable to Castle");
            }
        } else if (s.contains("=") && !s.contains("+") && !s.contains("#")) {
            if ((s.charAt(s.length() - 1) == 'Q'
                    || s.charAt(s.length() - 1) == 'R'
                    || s.charAt(s.length() - 1) == 'B'
                    || s.charAt(s.length() - 1) == 'N')
                    && s.charAt(s.length() - 2) == '=') {
                return "promote" + s.charAt(s.length() - 1)
                        + parseInput(newBoard, color, listOfErrors,
                                s.substring(0, s.length() - 2), pos1, pos2,
                                null, null, capture, true, lastMove);
            } else {
                listOfErrors.add("Illegal Promotion");
            }
        } else if (s.contains("+") && !s.contains("#")) {
            return parseInput(newBoard, color, listOfErrors,
                    s.substring(0, s.length() - 1), pos1, pos2, null, null,
                    capture, promote, lastMove) + "check";
        } else if (s.contains("#") && !s.contains("+")) {
            return parseInput(newBoard, color, listOfErrors,
                    s.substring(0, s.length() - 1), pos1, pos2, null, null,
                    capture, promote, lastMove) + "checkmate";
        }
        return "";
    }

    public static void main(String[] args) {
        /*
         * Initializes a new board.
         */
        Board newBoard = new Board1();
        System.out.println(
                "An extremely simple chess board that takes input in the form of Standard Algebraic Notation (SAN). It is important to note that \n\t1) This program does not accept the letter 'P' as an initial for pawn moves; in these cases, the 'P' is ommitted\n\t2) This program does not accept 'e.p' as part of an annotation; it will auto-recognize the validity of en passant moves\n\t3) This program does not allow the presence of additional annotations on the quality of moves (such as '!' or '!!' or '?!')\n\t4) The symbol '#' is used for checkmates instead of '++'\n\t5) Unfortunately, I am not completely familiar with SAN; I have tried my best to make sure all possible inputs are accounted for, but I could have misunderstood a few things about the notation\n\t6) If an invalid input was given, then the program will prompt for the input again\n\t7) It is completely UNNECESSARY to include '+' or '#' as part of a move for checks or checkmates; however, if one chooses to include either character, then the move MUST be either a check or a checkmate\n\t8) The 50-move Rule is not yet implemented\n\t9) Draw by Repitition is not yet implemented\n\t10) Draw by Agreement is not yet implemented\n\t11) Resignation is not yet implemented");
        System.out.println("Initial State: Move: " + newBoard.getMove()
                + ", Board: \n\n" + newBoard.toStringTable());
        /*
         * If count is even, it is white's turn; otherwise, it is black's turn.
         */
        int count = 0;
        String lastMove = "";
        /*
         * Takes user input (only accepts Algebraic Notation).
         */
        while (true) {
            HashSet<String> listOfErrors = new HashSet<String>();
            if (count % 2 == 0) {
                System.out.print(
                        "Enter a move in Algebraic Notation (WHITE); do not specify the move number: ");
            } else {
                System.out.print(
                        "Enter a move in Algebraic Notation (BLACK); do not specify the move number: ");
            }
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(System.in));
            String s = null;
            try {
                s = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            /*
             * Starts parsing the user input to find pos1 and pos2.
             */
            Position pos1 = new Position(-1, -1);
            Position pos2 = new Position(-1, -1);
            /*
             * Checks if it is White's turn or Black's turn.
             */
            Piece.Color currentColor = null;
            if (count % 2 == 0) {
                currentColor = Piece.Color.WHITE;
            } else {
                currentColor = Piece.Color.BLACK;
            }
            String special = parseInput(newBoard, currentColor, listOfErrors, s,
                    pos1, pos2, null, null, false, false, lastMove);
            Piece pieceTaken = null;
            try {
                if (special.equals("") || special.equals("check")
                        || special.equals("checkmate")) {
                    pieceTaken = newBoard.updateState(pos1, pos2, currentColor,
                            false);
                } else if (special.contains("enpassant")) {
                    pieceTaken = newBoard.enpassant(pos1, pos2, currentColor,
                            false);
                } else if (special.contains("shortcastle")) {
                    newBoard.shortCastle(currentColor);
                } else if (special.contains("longcastle")) {
                    newBoard.longCastle(currentColor);
                } else if (special.contains("promote")) {
                    if (special.charAt(7) == 'Q') {
                        pieceTaken = newBoard.promote(pos1, pos2,
                                Piece.Type.QUEEN, currentColor);
                    } else if (special.charAt(7) == 'R') {
                        pieceTaken = newBoard.promote(pos1, pos2,
                                Piece.Type.ROOK, currentColor);
                    } else if (special.charAt(7) == 'B') {
                        pieceTaken = newBoard.promote(pos1, pos2,
                                Piece.Type.BISHOP, currentColor);
                    } else if (special.charAt(7) == 'N') {
                        pieceTaken = newBoard.promote(pos1, pos2,
                                Piece.Type.KNIGHT, currentColor);
                    }
                }
                Piece.Color oppositeColor = Piece.Color.WHITE;
                if (currentColor == Piece.Color.WHITE) {
                    oppositeColor = Piece.Color.BLACK;
                }
                int squaresToMoveBack = 1;
                if (currentColor == Piece.Color.BLACK) {
                    squaresToMoveBack = -1;
                }
                int row = 7;
                if (currentColor == Piece.Color.BLACK) {
                    row = 0;
                }
                boolean illegal = false;
                if (!special.contains("checkmate")
                        && special.contains("check")) {
                    if (!newBoard.kingInCheck(oppositeColor)) {
                        if (special.equals("check")) {
                            newBoard.updateState(pos2, pos1, currentColor,
                                    true);
                            if (pieceTaken != null) {
                                newBoard.getBoardAsArray()[pos2.getRow()][pos2
                                        .getColumn()] = pieceTaken;
                            }
                            newBoard.setPiecesMoved(
                                    newBoard.getPiecesMoved() - 2);
                        } else if (special.contains("enpassant")) {
                            newBoard.updateState(pos2, pos1, currentColor,
                                    true);
                            if (pieceTaken != null) {
                                newBoard.getBoardAsArray()[pos2.getRow()
                                        + squaresToMoveBack][pos2
                                                .getColumn()] = pieceTaken;
                            }
                            newBoard.setPiecesMoved(
                                    newBoard.getPiecesMoved() - 2);
                        } else if (special.contains("shortcastle")) {
                            newBoard.updateState(new Position(row, 6),
                                    new Position(row, 4), currentColor, true);
                            newBoard.updateState(new Position(row, 5),
                                    new Position(row, 7), currentColor, true);
                            newBoard.setPiecesMoved(
                                    newBoard.getPiecesMoved() - 3);
                        } else if (special.contains("longcastle")) {
                            newBoard.updateState(new Position(row, 2),
                                    new Position(row, 4), currentColor, true);
                            newBoard.updateState(new Position(row, 3),
                                    new Position(row, 0), currentColor, true);
                            newBoard.setPiecesMoved(
                                    newBoard.getPiecesMoved() - 3);
                        } else if (special.contains("promote")) {
                            newBoard.getBoardAsArray()[pos1.getRow()][pos1
                                    .getColumn()] = new Piece1(currentColor,
                                            Piece.Type.PAWN,
                                            new Position(pos1.getRow(),
                                                    pos1.getColumn()));
                            newBoard.getBoardAsArray()[pos1.getRow()][pos1
                                    .getColumn()].setTimesMoved(
                                            newBoard.getBoardAsArray()[pos2
                                                    .getRow()][pos2.getColumn()]
                                                            .getTimesMoved()
                                                    - 1);
                            newBoard.getBoardAsArray()[pos2.getRow()][pos2
                                    .getColumn()] = pieceTaken;
                            newBoard.setPiecesMoved(
                                    newBoard.getPiecesMoved() - 1);
                        }
                        newBoard.updateLineOfSights();
                        System.err.println(
                                "Move is not check - Please try again...");
                        illegal = true;
                    }
                } else if (special.contains("checkmate")) {
                    if (!newBoard.checkmateOrStalemate(oppositeColor, true,
                            lastMove)) {
                        if (special.equals("checkmate")) {
                            newBoard.updateState(pos2, pos1, currentColor,
                                    true);
                            if (pieceTaken != null) {
                                newBoard.getBoardAsArray()[pos2.getRow()][pos2
                                        .getColumn()] = pieceTaken;
                            }
                            newBoard.setPiecesMoved(
                                    newBoard.getPiecesMoved() - 2);
                        } else if (special.contains("enpassant")) {
                            newBoard.updateState(pos2, pos1, currentColor,
                                    true);
                            if (pieceTaken != null) {
                                newBoard.getBoardAsArray()[pos2.getRow()
                                        + squaresToMoveBack][pos2
                                                .getColumn()] = pieceTaken;
                            }
                            newBoard.setPiecesMoved(
                                    newBoard.getPiecesMoved() - 2);
                        } else if (special.contains("shortcastle")) {
                            newBoard.updateState(new Position(row, 6),
                                    new Position(row, 4), currentColor, true);
                            newBoard.updateState(new Position(row, 5),
                                    new Position(row, 7), currentColor, true);
                            newBoard.setPiecesMoved(
                                    newBoard.getPiecesMoved() - 3);
                        } else if (special.contains("longcastle")) {
                            newBoard.updateState(new Position(row, 2),
                                    new Position(row, 4), currentColor, true);
                            newBoard.updateState(new Position(row, 3),
                                    new Position(row, 0), currentColor, true);
                            newBoard.setPiecesMoved(
                                    newBoard.getPiecesMoved() - 3);
                        } else if (special.contains("promote")) {
                            newBoard.getBoardAsArray()[pos1.getRow()][pos1
                                    .getColumn()] = new Piece1(currentColor,
                                            Piece.Type.PAWN,
                                            new Position(pos1.getRow(),
                                                    pos1.getColumn()));
                            newBoard.getBoardAsArray()[pos1.getRow()][pos1
                                    .getColumn()].setTimesMoved(
                                            newBoard.getBoardAsArray()[pos2
                                                    .getRow()][pos2.getColumn()]
                                                            .getTimesMoved()
                                                    - 1);
                            newBoard.getBoardAsArray()[pos2.getRow()][pos2
                                    .getColumn()] = pieceTaken;
                            newBoard.setPiecesMoved(
                                    newBoard.getPiecesMoved() - 1);
                        }
                        newBoard.updateLineOfSights();
                        System.err.println(
                                "Move is not checkmate - Please try again...");
                        illegal = true;
                    }
                }
                if (newBoard.checkmateOrStalemate(oppositeColor, true,
                        lastMove)) {
                    System.out
                            .println("Checkmate, " + currentColor + " wins. ");
                    System.out.println("Move: " + newBoard.getMove()
                            + ", Board: \n\n" + newBoard.toStringTable());
                    System.exit(0);
                } else if (newBoard.checkmateOrStalemate(oppositeColor, false,
                        lastMove)) {
                    System.out.println("Stalemate, Draw. ");
                    System.out.println("Move: " + newBoard.getMove()
                            + ", Board: \n\n" + newBoard.toStringTable());
                    System.exit(0);
                } else if (newBoard.insufficientMaterialDraw()) {
                    System.out.println("Insufficient Material, Draw. ");
                    System.out.println("Move: " + newBoard.getMove()
                            + ", Board: \n\n" + newBoard.toStringTable());
                    System.exit(0);
                }
                System.out.println("Current State: Move: " + newBoard.getMove()
                        + ", Board: \n\n" + newBoard.toStringTable());
                if (!illegal) {
                    count++;
                    lastMove = s;
                }
            } catch (IndexOutOfBoundsException e) {
                if (listOfErrors.size() == 0) {
                    System.err.println("Invalid Input - Please try again...");
                } else {
                    for (String current : listOfErrors) {
                        System.err.println(current);
                    }
                    System.err.println("Please try again...");
                }
            } catch (KingInCheckException e) {
                System.err.println(e.getMessage());
            }
        }
    }

}
