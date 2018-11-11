package be.zwaldeck.zcms.repository.api.exception;

import be.zwaldeck.zcms.core.common.error.ZCMSException;

public class RepositoryException extends ZCMSException {

    public RepositoryException(RepositoryError error) {
        super(error);
    }

    public RepositoryException(RepositoryError error, Throwable cause) {
        super(error, cause);
    }
}
