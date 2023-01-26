package com.epam.esm.service;


import com.epam.esm.persistance.dao.support.page.Page;
import com.epam.esm.persistance.dao.support.page.Pageable;
import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.service.exceptions.NoSuchGiftCertificateException;
import com.epam.esm.service.payload.request.GiftCertificateCreateRequest;
import com.epam.esm.service.payload.request.GiftCertificatePriceUpdateRequest;
import com.epam.esm.service.payload.request.GiftCertificateSearchRequest;
import com.epam.esm.service.payload.request.GiftCertificateUpdateRequest;

/**
 * Interface describes business logic for working with {@link GiftCertificate} entity.
 *
 * @author Tamerlan Hurbanov
 * @see GiftCertificate
 * @see GiftCertificateUpdateRequest
 * @see GiftCertificateCreateRequest
 * @since 1.0
 */
public interface GiftCertificateService {

  /**
   * Return all {@link GiftCertificate} entities according to {@link GiftCertificateSearchRequest}
   * and {@link Pageable}
   *
   * @param searchRequest request for filtering
   * @param pageable      request for sorting and paging
   * @return {@link Page} of {@link GiftCertificate} filtered and sorted according to
   * {@link GiftCertificateSearchRequest} and {@link Pageable}
   */
  Page<GiftCertificate> findAll(GiftCertificateSearchRequest searchRequest, Pageable pageable);

  /**
   * Returns {@link GiftCertificate} entry by id
   *
   * @param id id of requested entity
   * @return entity with given id
   * @throws NoSuchGiftCertificateException if there is no entity by given id
   */
  GiftCertificate getById(long id) throws NoSuchGiftCertificateException;

  /**
   * Deletes {@link GiftCertificate} entry by id
   *
   * @param id id of requested entity
   * @throws NoSuchGiftCertificateException if there is no entity by given id
   */
  void deleteById(long id) throws NoSuchGiftCertificateException;

  /**
   * Creates {@link GiftCertificate} entry by given {@link GiftCertificateCreateRequest}. Also
   * creates {@link Tag} if requested tags is not exist.
   *
   * @param request request containing all required data to create entity
   * @return entity created by given request
   */
  GiftCertificate create(GiftCertificateCreateRequest request);

  /**
   * Modify {@link GiftCertificate} entry by given {@link GiftCertificateUpdateRequest}. Also
   * creates {@link Tag} if requested tags is not exist.
   *
   * @param id            id of requested entity
   * @param updateRequest request containing all required data to update entity
   * @return entity updated by given request
   * @throws NoSuchGiftCertificateException if there is no entity by given id
   */
  GiftCertificate update(long id, GiftCertificateUpdateRequest updateRequest)
      throws NoSuchGiftCertificateException;

  /**
   * Modify {@link GiftCertificate#getPrice()} by given {@link GiftCertificatePriceUpdateRequest}
   *
   * @param id            id of requested entity
   * @param updateRequest request containing all required data to update entity
   * @return entity updated by given request
   * @throws NoSuchGiftCertificateException if there is no entity by given id
   */
  GiftCertificate update(long id, GiftCertificatePriceUpdateRequest updateRequest);
}
