package com.epam.esm.service;

import com.epam.esm.persistance.entity.User;
import com.epam.esm.service.exceptions.NoSuchUserException;
import java.util.List;


/**
 * Interface describes business logic for working with {@link User} entity.
 *
 * @author Tamerlan Hurbanov
 * @see User
 * @since 1.0
 */
public interface UserService {

  /**
   * Return all {@link User} entities
   *
   * @return {@link List} of all {@link User} that exist
   */
  List<User> findAll();

  /**
   * Returns {@link User} entry by id
   *
   * @param id id of requested entity
   * @return entity with given id
   * @throws NoSuchUserException if there is no entity by given id
   */
  User getById(long id) throws NoSuchUserException;
}
