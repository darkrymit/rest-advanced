package com.epam.esm.persistance.dao.support.page;

import com.epam.esm.persistance.dao.support.SimpleCrudRepository;

/**
 * Interface {@link PagingRepository} extends {@link SimpleCrudRepository} and add paging method.
 *
 * @param <T> entity type parameter
 * @param <I> entity id type parameter
 * @author Tamerlan Hurbanov
 * @see SimpleCrudRepository
 * @since 1.0
 */
public interface PagingRepository<T, I> extends SimpleCrudRepository<T,I>{
  Page<T> findAll(Pageable pageable);
}