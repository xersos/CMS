package be.zwaldeck.zcms.core.common.rest.error;

import be.zwaldeck.zcms.core.common.error.ZCMSError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiErrorType implements ZCMSError {

    NOT_FOUND("not_found"),
    INTERNAL_ERROR("internal_error"),
    ACCESS_DENIED("access_denied"),
    EXPIRED_TOKEN("expired_token"),
    VALIDATION_ERROR("validation_error");

    private String key;

}
