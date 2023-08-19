package escb1;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Arlen Feng
 *
 */
public class Piece1 implements Piece {

    private Type type;
    private Color color;
    private Position pos;
    private int timesMoved;
    private Set<Position> lineOfSight;

    /**
     * The initial representation of a piece takes into account the intended
     * color, type, and position of the piece on the board.
     *
     * @param color
     *            the color of the piece
     * @param type
     *            the type of the piece
     * @param pos
     *            the position of the piece
     */
    public Piece1(Color color, Type type, Position pos) {
        this.color = color;
        this.type = type;
        this.pos = pos;
        this.timesMoved = 0;
        this.lineOfSight = new HashSet<Position>();
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return this.color.toString() + ": " + this.type.toString();
    }

    @Override
    public Position getPosition() {
        return this.pos;
    }

    @Override
    public void updatePosition(Position pos2) {
        this.pos = pos2;
        this.timesMoved++;

    }

    @Override
    public int getTimesMoved() {
        return this.timesMoved;
    }

    @Override
    public void setTimesMoved(int timesMoved) {
        this.timesMoved = timesMoved;
    }

    @Override
    public Set<Position> lineOfSight() {
        return this.lineOfSight;
    }

    @Override
    public void addToLineOfSight(Position pos) {
        Iterator<Position> i = this.lineOfSight.iterator();
        while (i.hasNext()) {
            Position next = i.next();
            if (next.getRow() == pos.getRow()
                    && next.getColumn() == pos.getColumn()) {
                System.err.println(
                        "ERROR: Nothing was added; Position is already in set");
                return;
            }
        }
        this.lineOfSight.add(pos);
    }

    @Override
    public void clearLineOfSight() {
        this.lineOfSight.clear();
    }

    /**
     * Should only be used to check whether a board's position at a particular
     * point in time is equal to another position through the
     * Arrays.deepEquals() method; does not take into account line of sight or
     * the times moved; only compares type, color, and position.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Piece)) {
            return false;
        }
        Piece p = (Piece) obj;
        if (this.getType() != p.getType()) {
            return false;
        }
        if (this.getColor() != p.getColor()) {
            return false;
        }
        if (this.getPosition().getRow() != p.getPosition().getRow() || this
                .getPosition().getColumn() != p.getPosition().getColumn()) {
            return false;
        }
        return true;
    }
}
