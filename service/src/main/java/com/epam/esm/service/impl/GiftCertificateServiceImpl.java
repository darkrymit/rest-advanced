package com.epam.esm.service.impl;

import com.epam.esm.persistance.dao.GiftCertificateRepository;
import com.epam.esm.persistance.dao.TagRepository;
import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.persistance.entity.GiftCertificate_;
import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.persistance.entity.Tag_;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exceptions.NoSuchGiftCertificateException;
import com.epam.esm.service.impl.handler.GiftCertificateUpdateHandler;
import com.epam.esm.service.payload.request.GiftCertificateCreateRequest;
import com.epam.esm.service.payload.request.GiftCertificatePriceUpdateRequest;
import com.epam.esm.service.payload.request.GiftCertificateSearchRequest;
import com.epam.esm.service.payload.request.GiftCertificateUpdateRequest;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

  private final GiftCertificateRepository giftCertificateRepository;

  private final TagRepository tagRepository;

  @Qualifier("compositeGiftCertificateUpdateHandler")
  private final GiftCertificateUpdateHandler giftCertificateUpdateHandler;

  private static Supplier<NoSuchGiftCertificateException> getNoSuchGiftCertificateException(
      Long id) {
    return () -> new NoSuchGiftCertificateException(id);
  }

  private static Specification<GiftCertificate> getPartSpecification(
      GiftCertificateSearchRequest searchRequest) {
    String pattern = "%" + searchRequest.getPart() + "%";
    return (root, query, cb) -> cb.or(cb.like(root.get(GiftCertificate_.name), pattern),
        cb.like(root.get(GiftCertificate_.description), pattern));
  }

  private static Specification<GiftCertificate> getTagsSpecification(
      GiftCertificateSearchRequest searchRequest) {
    List<String> tags = searchRequest.getTags();
    return tags.stream().map(GiftCertificateServiceImpl::getTagSpecification)
        .reduce(Specification.where(null), Specification::and);
  }

  private static Specification<GiftCertificate> getTagSpecification(String tag) {
    return (root, query, cb) -> cb.equal(root.join(GiftCertificate_.tags).get(Tag_.name), tag);
  }

  @Override
  public Page<GiftCertificate> findAll(GiftCertificateSearchRequest searchRequest,
      Pageable pageable) {
    Specification<GiftCertificate> specification = Specification.where(null);

    if (searchRequest.isTagsPresent()) {
      specification = specification.and(getTagsSpecification(searchRequest));
    }

    if (searchRequest.isPartPresent()) {
      specification = specification.and(getPartSpecification(searchRequest));
    }

    return giftCertificateRepository.findAll(specification, pageable);
  }

  @Override
  public GiftCertificate getById(long id) {
    return giftCertificateRepository.findById(id)
        .orElseThrow(getNoSuchGiftCertificateException(id));
  }

  @Override
  @Transactional
  public void deleteById(long id) {
    GiftCertificate certificate = giftCertificateRepository.findById(id)
        .orElseThrow(getNoSuchGiftCertificateException(id));
    giftCertificateRepository.delete(certificate);
  }

  @Override
  @Transactional
  public GiftCertificate create(GiftCertificateCreateRequest request) {
    List<Tag> tags = request.getTags().stream().map(tagName -> tagRepository.findByName(tagName)
        .orElseGet(() -> tagRepository.save(new Tag(tagName)))).collect(Collectors.toList());

    GiftCertificate giftCertificate = new GiftCertificate(null, request.getName(),
        request.getDescription(), request.getPrice(), request.getDuration(), Instant.now(),
        Instant.now(), tags);

    return giftCertificateRepository.save(giftCertificate);
  }

  @Override
  @Transactional
  public GiftCertificate update(long id, GiftCertificateUpdateRequest updateRequest) {
    GiftCertificate giftCertificate = giftCertificateRepository.findById(id)
        .orElseThrow(getNoSuchGiftCertificateException(id));

    giftCertificate = giftCertificateUpdateHandler.handle(updateRequest, giftCertificate);

    return giftCertificateRepository.save(giftCertificate);
  }

  @Override
  @Transactional
  public GiftCertificate update(long id, GiftCertificatePriceUpdateRequest updateRequest) {
    GiftCertificate giftCertificate = giftCertificateRepository.findById(id)
        .orElseThrow(getNoSuchGiftCertificateException(id));
    giftCertificate.setPrice(updateRequest.getPrice());
    return giftCertificateRepository.save(giftCertificate);
  }

}
