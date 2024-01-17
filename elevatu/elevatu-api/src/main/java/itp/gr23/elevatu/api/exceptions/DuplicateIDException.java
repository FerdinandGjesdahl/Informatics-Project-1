package itp.gr23.elevatu.api.exceptions;

/**
 * Exception thrown when a duplicate ID is found.
 */
public class DuplicateIDException extends Exception {
    /**
     * Create a new DuplicateIDException.
     * @param errorMessage Error message
     */
    public DuplicateIDException(final String errorMessage) {
        super(errorMessage);
    }
}
