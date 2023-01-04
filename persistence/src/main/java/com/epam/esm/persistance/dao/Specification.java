package com.epam.esm.persistance.dao;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.StreamSupport;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.lang.Nullable;

public interface Specification<T> extends Serializable {

  long serialVersionUID = 1L;

  static <T> Specification<T> emptySpecification() {
    return (root, query, builder) -> null;
  }

  static <T> Specification<T> where(@Nullable Specification<T> spec) {
    return spec == null //
        ? emptySpecification() //
        : spec;
  }

  static <T> Specification<T> not(@Nullable Specification<T> spec) {
    return spec == null //
        ? emptySpecification() //
        : (root, query, builder) -> builder.not(spec.toPredicate(root, query, builder));
  }

  default Specification<T> and(@Nullable Specification<T> other) {
    return SpecificationComposition.composed(this, other, CriteriaBuilder::and);
  }

  default Specification<T> or(@Nullable Specification<T> other) {
    return SpecificationComposition.composed(this, other, CriteriaBuilder::or);
  }

  static <T> Specification<T> allOf(Iterable<Specification<T>> specifications) {
    return StreamSupport.stream(specifications.spliterator(), false) //
        .reduce(emptySpecification(), Specification::and);
  }


  @SafeVarargs
  static <T> Specification<T> allOf(Specification<T>... specifications) {
    return allOf(Arrays.asList(specifications));
  }

  static <T> Specification<T> anyOf(Iterable<Specification<T>> specifications) {
    return StreamSupport.stream(specifications.spliterator(), false) //
        .reduce(emptySpecification(), Specification::and);
  }

  @SafeVarargs
  static <T> Specification<T> anyOf(Specification<T>... specifications) {
    return anyOf(Arrays.asList(specifications));
  }

  @Nullable
  Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);
}