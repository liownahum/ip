package exceptions;

/**
 * Base exception class for all custom exceptions in the Grower application.
 * This allows for catching all application-specific errors with a single catch block
 */
public class GrowerException extends Exception {
    public GrowerException(String message) {
        super(message);
    }
}
