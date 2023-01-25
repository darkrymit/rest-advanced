package com.epam.esm.service;

import com.epam.esm.persistance.dao.support.page.Page;
import com.epam.esm.persistance.dao.support.page.Pageable;
import com.epam.esm.persistance.entity.User;
import com.epam.esm.service.exceptions.NoSuchUserByEmailException;
import com.epam.esm.service.exceptions.NoSuchUserException;


/**
 * Interface describes business logic for working with {@link User} entity.
 *
 * @author Tamerlan Hurbanov
 * @see User
 * @since 1.0
 */
public interface UserService {

  /**
   * Return {@link User} entities by {@link Pageable}
   *
   * @return {@link Page} of {@link User} by {@link Pageable}
   */
  Page<User> findAll(Pageable pageable);

  /**
   * Returns {@link User} entry by id
   *
   * @param id id of requested entity
   * @return entity with given id
   * @throws NoSuchUserException if there is no entity by given id
   */
  User getById(long id) throws NoSuchUserException;

  /**
   * Returns {@link User} entry by email
   *
   * @param email email of requested entity
   * @return entity with given email
   * @throws NoSuchUserException if there is no entity by given email
   */
  User getByEmail(String email) throws NoSuchUserByEmailException;
}
