package com.epam.esm.service.impl.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import com.epam.esm.persistance.dao.TagRepository;
import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.service.config.GiftCertificateUpdateHandlerConfig;
import com.epam.esm.service.payload.request.GiftCertificateUpdateRequest;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {GiftCertificateUpdateHandlerConfig.class})
class CompositeGiftCertificateUpdateHandlerTest {

  @Autowired
  CompositeGiftCertificateUpdateHandler handler;

  @MockBean
  TagRepository tagRepository;

  private static Answer1<Optional<Tag>, String> getTagFromListByName(List<Tag> tags) {
    return name -> tags.stream().filter(tag -> tag.getName().equals(name)).findFirst();
  }

  private GiftCertificate getGiftCertificate() {
    return new GiftCertificate(4L, "second", "Second grade", BigDecimal.valueOf(40), 2,
        Instant.now(), Instant.now(), getTags());
  }

  private List<Tag> getTags() {
    return List.of(new Tag(1L, "basic"), new Tag(2L, "advanced"));
  }

  private List<Tag> getTags1() {
    return List.of(new Tag(10L, "mbasic"), new Tag(20L, "madvanced"));
  }

  @Test
  void handleShouldReturnEntityWithSameIdWhenNormalUpdateRequest() {
    GiftCertificate giftCertificate = getGiftCertificate();
    Long id = giftCertificate.getId();
    GiftCertificateUpdateRequest updateRequest = new GiftCertificateUpdateRequest("test", null,
        BigDecimal.TEN, null, null);

    GiftCertificate result = handler.handle(updateRequest, giftCertificate);

    assertEquals(id, result.getId());
  }

  @Test
  void handleShouldReturnEntityWithNameEqualsToUpdateRequestWhenUpdateRequestWithName() {
    GiftCertificate giftCertificate = getGiftCertificate();
    GiftCertificateUpdateRequest updateRequest = new GiftCertificateUpdateRequest("test", null,
        BigDecimal.TEN, null, null);

    GiftCertificate result = handler.handle(updateRequest, giftCertificate);

    assertEquals(updateRequest.getName(), result.getName());
  }

  @Test
  void handleShouldReturnEntityWithPriceEqualsToUpdateRequestWhenUpdateRequestWithPrice() {
    GiftCertificate giftCertificate = getGiftCertificate();
    GiftCertificateUpdateRequest updateRequest = new GiftCertificateUpdateRequest("test", null,
        BigDecimal.TEN, null, null);

    GiftCertificate result = handler.handle(updateRequest, giftCertificate);

    assertEquals(updateRequest.getPrice(), result.getPrice());
  }

  @Test
  void handleShouldReturnEntityWithTagsEqualsToUpdateRequestWhenUpdateRequestWithTags() {
    GiftCertificate giftCertificate = getGiftCertificate();
    List<Tag> existTags = getTags1();
    Tag newTag = new Tag(404L, "test");
    List<Tag> allTags = new ArrayList<>(existTags);
    allTags.add(newTag);

    GiftCertificateUpdateRequest updateRequest = new GiftCertificateUpdateRequest(null, null, null,
        null, allTags.stream().map(Tag::getName).collect(Collectors.toList()));

    when(tagRepository.findByName(any())).thenAnswer(answer(getTagFromListByName(existTags)));
    when(tagRepository.save(argThat(t -> t.getName().equals(newTag.getName())))).thenReturn(newTag);

    GiftCertificate result = handler.handle(updateRequest, giftCertificate);

    assertEquals(allTags, result.getTags());
  }

  @Test
  void handleShouldReturnEntityWithSameDescriptionWhenUpdateRequestWithoutDescription() {
    GiftCertificate giftCertificate = getGiftCertificate();
    String description = giftCertificate.getDescription();
    GiftCertificateUpdateRequest updateRequest = new GiftCertificateUpdateRequest("test", null,
        BigDecimal.TEN, null, null);

    GiftCertificate result = handler.handle(updateRequest, giftCertificate);

    assertEquals(description, result.getDescription());
  }

}