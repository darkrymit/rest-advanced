package com.epam.esm.service.config;

import com.epam.esm.service.impl.handler.CompositeGiftCertificateUpdateHandler;
import com.epam.esm.service.impl.handler.GiftCertificateUpdateHandler;
import com.epam.esm.service.impl.handler.PredicateGiftCertificateUpdateHandler;
import com.epam.esm.service.payload.request.GiftCertificateUpdateRequest;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GiftCertificateUpdateHandlerTestConfig {

  @Bean
  CompositeGiftCertificateUpdateHandler compositeGiftCertificateUpdateHandler(List<GiftCertificateUpdateHandler> handlers){
    return new CompositeGiftCertificateUpdateHandler(handlers);
  }

  @Bean
  GiftCertificateUpdateHandler giftCertificateUpdateNameHandler() {
    return new PredicateGiftCertificateUpdateHandler(GiftCertificateUpdateRequest::isNamePresent,
        (request, giftCertificate) -> {
          giftCertificate.setName(request.getName());
          return giftCertificate;
        });
  }

  @Bean
  GiftCertificateUpdateHandler giftCertificateUpdateDescriptionHandler() {
    return new PredicateGiftCertificateUpdateHandler(
        GiftCertificateUpdateRequest::isDescriptionPresent, (request, giftCertificate) -> {
      giftCertificate.setDescription(request.getDescription());
      return giftCertificate;
    });
  }

  @Bean
  GiftCertificateUpdateHandler giftCertificateUpdateDurationHandler() {
    return new PredicateGiftCertificateUpdateHandler(
        GiftCertificateUpdateRequest::isDurationPresent, (request, giftCertificate) -> {
      giftCertificate.setDuration(request.getDuration());
      return giftCertificate;
    });
  }

  @Bean
  GiftCertificateUpdateHandler giftCertificateUpdatePriceHandler() {
    return new PredicateGiftCertificateUpdateHandler(GiftCertificateUpdateRequest::isPricePresent,
        (request, giftCertificate) -> {
          giftCertificate.setPrice(request.getPrice());
          return giftCertificate;
        });
  }
}
