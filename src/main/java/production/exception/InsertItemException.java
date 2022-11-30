package production.exception;

/**
 * Is thrown when user inserts single item multiple times in a factory/store.
 * Has three different constructors depending on the parameter sent.
 */
public class InsertItemException extends Exception {

    public InsertItemException(String message) {
        super(message);
    }

    public InsertItemException(Throwable cause) {
        super(cause);
    }

    public InsertItemException(String message, Throwable cause) {
        super(message, cause);
    }
}
