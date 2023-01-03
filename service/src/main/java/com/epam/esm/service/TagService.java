package com.epam.esm.service;

import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.service.exceptions.NoSuchTagException;
import com.epam.esm.service.payload.request.TagCreateRequest;
import java.util.List;


/**
 * Interface describes business logic for working with {@link Tag} entity.
 *
 * @author Tamerlan Hurbanov
 * @see Tag
 * @see TagCreateRequest
 * @since 1.0
 */
public interface TagService {

  /**
   * Return all {@link Tag} entities
   *
   * @return {@link List} of all {@link Tag} that exist
   */
  List<Tag> findAll();

  /**
   * Returns {@link Tag} entry by id
   *
   * @param id id of requested entity
   * @return entity with given id
   * @throws NoSuchTagException if there is no entity by given id
   */
  Tag getById(long id) throws NoSuchTagException;

  /**
   * Deletes {@link Tag} entry by id
   *
   * @param id id of requested entity
   * @throws NoSuchTagException if there is no entity by given id
   */
  void deleteById(long id);

  /**
   * Creates {@link Tag} entry by given {@link TagCreateRequest}.
   *
   * @param request request containing all required data to create entity
   * @return entity created by given request
   */
  Tag create(TagCreateRequest request);
}
