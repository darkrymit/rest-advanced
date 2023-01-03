package com.epam.esm.web.exceptions.handler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.epam.esm.web.exceptions.GeneralErrorResponse;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@RequiredArgsConstructor
public class GeneralControllerAdvice {

  private final ResourceBundleMessageSource messageSource;

  @ExceptionHandler(DataAccessException.class)
  public final ResponseEntity<GeneralErrorResponse> handleDataAccessException() {
    String message = toLocale("com.epam.esm.exception.DataAccessException.message");

    return getResponseEntity(new GeneralErrorResponse(INTERNAL_SERVER_ERROR, message));
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public final ResponseEntity<GeneralErrorResponse> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e) {
    String message = toLocale(
        "com.epam.esm.exception.HttpRequestMethodNotSupportedException.message",
        Arrays.asList(e.getMethod(), Arrays.toString(e.getSupportedMethods())));

    return getResponseEntity(new GeneralErrorResponse(METHOD_NOT_ALLOWED, message));
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<GeneralErrorResponse> handleNoFoundException() {
    String message = toLocale(
        "com.epam.esm.exception.NoHandlerFoundException.message");
    GeneralErrorResponse response = new GeneralErrorResponse(NOT_FOUND,message);
    return getResponseEntity(response);
  }

  @ExceptionHandler({RuntimeException.class, Exception.class})
  public final ResponseEntity<GeneralErrorResponse> handleUnmappedException(
      Exception e) {
    String message = toLocale("com.epam.esm.exception.unmapped-exception.message");

    return getResponseEntity(new GeneralErrorResponse(INTERNAL_SERVER_ERROR, message));
  }

  private ResponseEntity<GeneralErrorResponse> getResponseEntity(
      GeneralErrorResponse errorResponse) {
    return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
  }

  private String toLocale(String message) {
    return messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
  }

  private String toLocale(String message, List<Object> arguments) {
    return messageSource.getMessage(message, arguments.toArray(), LocaleContextHolder.getLocale());
  }
}
