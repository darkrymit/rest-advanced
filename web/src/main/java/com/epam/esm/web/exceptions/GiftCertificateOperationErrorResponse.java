package com.epam.esm.web.exceptions;

import com.epam.esm.persistance.entity.GiftCertificate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;
import org.springframework.http.HttpStatus;

/**
 * Data class created with purpose to simplify creation of response for specific error when process {@link GiftCertificate}.
 *
 * @author Tamerlan Hurbanov
 * @see ErrorResponse
 * @since 1.0
 */
@Value
public class GiftCertificateOperationErrorResponse implements ErrorResponse{

    @JsonIgnore
    HttpStatus httpStatus;

    int errorCode;

    String errorMessage;

    public GiftCertificateOperationErrorResponse(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = calculateErrorCode(httpStatus,30);
        this.errorMessage = errorMessage;
    }

}
