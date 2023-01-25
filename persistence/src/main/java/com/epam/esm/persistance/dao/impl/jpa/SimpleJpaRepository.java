package com.epam.esm.persistance.dao.impl.jpa;

import com.epam.esm.persistance.dao.support.page.Page;
import com.epam.esm.persistance.dao.support.page.PageImpl;
import com.epam.esm.persistance.dao.support.page.Pageable;
import com.epam.esm.persistance.dao.support.page.PagingRepository;
import com.epam.esm.persistance.dao.support.specification.JpaSpecificationExecutor;
import com.epam.esm.persistance.dao.support.Sort;
import com.epam.esm.persistance.dao.support.specification.Specification;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@AllArgsConstructor
public abstract class SimpleJpaRepository<T, I> implements PagingRepository<T, I>,
    JpaSpecificationExecutor<T> {

  private final Class<T> entityClass;
  @PersistenceContext
  protected EntityManager entityManager;

  private static long executeCountQuery(TypedQuery<Long> query) {
    Assert.notNull(query, "TypedQuery must not be null!");
    return query.getResultList().stream().reduce(0L, Long::sum);
  }

  protected <S extends T> boolean isNew(S entity) {
    I id = getIdFromEntity(entity);

    if (id == null) {
      return true;
    }

    return id instanceof Number && ((Number) id).longValue() == 0L;
  }

  protected Class<T> getEntityType() {
    return entityClass;
  }

  protected abstract <S extends T> I getIdFromEntity(S entity);

  @Override
  public <S extends T> S save(S entity) {
    if (isNew(entity)) {
      entityManager.persist(entity);
      return entity;
    } else {
      return entityManager.merge(entity);
    }
  }

  @Override
  public Optional<T> findById(I id) {
    return Optional.ofNullable(entityManager.find(getEntityType(), id));
  }

  @Override
  public boolean existsById(I id) {
    return findById(id).isPresent();
  }

  @Override
  public List<T> findAllAsList() {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<T> query = builder.createQuery(getEntityType());
    Root<T> root = query.from(getEntityType());
    query.select(root);
    return entityManager.createQuery(query).getResultList();
  }

  @Override
  public void delete(T entity) {
    if (isNew(entity)) {
      return;
    }
    T existing = this.entityManager.find(getEntityType(), getIdFromEntity(entity));
    if (existing != null) {
      entityManager.remove(
          this.entityManager.contains(entity) ? entity : this.entityManager.merge(entity));
    }
  }

  @Override
  public Optional<T> findOne(Specification<T> spec) {
    try {
      return Optional.of(getQuery(spec, Sort.unsorted()).setMaxResults(2).getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<T> findAll(Specification<T> spec) {
    return getQuery(spec, Sort.unsorted()).getResultList();
  }

  @Override
  public List<T> findAll(Specification<T> spec, Sort sort) {
    return getQuery(spec, sort).getResultList();
  }

  @Override
  public Page<T> findAll(Specification<T> spec, Pageable pageable) {
    TypedQuery<T> query = this.getQuery(spec, pageable);
    return readPage(query, getEntityType(), pageable, spec);

  }

  protected <S extends T> Page<S> readPage(TypedQuery<S> query, Class<S> domainClass,
      Pageable pageable, @Nullable Specification<S> spec) {

    if (pageable.isUnpaged()) {
      return new PageImpl<>(query.getResultList());
    }

    query.setFirstResult((int) pageable.getOffset());
    query.setMaxResults(pageable.getPageSize());

    List<S> content = query.getResultList();
    LongSupplier totalSupplier = () -> executeCountQuery(this.getCountQuery(spec, domainClass));

    if (pageable.getOffset() != 0L && !content.isEmpty() && pageable.getPageSize() > content.size() ) {
      return new PageImpl<>(content, pageable, pageable.getOffset() + content.size());
    }

    if(pageable.getOffset() == 0L && pageable.getPageSize() > content.size()){
      return new PageImpl<>(content, pageable, content.size());
    }

    return new PageImpl<>(content, pageable, totalSupplier.getAsLong());

  }

  @Override
  public Page<T> findAll(Pageable pageable) {
    return pageable.isUnpaged() ? new PageImpl<>(this.findAllAsList())
        : this.findAll(null, pageable);
  }

  @Override
  public long count(Specification<T> spec) {
    return executeCountQuery(getCountQuery(spec, getEntityType()));
  }

  @Override
  public boolean exists(Specification<T> spec) {
    throw new UnsupportedOperationException();
  }

  @Override
  public long delete(Specification<T> spec) {
    throw new UnsupportedOperationException();
  }

  protected TypedQuery<T> getQuery(@Nullable Specification<T> spec, Sort sort) {
    return getQuery(spec, getEntityType(), sort);
  }

  protected TypedQuery<T> getQuery(Specification<T> spec, Pageable pageable) {
    Sort sort = pageable.isPaged() ? pageable.getSort() : Sort.unsorted();
    return getQuery(spec, sort);
  }

  protected <C> TypedQuery<C> getNamedQuery(String name, Class<C> resultClass) {
    return entityManager.createNamedQuery(name, resultClass);
  }

  protected <S extends T> TypedQuery<S> getQuery(@Nullable Specification<S> spec,
      Class<S> domainClass, Sort sort) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<S> query = builder.createQuery(domainClass);

    Root<S> root = this.applySpecificationToCriteria(spec, domainClass, query);
    query.select(root);

    if (sort.isSorted()) {
      query.orderBy(toOrders(sort, root, builder));
    }

    return entityManager.createQuery(query);
  }

  protected <S extends T> TypedQuery<Long> getCountQuery(@Nullable Specification<S> spec,
      Class<S> domainClass) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> query = builder.createQuery(Long.class);

    Root<S> root = this.applySpecificationToCriteria(spec, domainClass, query);
    if (query.isDistinct()) {
      query.select(builder.countDistinct(root));
    } else {
      query.select(builder.count(root));
    }

    return entityManager.createQuery(query);
  }

  private List<Order> toOrders(Sort sort, From<?, ?> from, CriteriaBuilder criteriaBuilder) {
    if (sort.isUnsorted()) {
      return Collections.emptyList();
    }
    Assert.notNull(from, "From must not be null");
    Assert.notNull(criteriaBuilder, "CriteriaBuilder must not be null");
    return sort.stream().map(order -> toJpaOrder(order, from, criteriaBuilder))
        .collect(Collectors.toList());

  }

  private Order toJpaOrder(Sort.Order order, From<?, ?> from, CriteriaBuilder criteriaBuilder) {
    Expression<?> expression = from.get(order.getProperty());
    return order.isAscending() ? criteriaBuilder.asc(expression) : criteriaBuilder.desc(expression);
  }

  private <S, U extends T> Root<U> applySpecificationToCriteria(
      @Nullable Specification<U> specification, Class<U> domainClass, CriteriaQuery<S> query) {
    Assert.notNull(domainClass, "Domain class must not be null");
    Assert.notNull(query, "CriteriaQuery must not be null");
    Root<U> root = query.from(domainClass);

    if (specification == null) {
      return root;
    }

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    Predicate predicate = specification.toPredicate(root, query, builder);
    if (predicate != null) {
      query.where(predicate);
    }

    return root;

  }
}
