package vip.isass.core.exception;

/**
 * @author Rain
 */
public class AbsentException extends RuntimeException {

    public AbsentException() {
        super();
    }

    public AbsentException(String message) {
        super(message);
    }

    public AbsentException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbsentException(Throwable cause) {
        super(cause);
    }

}
