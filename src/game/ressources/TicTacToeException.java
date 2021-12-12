package game.ressources;

/**
 * Parent class of the Exceptions, which are thrown
 * @author Johannes Stephan
 * @version 1.0
 */
public abstract class TicTacToeException extends Exception {
    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = 7123547557633386527L;

    /**
     * Constructs a exception with message.
     * @param message the message describing the exception
     */
    protected TicTacToeException(String message) {
        super(message);
    }


}
