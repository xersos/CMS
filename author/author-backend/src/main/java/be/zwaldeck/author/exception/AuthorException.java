package be.zwaldeck.author.exception;

import be.zwaldeck.zcms.core.common.error.ZCMSException;

public class AuthorException extends ZCMSException {

    public AuthorException(AuthorError error) {
        super(error);
    }

    public AuthorException(AuthorError error, Throwable cause) {
        super(error, cause);
    }
}
