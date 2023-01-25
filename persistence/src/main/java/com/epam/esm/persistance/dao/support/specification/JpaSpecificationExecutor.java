package com.epam.esm.persistance.dao.support.specification;

import com.epam.esm.persistance.dao.support.Sort;
import com.epam.esm.persistance.dao.support.page.Page;
import com.epam.esm.persistance.dao.support.page.Pageable;
import java.util.List;
import java.util.Optional;
import org.springframework.lang.Nullable;

public interface JpaSpecificationExecutor<T> {
  Optional<T> findOne(@Nullable Specification<T> spec);

  List<T> findAll(@Nullable Specification<T> spec);

  Page<T> findAll(@Nullable Specification<T> spec, Pageable pageable);

  List<T> findAll(@Nullable Specification<T> spec, Sort sort);

  long count(@Nullable Specification<T> spec);

  boolean exists(Specification<T> spec);

  long delete(Specification<T> spec);

}