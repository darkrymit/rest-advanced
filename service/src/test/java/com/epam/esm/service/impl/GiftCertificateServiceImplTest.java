package com.epam.esm.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.esm.persistance.dao.GiftCertificateRepository;
import com.epam.esm.persistance.dao.TagRepository;
import com.epam.esm.persistance.dao.support.page.PageImpl;
import com.epam.esm.persistance.dao.support.page.Pageable;
import com.epam.esm.persistance.dao.support.specification.Specification;
import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.service.impl.handler.GiftCertificateUpdateHandler;
import com.epam.esm.service.payload.request.GiftCertificateCreateRequest;
import com.epam.esm.service.payload.request.GiftCertificatePriceUpdateRequest;
import com.epam.esm.service.payload.request.GiftCertificateSearchRequest;
import com.epam.esm.service.payload.request.GiftCertificateUpdateRequest;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer1;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

  @Mock
  TagRepository tagRepository;

  @Mock
  GiftCertificateRepository giftCertificateRepository;

  @Mock
  GiftCertificateUpdateHandler giftCertificateUpdateHandler;

  @InjectMocks
  GiftCertificateServiceImpl giftCertificateService;

  private static Answer1<GiftCertificate, GiftCertificate> getFakeSave(Long id) {
    return giftCertificate -> {
      giftCertificate.setId(id);
      return giftCertificate;
    };
  }

  private static GiftCertificateCreateRequest getCreateRequest(GiftCertificate giftCertificate) {
    return new GiftCertificateCreateRequest(giftCertificate.getName(), giftCertificate.getName(),
        giftCertificate.getPrice(), giftCertificate.getDuration(),
        getTagsAsStringList(giftCertificate.getTags()));
  }

  private static List<String> getTagsAsStringList(List<Tag> tags) {
    return tags.stream().map(Tag::getName).collect(Collectors.toList());
  }

  private List<GiftCertificate> getGiftCertificates() {
    return List.of(
        new GiftCertificate(1L, "new", "New certificate", BigDecimal.TEN, 5, ZonedDateTime.now(),
            ZonedDateTime.now(), getTags()),
        new GiftCertificate(2L, "standard", "Real good certificate", BigDecimal.valueOf(20), 15,
            ZonedDateTime.now(), ZonedDateTime.now(), getTags().subList(0, 1)));
  }

  private GiftCertificate getGiftCertificate() {
    return new GiftCertificate(4L, "second", "Second grade", BigDecimal.valueOf(40), 2,
        ZonedDateTime.now(), ZonedDateTime.now(), getTags());
  }

  private List<Tag> getTags() {
    return List.of(new Tag(1L, "basic"), new Tag(2L, "advanced"));
  }

  @Test
  void findAllShouldReturnNotEmptyContentWhenEntriesExists() {
    List<GiftCertificate> giftCertificates = getGiftCertificates();
    GiftCertificateSearchRequest searchRequest = new GiftCertificateSearchRequest(List.of("tag"),
        null);
    Pageable pageable = Pageable.unpaged();

    when(giftCertificateRepository.findAll(
        argThat(spec -> !Specification.emptySpecification().equals(spec)),
        (Pageable) argThat(arg -> arg.equals(pageable)))).thenReturn(
        new PageImpl<>(giftCertificates));

    assertFalse(giftCertificateService.findAll(searchRequest, pageable).getContent().isEmpty());
  }

  @Test
  void getByIdShouldReturnEntryWhenByExistingId() {
    GiftCertificate giftCertificate = getGiftCertificate();

    when(giftCertificateRepository.findById(giftCertificate.getId())).thenReturn(
        Optional.of(giftCertificate));

    assertEquals(giftCertificate.getName(),
        giftCertificateService.getById(giftCertificate.getId()).getName());
  }

  @Test
  void deleteByIdShouldDeleteMetaWhenByExistingId() {
    GiftCertificate giftCertificate = getGiftCertificate();

    when(giftCertificateRepository.findById(giftCertificate.getId())).thenReturn(
        Optional.of(giftCertificate));
    doNothing().when(giftCertificateRepository).delete(giftCertificate);

    giftCertificateService.deleteById(giftCertificate.getId());

    verify(giftCertificateRepository, times(1)).delete(giftCertificate);
  }

  @Test
  void createShouldReturnGiftCertificateWithNameMatchingToRequestWhenNormalRequest() {
    GiftCertificate giftCertificate = getGiftCertificate();
    GiftCertificateCreateRequest createRequest = getCreateRequest(giftCertificate);

    for (Tag tag : giftCertificate.getTags()) {
      when(tagRepository.findByName(tag.getName())).thenReturn(Optional.of(tag));
    }
    when(giftCertificateRepository.save(any())).thenAnswer(
        answer(getFakeSave(giftCertificate.getId())));

    assertEquals(giftCertificate.getName(), giftCertificateService.create(createRequest).getName());
  }

  @Test
  void updateShouldReturnGiftCertificateWithNameMatchingToRequestWhenNormalRequest() {
    GiftCertificate giftCertificateOriginal = getGiftCertificate();
    GiftCertificateUpdateRequest updateRequest = new GiftCertificateUpdateRequest("changed", null,
        null, 10, null);
    GiftCertificate giftCertificateModified = getGiftCertificate();
    giftCertificateModified.setName(updateRequest.getName());
    giftCertificateModified.setDuration(updateRequest.getDuration());

    when(giftCertificateRepository.findById(giftCertificateOriginal.getId())).thenReturn(
        Optional.of(giftCertificateOriginal));
    when(giftCertificateUpdateHandler.handle(updateRequest, giftCertificateOriginal)).thenReturn(
        giftCertificateModified);
    when(giftCertificateRepository.save(giftCertificateModified)).thenAnswer(returnsFirstArg());

    assertEquals(updateRequest.getName(),
        giftCertificateService.update(giftCertificateOriginal.getId(), updateRequest).getName());
  }
  @Test
  void updateShouldReturnGiftCertificateWithPriceMatchingToRequestWhenPriceUpdateRequest() {
    GiftCertificate giftCertificateOriginal = getGiftCertificate();
    GiftCertificatePriceUpdateRequest updateRequest = new GiftCertificatePriceUpdateRequest(BigDecimal.valueOf(22));

    when(giftCertificateRepository.findById(giftCertificateOriginal.getId())).thenReturn(
        Optional.of(giftCertificateOriginal));
    when(giftCertificateRepository.save(any())).thenAnswer(returnsFirstArg());

    assertEquals(updateRequest.getPrice(),
        giftCertificateService.update(giftCertificateOriginal.getId(), updateRequest).getPrice());
  }
}