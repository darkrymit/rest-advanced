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
    super(String.format("No such UserOAuthDetails by id:%s", id));
    this.id = id;
  }

  public UUID getId() {
    return id;
  }
}
