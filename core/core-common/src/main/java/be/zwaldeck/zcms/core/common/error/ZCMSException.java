package be.zwaldeck.zcms.core.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ZCMSException extends RuntimeException {

    public ZCMSError getError() {
        return error;
    }

    public void setError(ZCMSError error) {
        this.error = error;
    }

    private ZCMSError error;

    public ZCMSException(ZCMSError error) {
        super(error.getKey());
        this.error = error;
    }

    public ZCMSException(ZCMSError error, Throwable cause) {
        super(error.getKey(), cause);
        this.error = error;
    }
}
