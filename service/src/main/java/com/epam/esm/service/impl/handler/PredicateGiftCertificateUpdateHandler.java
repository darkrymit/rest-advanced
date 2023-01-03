package com.epam.esm.service.impl.handler;

import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.service.payload.request.GiftCertificateUpdateRequest;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;

/**
 * Class created with purpose to simplify simple predicate update logic of {@link GiftCertificate}
 * entity.
 *
 * @author Tamerlan Hurbanov
 * @see GiftCertificateUpdateHandler
 * @see Predicate
 * @see GiftCertificateUpdateRequest
 * @since 1.0
 */
@RequiredArgsConstructor
public class PredicateGiftCertificateUpdateHandler implements GiftCertificateUpdateHandler {

  private final Predicate<GiftCertificateUpdateRequest> predicate;
  private final GiftCertificateUpdateHandler handler;

  /**
   * Method for handle update of {@link GiftCertificate} entity that run update logic only if result
   * of test {@link Predicate} is true
   *
   * @param updateRequest   request containing update information
   * @param giftCertificate current saved entity
   * @return same {@link GiftCertificate} entity if {@link Predicate} false and updated according to
   * handler {@link GiftCertificate} entity otherwise
   */
  @Override
  public GiftCertificate handle(GiftCertificateUpdateRequest updateRequest,
      GiftCertificate giftCertificate) {
    if (predicate.test(updateRequest)) {
      return handler.handle(updateRequest, giftCertificate);
    }
    return giftCertificate;
  }

}
