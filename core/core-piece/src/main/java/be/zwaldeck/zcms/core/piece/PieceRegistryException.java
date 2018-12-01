package be.zwaldeck.zcms.core.piece;

import be.zwaldeck.zcms.core.common.error.ZCMSException;

public class PieceRegistryException extends RuntimeException {

    public PieceRegistryException(String message) {
        super(message);
    }

    public PieceRegistryException(String message, Throwable cause) {
        super(message, cause);
    }
}
