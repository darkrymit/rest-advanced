package com.epam.esm.persistance.dao;

import com.epam.esm.persistance.dao.support.SimpleCrudRepository;
import com.epam.esm.persistance.dao.support.page.PagingRepository;
import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.persistance.projection.BestTag;
import java.util.Optional;

/**
 * Interface that extends {@link PagingRepository} and parametrizes according
 * to {@link Tag} entity.
 *
 * @author Tamerlan Hurbanov
 * @see SimpleCrudRepository
 * @see PagingRepository
 * @see Tag
 * @since 1.0
 */
public interface TagRepository extends PagingRepository<Tag, Long> {

  /**
   * Retrieves an entity by its name field.
   *
   * @param name must not be {@code null}.
   * @return the entity with the given name or {@link Optional#empty()} if none found.
   */
  Optional<Tag> findByName(String name);

  Optional<BestTag> findMostUsedTagForBestBuyer();
}
