package com.epam.esm.service.exceptions;

/**
 * Thrown by various methods to indicate that the {@link com.epam.esm.persistance.entity.Tag} entity being requested does not exist.
 * @author Tamerlan Hurbanov
 * @see RuntimeException
 * @since  1.0
 */
public class NoSuchTagException extends RuntimeException {
  private final Long id;

  public NoSuchTagException(Long id) {
    this.id = id;
  }

  public NoSuchTagException(String s, Throwable cause, Long id) {
    super(s, cause);
    this.id = id;
  }

  public NoSuchTagException(Throwable cause, Long id) {
    super(cause);
    this.id = id;
  }

  public NoSuchTagException(String s, Long id) {
    super(s);
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
