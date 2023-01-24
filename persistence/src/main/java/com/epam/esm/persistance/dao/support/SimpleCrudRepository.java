package com.epam.esm.persistance.dao.support;

import java.util.List;
import java.util.Optional;

/**
 * Interface {@link SimpleCrudRepository} describes CRUD operations for data access.
 *
 * @param <T> entity type parameter
 * @param <I> entity id type parameter
 * @author Tamerlan Hurbanov
 * @since 1.0
 */
public interface SimpleCrudRepository<T, I> {

  /**
   * Saves a given entity. Use the returned instance for further operations as the save operation
   * might have changed the entity instance completely.
   *
   * @param entity must not be {@code null}.
   * @return the saved entity will never be {@code null}.
   */
  <S extends T> S save(S entity);

  /**
   * Retrieves an entity by its id.
   *
   * @param id must not be {@code null}.
   * @return the entity with the given id or {@link Optional#empty()} if none found.
   */
  Optional<T> findById(I id);

  /**
   * Returns whether an entity with the given id exists.
   *
   * @param id must not be {@code null}.
   * @return {@code true} if an entity with the given id exists, {@code false} otherwise.
   */
  boolean existsById(I id);

  /**
   * Returns all instances of the type as {@link List}.
   *
   * @return all entities as {@link List}.
   */
  List<T> findAllAsList();

  /**
   * Deletes a given entity.
   *
   * @param entity must not be {@code null}.
   */
  void delete(T entity);
}
