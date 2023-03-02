package com.epam.esm.persistance.dao;

import com.epam.esm.persistance.entity.GiftCertificate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Interface that extends {@link JpaRepository} and parametrizes according to
 * {@link GiftCertificate} entity.
 *
 * @author Tamerlan Hurbanov
 * @see JpaRepository
 * @see GiftCertificate
 * @since 1.0
 */
@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long>,
    JpaSpecificationExecutor<GiftCertificate> {

  @Query(value = "SELECT c FROM GiftCertificate c where c.name in :giftCertificates")
  List<GiftCertificate> findAllByNames(List<String> giftCertificates);

}
