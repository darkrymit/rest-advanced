package com.epam.esm.persistance.dao.impl.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.esm.persistance.dao.GiftCertificateRepository;
import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.persistance.entity.GiftCertificate_;
import com.epam.esm.persistance.entity.Tag;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@DataJpaTest
class GiftCertificateJPARepositoryTest {

  @Autowired
  GiftCertificateRepository giftCertificateJPARepository;


  @Test
  void saveShouldInsertEntryWithGeneratedIdWhenEntryHasIdNull() {
    GiftCertificate certificate = new GiftCertificate(null, "test", "testing", BigDecimal.TEN, 405,
        Instant.now(), Instant.now(), List.of(new Tag(1L, null)));

    GiftCertificate savedCertificate = giftCertificateJPARepository.save(certificate);

    assertTrue(giftCertificateJPARepository.findById(savedCertificate.getId()).isPresent());
  }

  @Test
  void saveShouldUpdateEntryWithNewDescriptionWhenEntryExist() {
    String targetDescription = "Regular test ...";
    GiftCertificate certificate = giftCertificateJPARepository.findById(1L).orElseThrow();

    certificate.setDescription(targetDescription);

    giftCertificateJPARepository.save(certificate);

    assertEquals(targetDescription,
        giftCertificateJPARepository.findById(1L).orElseThrow().getDescription());
  }

  @Test
  void findByIdShouldReturnPresentOptionalWhenByExistId() {
    assertTrue(giftCertificateJPARepository.findById(1L).isPresent());
  }

  @Test
  void findByIdShouldReturnEmptyOptionalWhenByNonExistId() {
    assertTrue(giftCertificateJPARepository.findById(-404L).isEmpty());
  }

  @Test
  void existsByIdShouldReturnTrueWhenByExistId() {
    assertTrue(giftCertificateJPARepository.existsById(1L));
  }

  @Test
  void existsByIdShouldReturnFalseWhenByNonExistId() {
    assertFalse(giftCertificateJPARepository.existsById(-404L));
  }

  @Test
  void findAllAsListShouldReturnNonEmptyListWhenCertificatesEntryExists() {
    assertFalse(giftCertificateJPARepository.findAll().isEmpty());
  }
  @Test
  void findAllByNamesShouldReturnNonEmptyListWhenCertificatesEntryExists() {
    assertFalse(giftCertificateJPARepository.findAllByNames(List.of("giftCertificate3","giftCertificate1")).isEmpty());
  }

  @Test
  void findAllShouldReturnPageWithContentWhenCertificatesByParametersExists() {
    String pattern = "%" + "gift" + "%";
    Specification<GiftCertificate> specification = (root, query, cb) -> cb.or(cb.like(root.get(GiftCertificate_.name), pattern),
        cb.like(root.get(GiftCertificate_.description), pattern));

    assertTrue(giftCertificateJPARepository.findAll(specification, Pageable.unpaged()).hasContent());
  }

  @Test
  void deleteShouldDeleteEntryWhenEntryExists() {
    GiftCertificate giftCertificate = giftCertificateJPARepository.findById(1L).orElseThrow();

    giftCertificateJPARepository.delete(giftCertificate);

    assertFalse(giftCertificateJPARepository.existsById(1L));
  }

  @Test
  void deleteShouldNotDeleteAllEntryWhenEntryExists() {
    GiftCertificate giftCertificate = giftCertificateJPARepository.findById(1L).orElseThrow();

    giftCertificateJPARepository.delete(giftCertificate);

    assertFalse(giftCertificateJPARepository.findAll().isEmpty());
  }
}