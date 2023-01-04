package com.epam.esm.persistance.dao.impl.jpa;

import com.epam.esm.persistance.dao.TagRepository;
import com.epam.esm.persistance.entity.Tag;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Implementation of {@link TagRepository} to work with JPA
 *
 * @author Tamerlan Hurbanov
 * @see TagRepository
 * @see EntityManager
 * @since 1.0
 */
@Repository
@RequiredArgsConstructor
@AllArgsConstructor
public class TagJPARepository implements TagRepository {

  @PersistenceContext
  protected EntityManager entityManager;

  private <S extends Tag> boolean isNew(S entity) {
    return entity.getId() == null || entity.getId() == 0L;
  }

  private Class<Tag> getEntityType() {
    return Tag.class;
  }

  @Override
  public <S extends Tag> S save(S entity) {
    if (isNew(entity)) {
      entityManager.persist(entity);
      return entity;
    } else {
      return entityManager.merge(entity);
    }
  }

  @Override
  public Optional<Tag> findById(Long id) {
    return Optional.ofNullable(entityManager.find(getEntityType(), id));
  }

  @Override
  public boolean existsById(Long id) {
    return findById(id).isPresent();
  }

  @Override
  public List<Tag> findAllAsList() {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tag> query = builder.createQuery(getEntityType());
    Root<Tag> root = query.from(getEntityType());
    query.select(root);
    return entityManager.createQuery(query).getResultList();
  }

  @Override
  public void delete(Tag entity) {
    if (isNew(entity)) {
      return;
    }
    Tag existing = this.entityManager.find(getEntityType(), entity.getId());
    if (existing != null) {
      entityManager.remove(
          this.entityManager.contains(entity) ? entity : this.entityManager.merge(entity));
    }
  }

  @Override
  public Optional<Tag> findByName(String name) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tag> query = criteriaBuilder.createQuery(getEntityType());
    Root<Tag> root = query.from(getEntityType());
    query.select(root).where(criteriaBuilder.equal(root.get("name"), name));
    return entityManager.createQuery(query).getResultStream().findFirst();
  }
}
