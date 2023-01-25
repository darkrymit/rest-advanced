package com.epam.esm.persistance.dao.impl.jpa;

import com.epam.esm.persistance.dao.GiftCertificateRepository;
import com.epam.esm.persistance.dao.TagRepository;
import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.persistance.entity.GiftCertificate_;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Implementation of {@link GiftCertificateJPARepository} to work with JPA
 *
 * @author Tamerlan Hurbanov
 * @see TagRepository
 * @see EntityManager
 * @since 1.0
 */
@Repository
public class GiftCertificateJPARepository extends SimpleJpaRepository<GiftCertificate,Long> implements GiftCertificateRepository {

  @Autowired
  public GiftCertificateJPARepository() {
    super(GiftCertificate.class);
  }

  public GiftCertificateJPARepository(EntityManager entityManager) {
    super(GiftCertificate.class, entityManager);
  }

  @Override
  protected <S extends GiftCertificate> Long getIdFromEntity(S entity) {
    return entity.getId();
  }

  @Override
  public List<GiftCertificate> findAllByNames(List<String> giftCertificates) {
    return findAll((root, query, criteriaBuilder) -> root.get(GiftCertificate_.name).in(giftCertificates));
  }
}
