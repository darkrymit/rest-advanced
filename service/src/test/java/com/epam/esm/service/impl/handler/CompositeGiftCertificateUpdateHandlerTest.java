package com.epam.esm.service.impl.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.service.config.GiftCertificateUpdateHandlerTestConfig;
import com.epam.esm.service.payload.request.GiftCertificateUpdateRequest;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GiftCertificateUpdateHandlerTestConfig.class})
class CompositeGiftCertificateUpdateHandlerTest {

  @Autowired
  CompositeGiftCertificateUpdateHandler handler;

  private GiftCertificate getGiftCertificate() {
    return new GiftCertificate(4L, "second", "Second grade", BigDecimal.valueOf(40), 2,
        Instant.now(), Instant.now(), getTags());
  }

  private List<Tag> getTags() {
    return List.of(new Tag(1L, "basic"), new Tag(2L, "advanced"));
  }

  @Test
  void handleShouldReturnEntityWithSameIdWhenNormalUpdateRequest() {
    GiftCertificate giftCertificate = getGiftCertificate();
    Long id = giftCertificate.getId();
    GiftCertificateUpdateRequest updateRequest = new GiftCertificateUpdateRequest("test", null,
        BigDecimal.TEN, null, null);

    GiftCertificate result = handler.handle(updateRequest, giftCertificate);

    assertEquals(id,result.getId());
  }
  @Test
  void handleShouldReturnEntityWithNameEqualsToUpdateRequestWhenUpdateRequestWithName() {
    GiftCertificate giftCertificate = getGiftCertificate();
    GiftCertificateUpdateRequest updateRequest = new GiftCertificateUpdateRequest("test", null,
        BigDecimal.TEN, null, null);

    GiftCertificate result = handler.handle(updateRequest, giftCertificate);

    assertEquals(updateRequest.getName(),result.getName());
  }

  @Test
  void handleShouldReturnEntityWithPriceEqualsToUpdateRequestWhenUpdateRequestWithPrice() {
    GiftCertificate giftCertificate = getGiftCertificate();
    GiftCertificateUpdateRequest updateRequest = new GiftCertificateUpdateRequest("test", null,
        BigDecimal.TEN, null, null);

    GiftCertificate result = handler.handle(updateRequest, giftCertificate);

    assertEquals(updateRequest.getPrice(),result.getPrice());
  }

  @Test
  void handleShouldReturnEntityWithSameDescriptionWhenUpdateRequestWithoutDescription() {
    GiftCertificate giftCertificate = getGiftCertificate();
    String description = giftCertificate.getDescription();
    GiftCertificateUpdateRequest updateRequest = new GiftCertificateUpdateRequest("test", null,
        BigDecimal.TEN, null, null);

    GiftCertificate result = handler.handle(updateRequest, giftCertificate);

    assertEquals(description,result.getDescription());
  }

}