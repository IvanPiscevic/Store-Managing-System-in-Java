package production.exception;

/**
 * Is thrown when user inserts single category multiple times.
 * Has three different constructors depending on the parameter sent.
 */
public class InsertCategoryException extends RuntimeException {

    public InsertCategoryException(String message) {
        super(message);
    }

    public InsertCategoryException(Throwable cause) {
        super(cause);
    }

    public InsertCategoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
