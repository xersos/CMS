package be.zwaldeck.zcms.repository.api.exception;


public class BrakingRepositoryException extends Exception {

    public BrakingRepositoryException(String msg) {
        super(msg);
    }

    public BrakingRepositoryException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
