package com.epam.esm.web.exceptions.handler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.epam.esm.service.exceptions.NoSuchTagException;
import com.epam.esm.web.controllers.TagController;
import com.epam.esm.web.exceptions.TagOperationErrorResponse;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {TagController.class})
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TagControllerAdvice {

  private final ResourceBundleMessageSource messageSource;

  @ExceptionHandler(NoSuchTagException.class)
  public final ResponseEntity<TagOperationErrorResponse> handleDataAccessException(
      NoSuchTagException e) {
    String message = toLocale("com.epam.esm.exception.NoSuchTagException.message",
        Collections.singletonList(e.getId()));

    return getResponseEntity(new TagOperationErrorResponse(NOT_FOUND, message));
  }

  private ResponseEntity<TagOperationErrorResponse> getResponseEntity(
      TagOperationErrorResponse errorResponse) {
    return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
  }

  private String toLocale(String message, List<Object> arguments) {
    return messageSource.getMessage(message, arguments.toArray(), LocaleContextHolder.getLocale());
  }

}
