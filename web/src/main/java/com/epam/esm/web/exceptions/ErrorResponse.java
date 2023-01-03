package com.epam.esm.web.exceptions;


import org.springframework.http.HttpStatus;

/**
 * Interface class created with purpose to unify all data required to form api error response.
 *
 * @author Tamerlan Hurbanov
 * @since 1.0
 */
public interface ErrorResponse {

  
  HttpStatus getHttpStatus();

  int getErrorCode();

  String getErrorMessage();

  default int calculateErrorCode(HttpStatus httpStatus, int resourceCode) {
    return httpStatus.value() * 1000 + resourceCode % 1000;
  }

}
