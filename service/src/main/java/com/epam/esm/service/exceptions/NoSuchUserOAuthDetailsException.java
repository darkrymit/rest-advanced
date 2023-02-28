package com.epam.esm.service.exceptions;

import java.util.UUID;

/**
 * Thrown by various methods to indicate that the {@link com.epam.esm.service.UserOAuthDetails} being requested does not exist.
 * @author Tamerlan Hurbanov
 * @see RuntimeException
 * @since  1.0
 */
public class NoSuchUserOAuthDetailsException extends RuntimeException {
  private final UUID id;

  public NoSuchUserOAuthDetailsException(UUID id) {
    this.id = id;
  }

  public NoSuchUserOAuthDetailsException(String s, Throwable cause, UUID id) {
    super(s, cause);
    this.id = id;
  }

  public NoSuchUserOAuthDetailsException(Throwable cause, UUID id) {
    super(cause);
    this.id = id;
  }

  public NoSuchUserOAuthDetailsException(String s, UUID id) {
    super(s);
    this.id = id;
  }

  public UUID getId() {
    return id;
  }
}
