package com.epam.esm.persistance.dao;

import java.io.Serializable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class SpecificationComposition {

  private SpecificationComposition() {
  }

  public static <T> Specification<T> composed(@NonNull Specification<T> leftSide,
      @Nullable Specification<T> rightSide, Combiner combiner) {
    return (root, query, builder) -> {
      Predicate leftSidePredicate = toPredicate(leftSide, root, query, builder);
      Predicate rightSidePredicate = toPredicate(rightSide, root, query, builder);
      if (leftSidePredicate == null) {
        return rightSidePredicate;
      }
      if (rightSidePredicate == null) {
        return leftSidePredicate;
      }
      return combiner.combine(builder, leftSidePredicate, rightSidePredicate);
    };
  }

  @Nullable
  public static <T> Predicate toPredicate(@Nullable Specification<T> specification, Root<T> root,
      CriteriaQuery<?> query, CriteriaBuilder builder) {
    return specification == null ? null : specification.toPredicate(root, query, builder);
  }

  interface Combiner extends Serializable {

    Predicate combine(CriteriaBuilder builder, @Nullable Predicate leftSide, @Nullable Predicate rightSide);
  }
}