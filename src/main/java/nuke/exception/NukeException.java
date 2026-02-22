package nuke.exception;

/**
 * The specific exception for Nuke chatbot, it is wrapper around Exception only
 */
public class NukeException extends Exception {
    /**
     * Create an {@link Exception}
     * @param message the message of the exception
     */
    public NukeException(String message) {
        super(message);
    }
}
