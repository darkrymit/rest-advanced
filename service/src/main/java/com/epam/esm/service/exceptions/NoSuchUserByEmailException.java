package com.epam.esm.service.exceptions;

/**
 * Thrown by various methods to indicate that the {@link com.epam.esm.persistance.entity.User} entity being requested by email does not exist.
 * @author Tamerlan Hurbanov
 * @see RuntimeException
 * @since  1.0
 */
public class NoSuchUserByEmailException extends RuntimeException {
  private final String email;

  public NoSuchUserByEmailException(String id) {
    this.email = id;
  }

  public NoSuchUserByEmailException(String s, Throwable cause, String email) {
    super(s, cause);
    this.email = email;
  }

  public NoSuchUserByEmailException(Throwable cause, String email) {
    super(cause);
    this.email = email;
  }

  public NoSuchUserByEmailException(String s, String email) {
    super(s);
    this.email = email;
  }

  public String getId() {
    return email;
  }
}
