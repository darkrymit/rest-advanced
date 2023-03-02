package com.epam.esm.persistance.dao;

import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.persistance.projection.BestTag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Interface that extends {@link JpaRepository} and parametrizes according
 * to {@link Tag} entity.
 *
 * @author Tamerlan Hurbanov
 * @see JpaRepository
 * @see Tag
 * @since 1.0
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

  /**
   * Retrieves an entity by its name field.
   *
   * @param name must not be {@code null}.
   * @return the entity with the given name or {@link Optional#empty()} if none found.
   */
  Optional<Tag> findByName(String name);

  @Query(nativeQuery = true)
  Optional<BestTag> findMostUsedTagForBestBuyer();
}
