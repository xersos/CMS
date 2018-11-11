package be.zwaldeck.author.exception;

import be.zwaldeck.zcms.core.common.error.ZCMSException;
import be.zwaldeck.zcms.core.common.rest.error.ApiError;
import be.zwaldeck.zcms.core.common.rest.error.ApiErrorType;
import be.zwaldeck.zcms.core.common.rest.error.NotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleValidationError(MethodArgumentNotValidException ex) {
        var result = ex.getBindingResult();
        var errors = result.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

        return new ApiError(HttpStatus.BAD_REQUEST.value(), ApiErrorType.VALIDATION_ERROR, errors);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleNotFound(NotFoundException ex) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), ApiErrorType.NOT_FOUND, ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ZCMSException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleApiException(ZCMSException ex) {
        return new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getError());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiError handleAccessDenied(Exception ex) {
        ex.printStackTrace();
        return new ApiError(HttpStatus.UNAUTHORIZED.value(), ApiErrorType.INTERNAL_ERROR, ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiError handleAllOtherExceptions(Exception ex) {
        ex.printStackTrace();
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ApiErrorType.INTERNAL_ERROR, ex.getMessage());
    }
}
