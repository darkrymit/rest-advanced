package com.epam.esm.service.impl.handler;

import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.service.payload.request.GiftCertificateUpdateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;

/**
 * Class created with purpose to group multiple {@link GiftCertificateUpdateHandler} to result
 * another complex handler
 *
 * @author Tamerlan Hurbanov
 * @see GiftCertificateUpdateHandler
 * @see GiftCertificateUpdateRequest
 * @since 1.0
 */
@RequiredArgsConstructor
public class CompositeGiftCertificateUpdateHandler implements GiftCertificateUpdateHandler {

  private final List<GiftCertificateUpdateHandler> handlers;

  /**
   * Handler that runs all of its registered handlers sequentially to produce update
   *
   * @param updateRequest   request containing update information
   * @param giftCertificate current saved entity
   * @return entity transformed according to logic of all handlers
   */
  @Override
  public GiftCertificate handle(GiftCertificateUpdateRequest updateRequest,
      GiftCertificate giftCertificate) {

    for (GiftCertificateUpdateHandler handler : handlers) {
      giftCertificate = handler.handle(updateRequest, giftCertificate);
    }

    return giftCertificate;
  }


}
