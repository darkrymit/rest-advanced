package com.epam.esm.web.exceptions.handler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.epam.esm.service.exceptions.NoSuchGiftCertificateException;
import com.epam.esm.web.controllers.GiftCertificateController;
import com.epam.esm.web.exceptions.GiftCertificateOperationErrorResponse;
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

@RestControllerAdvice(assignableTypes = {GiftCertificateController.class})
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GiftCertificateControllerAdvice {

  private final ResourceBundleMessageSource messageSource;

  @ExceptionHandler(NoSuchGiftCertificateException.class)
  public final ResponseEntity<GiftCertificateOperationErrorResponse> handleDataAccessException(
      NoSuchGiftCertificateException e) {
    String message = toLocale("com.epam.esm.exception.NoSuchGiftCertificateException.message",
        Collections.singletonList(e.getId()));

    return getResponseEntity(new GiftCertificateOperationErrorResponse(NOT_FOUND, message));
  }

  private ResponseEntity<GiftCertificateOperationErrorResponse> getResponseEntity(
      GiftCertificateOperationErrorResponse errorResponse) {
    return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
  }

  private String toLocale(String message, List<Object> arguments) {
    return messageSource.getMessage(message, arguments.toArray(), LocaleContextHolder.getLocale());
  }
}
