package be.zwaldeck.zcms.core.common.rest.error;

import be.zwaldeck.zcms.core.common.error.ZCMSError;
import lombok.Getter;

@Getter
public class ApiError {

    private int status;
    private String key;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getDetail() {
        return detail;
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }

    private Object detail;

    public ApiError(int statusCode, ZCMSError error) {
        this(statusCode, error, null);
    }

    public ApiError(int statusCode, ZCMSError error, Object detail) {
        status = statusCode;
        this.key = error.getKey();
        this.detail = detail;
    }

}
