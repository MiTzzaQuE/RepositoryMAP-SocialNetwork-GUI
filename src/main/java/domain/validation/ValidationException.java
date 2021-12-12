package domain.validation;

/**
 * ValidationException class
 */
public class ValidationException extends RuntimeException {
    /**
     * constructor for the validationException
     */
    public ValidationException() {
    }

    /**
     * constructor for the validationException
     * @param message of the error
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * constructor for the validationException
     * @param message of the error
     * @param cause of the error
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * constructor for the validationException
     * @param cause the cause of the problem
     */
    public ValidationException(Throwable cause) {
        super(cause);
    }

    /**
     * constructor for the validationException
     * @param message message of the error
     * @param cause cause of the error
     * @param enableSuppression makes the things better
     * @param writableStackTrace not a clue
     */
    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
