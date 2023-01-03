package com.epam.esm.service.impl.handler;

import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.service.payload.request.GiftCertificateUpdateRequest;

/**
 * Functional interface created with purpose to abstract update logic of {@link GiftCertificate}.
 * Highly recommended to use atomic update of field with multiple handlers instead of whole update
 * of entity with one handler.
 *
 * @author Tamerlan Hurbanov
 * @see FunctionalInterface
 * @see GiftCertificateUpdateRequest
 * @since 1.0
 */
@FunctionalInterface
public interface GiftCertificateUpdateHandler {

  /**
   * Method for handle update of {@link GiftCertificate} entity. Highly recommended to use atomic
   * update of field with multiple handlers instead of whole update of entity with one handler.
   *
   * @param updateRequest   request containing update information
   * @param giftCertificate current saved entity
   * @return entity transformed according to logic
   */
  GiftCertificate handle(GiftCertificateUpdateRequest updateRequest,
      GiftCertificate giftCertificate);

}

