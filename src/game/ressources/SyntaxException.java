package game.ressources;

import java.io.Serial;

/**
 * An exception thrown if Syntax is wrong/Parsing Failed
 * @author Johannes Stephan
 * @version 1.0
 */
public final class SyntaxException extends TicTacToeException {
    /**
     * Generated serial version UID.
     */
    @Serial
    private static final long serialVersionUID = 8533547557133386227L;

    /**
     * Constructs a exception with message.
     * @param message the message describing the exception
     */
    public SyntaxException(String message) {
        super(message);
    }
}
