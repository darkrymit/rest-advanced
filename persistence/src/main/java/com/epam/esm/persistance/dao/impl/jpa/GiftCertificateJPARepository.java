package com.epam.esm.persistance.dao.impl.jpa;

import com.epam.esm.persistance.dao.GiftCertificateRepository;
import com.epam.esm.persistance.dao.GiftCertificateSearchParameters;
import com.epam.esm.persistance.dao.TagRepository;
import com.epam.esm.persistance.entity.GiftCertificate;
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
public class GiftCertificateJPARepository implements GiftCertificateRepository {

  @PersistenceContext
  protected EntityManager entityManager;

  private <S extends GiftCertificate> boolean isNew(S entity) {
    return entity.getId() == null || entity.getId() == 0L;
  }

  private Class<GiftCertificate> getEntityType() {
    return GiftCertificate.class;
  }

  @Override
  public <S extends GiftCertificate> S save(S entity) {
    if (isNew(entity)) {
      entityManager.persist(entity);
      return entity;
    } else {
      return entityManager.merge(entity);
    }
  }

  @Override
  public Optional<GiftCertificate> findById(Long id) {
    return Optional.ofNullable(entityManager.find(getEntityType(), id));
  }

  @Override
  public boolean existsById(Long id) {
    return findById(id).isPresent();
  }

  @Override
  public List<GiftCertificate> findAllAsList() {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<GiftCertificate> query = builder.createQuery(getEntityType());
    Root<GiftCertificate> root = query.from(getEntityType());
    query.select(root);
    return entityManager.createQuery(query).getResultList();
  }

  @Override
  public void delete(GiftCertificate entity) {
    if (isNew(entity)) {
      return;
    }
    GiftCertificate existing = this.entityManager.find(getEntityType(), entity.getId());
    if (existing != null) {
      entityManager.remove(
          this.entityManager.contains(entity) ? entity : this.entityManager.merge(entity));
    }
  }

  @Override
  public List<GiftCertificate> findAllAsList(GiftCertificateSearchParameters searchParameters) {
    return findAllAsList();
  }
}
