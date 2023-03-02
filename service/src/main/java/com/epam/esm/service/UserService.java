package com.epam.esm.service;

import com.epam.esm.service.exceptions.NoSuchUserException;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Interface describes business logic for working with {@link UserInfo}.
 *
 * @author Tamerlan Hurbanov
 * @see UserInfo
 * @since 1.0
 */
public interface UserService {

  /**
   * Return {@link UserInfo} entities by {@link Pageable}
   *
   * @return {@link Page} of {@link UserInfo} by {@link Pageable}
   */
  Page<UserInfo> findAll(Pageable pageable);

  /**
   * Returns {@link UserInfo} entry by id
   *
   * @param id id of requested entity
   * @return entity with given id
   * @throws NoSuchUserException if there is no entity by given id
   */
  UserInfo getById(UUID id) throws NoSuchUserException;
}
