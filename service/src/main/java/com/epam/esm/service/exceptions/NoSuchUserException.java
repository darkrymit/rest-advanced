package com.epam.esm.service.exceptions;

import java.util.UUID;

/**
 * Thrown by various methods to indicate that the {@link com.epam.esm.persistance.entity.User} entity being requested does not exist.
 * @author Tamerlan Hurbanov
 * @see RuntimeException
 * @since  1.0
 */
public class NoSuchUserException extends RuntimeException {
  private final UUID id;

  public NoSuchUserException(UUID id) {
    this.id = id;
  }

  public NoSuchUserException(String s, Throwable cause, UUID id) {
    super(s, cause);
    this.id = id;
  }

  public NoSuchUserException(Throwable cause, UUID id) {
    super(cause);
    this.id = id;
  }

  public NoSuchUserException(String s, UUID id) {
    super(s);
    this.id = id;
  }

  public UUID getId() {
    return id;
  }
}
