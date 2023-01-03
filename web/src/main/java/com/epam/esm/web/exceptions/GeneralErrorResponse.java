package com.epam.esm.web.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;
import org.springframework.http.HttpStatus;

/**
 * Data class created with purpose to simplify creation of response for general error.
 *
 * @author Tamerlan Hurbanov
 * @see ErrorResponse
 * @since 1.0
 */
@Value
public class GeneralErrorResponse implements ErrorResponse {

  @JsonIgnore
  HttpStatus httpStatus;

  int errorCode;

  String errorMessage;

  public GeneralErrorResponse(HttpStatus httpStatus, String errorMessage) {
    this.httpStatus = httpStatus;
    this.errorCode = calculateErrorCode(httpStatus,10);
    this.errorMessage = errorMessage;
  }

}
