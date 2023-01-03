package com.epam.esm.web.exceptions;


import com.epam.esm.persistance.entity.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;
import org.springframework.http.HttpStatus;

/**
 * Data class created with purpose to simplify creation of response for specific error when process {@link Tag}.
 *
 * @author Tamerlan Hurbanov
 * @see ErrorResponse
 * @since 1.0
 */
@Value
public class TagOperationErrorResponse implements ErrorResponse{

    @JsonIgnore
    HttpStatus httpStatus;

    int errorCode;

    String errorMessage;

    public TagOperationErrorResponse(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = calculateErrorCode(httpStatus,20);
        this.errorMessage = errorMessage;
    }

}
