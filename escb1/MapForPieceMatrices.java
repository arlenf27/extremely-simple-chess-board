package escb1;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class used to compare 2D arrays filled with Pieces. Since this class is
 * implemented completely differently from HashMap, ONLY the three overridden
 * methods (containsKey(), get(), put()) work.
 *
 * @author Arlen Feng
 *
 */
public class MapForPieceMatrices extends HashMap<Piece[][], Integer> {

    private static final long serialVersionUID = 1L;

    private ArrayList<Piece[][]> keys;
    private ArrayList<Integer> values;

    public MapForPieceMatrices() {
        this.keys = new ArrayList<Piece[][]>();
        this.values = new ArrayList<Integer>();
    }

    /**
     * Overridden to work with Piece matrices.
     */
    @Override
    public boolean containsKey(Object key) {
        Piece[][] outsideMatrix = (Piece[][]) key;
        for (Piece[][] current : this.keys) {
            if (equals(current, outsideMatrix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Overridden to work with Piece matrices.
     */
    @Override
    public Integer get(Object key) {
        Piece[][] outsideMatrix = (Piece[][]) key;
        for (Piece[][] current : this.keys) {
            if (equals(current, outsideMatrix)) {
                return this.values.get(this.keys.indexOf(current));
            }
        }
        return null;
    }

    /**
     * Overridden to work with Piece matrices.
     */
    @Override
    public Integer put(Piece[][] key, Integer value) {
        Integer result = null;
        if (this.containsKey(key)) {
            for (Piece[][] current : this.keys) {
                if (equals(current, key)) {
                    result = this.values.remove(this.keys.indexOf(current));
                    this.values.add(this.keys.indexOf(current), value);
                }
            }
        } else {
            this.keys.add(key);
            this.values.add(value);
        }
        return result;
    }

    /**
     * Compares two Piece matrices
     *
     * @param one
     *            the first matrix
     * @param two
     *            the second matrix
     * @return true if the two matrices are equal
     */
    private static boolean equals(Piece[][] one, Piece[][] two) {
        for (int i = 0; i < one.length; i++) {
            for (int j = 0; j < one[i].length; j++) {
                if ((one[i][j] != null && two[i][j] != null
                        && !one[i][j].equals(two[i][j]))
                        || (one[i][j] == null && two[i][j] != null)
                        || (one[i][j] != null && two[i][j] == null)) {
                    return false;
                }
            }
        }
        return true;
    }
}
