package com.epam.esm.service.config;

import com.epam.esm.persistance.dao.TagRepository;
import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.service.impl.handler.CompositeGiftCertificateUpdateHandler;
import com.epam.esm.service.impl.handler.GiftCertificateUpdateHandler;
import com.epam.esm.service.impl.handler.PredicateGiftCertificateUpdateHandler;
import com.epam.esm.service.payload.request.GiftCertificateUpdateRequest;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GiftCertificateUpdateHandlerConfig {

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

  @Bean
  GiftCertificateUpdateHandler giftCertificateUpdateTagsHandler(TagRepository tagRepository) {
    return new PredicateGiftCertificateUpdateHandler(GiftCertificateUpdateRequest::isTagsPresent,
        (request, giftCertificate) -> {
          List<Tag> tags = request.getTags().stream().map(
                  tagName -> tagRepository.findByName(tagName)
                      .orElseGet(() -> tagRepository.save(new Tag(tagName))))
              .collect(Collectors.toList());
          giftCertificate.setTags(tags);
          return giftCertificate;
        });
  }
}
