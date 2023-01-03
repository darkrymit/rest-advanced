package com.epam.esm.service.exceptions;

/**
 * Thrown by various methods to indicate that the {@link com.epam.esm.persistance.entity.GiftCertificate} entity being requested does not exist.
 * @author Tamerlan Hurbanov
 * @see RuntimeException
 * @since  1.0
 */
public class NoSuchGiftCertificateException extends RuntimeException {
  private final Long id;

  public NoSuchGiftCertificateException(Long id) {
    this.id = id;
  }

  public NoSuchGiftCertificateException(String s, Throwable cause, Long id) {
    super(s, cause);
    this.id = id;
  }

  public NoSuchGiftCertificateException(Throwable cause, Long id) {
    super(cause);
    this.id = id;
  }

  public NoSuchGiftCertificateException(String s, Long id) {
    super(s);
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
