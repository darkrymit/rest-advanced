package com.epam.esm.persistance.dao;

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
public interface GiftCertificateRepository extends SimpleCrudRepository<GiftCertificate, Long> {

  /**
   * Returns all instances of the type as {@link List} filtered by given
   * {@link GiftCertificateSearchParameters}.
   *
   * @param searchParameters parameters for filtering, sorting
   * @return all entities by given {@link GiftCertificateSearchParameters} as {@link List}.
   */
  List<GiftCertificate> findAllAsList(GiftCertificateSearchParameters searchParameters);


  List<GiftCertificate> findAllByNames(List<String> giftCertificates);
}
