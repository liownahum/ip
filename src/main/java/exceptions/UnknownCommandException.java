package exceptions;

/**
 * Error representing an unkown input command
 */

public class UnknownCommandException extends GrowerException {
    public UnknownCommandException(String message) {
        super(message);
    }
}
