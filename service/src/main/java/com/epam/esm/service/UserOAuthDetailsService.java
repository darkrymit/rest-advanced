package com.epam.esm.service;

import com.epam.esm.service.exceptions.NoSuchUserOAuthDetailsException;
import java.util.UUID;


/**
 * Interface describes business logic for working with {@link UserOAuthDetails}.
 *
 * @author Tamerlan Hurbanov
 * @see UserOAuthDetails
 * @since 1.0
 */
public interface UserOAuthDetailsService {

  /**
   * Returns {@link UserOAuthDetails} entry by id
   *
   * @param id id of requested entity
   * @return entity with given id
   * @throws NoSuchUserOAuthDetailsException if there is no entity by given id
   */
  UserOAuthDetails getById(UUID id) throws NoSuchUserOAuthDetailsException;

}
