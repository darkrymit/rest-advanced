package com.epam.esm.persistance.dao;

import com.epam.esm.persistance.dao.support.SimpleCrudRepository;
import com.epam.esm.persistance.dao.support.page.PagingRepository;
import com.epam.esm.persistance.dao.support.specification.JpaSpecificationExecutor;
import com.epam.esm.persistance.entity.GiftCertificate;
import java.util.List;

/**
 * Interface that extends {@link SimpleCrudRepository} and parametrizes according to
 * {@link GiftCertificate} entity.
 *
 * @author Tamerlan Hurbanov
 * @see SimpleCrudRepository
 * @see GiftCertificate
 * @since 1.0
 */
public interface GiftCertificateRepository extends PagingRepository<GiftCertificate, Long>,
    JpaSpecificationExecutor<GiftCertificate> {
  List<GiftCertificate> findAllByNames(List<String> giftCertificates);

}
