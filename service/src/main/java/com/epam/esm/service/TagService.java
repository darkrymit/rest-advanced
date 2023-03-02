package com.epam.esm.service;

import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.persistance.projection.BestTag;
import com.epam.esm.service.exceptions.NoSuchTagException;
import com.epam.esm.service.payload.request.TagCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;


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
   * Return all {@link Tag} entities by pageable request
   *
   * @param pageable request for paging
   * @return {@link Page} of all {@link Tag} that exist by request
   */
  Page<Tag> findAll(Pageable pageable);

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
  @PreAuthorize("hasAnyRole('Administrator')")
  void deleteById(long id);

  /**
   * Creates {@link Tag} entry by given {@link TagCreateRequest}.
   *
   * @param request request containing all required data to create entity
   * @return entity created by given request
   */
  Tag create(TagCreateRequest request);

  @PreAuthorize("hasAnyRole('Administrator')")
  BestTag getMostUsedTagForBestBuyer();
}
