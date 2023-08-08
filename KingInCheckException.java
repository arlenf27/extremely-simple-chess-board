package escb1;

/**
 * Represents a custom exception that is used when a move does not deal with a
 * check on the king.
 *
 * @author Arlen Feng
 *
 */
public class KingInCheckException extends Exception {
    private static final long serialVersionUID = 1L;

    public KingInCheckException(String message) {
        super(message);
    }
}
