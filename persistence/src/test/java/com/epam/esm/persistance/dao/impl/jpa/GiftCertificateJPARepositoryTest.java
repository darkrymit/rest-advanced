package com.epam.esm.persistance.dao.impl.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.esm.persistance.config.EmbeddedDatabaseJpaConfig;
import com.epam.esm.persistance.dao.GiftCertificateSearchParameters;
import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.persistance.entity.Tag;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {EmbeddedDatabaseJpaConfig.class})
@ActiveProfiles("integration-test")
@Transactional
class GiftCertificateJPARepositoryTest {
  @PersistenceContext
  EntityManager entityManager;

  GiftCertificateJPARepository giftCertificateJPARepository;

  @BeforeEach
  void setUp() {
    giftCertificateJPARepository = new GiftCertificateJPARepository(entityManager);
  }

  @Test
  void saveShouldInsertEntryWithGeneratedIdWhenEntryHasIdNull() {
    GiftCertificate certificate = new GiftCertificate(null, "test", "testing", BigDecimal.TEN, 405,
        ZonedDateTime.now(), ZonedDateTime.now(), List.of(new Tag(1L, null)));

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
    assertFalse(giftCertificateJPARepository.findAllAsList().isEmpty());
  }

  @Test
  void findAllAsListShouldReturnNonEmptyListWhenCertificatesByParametersExists() {
    GiftCertificateSearchParameters searchParameters = new GiftCertificateSearchParameters("tagName3","gift",null);
    assertFalse(giftCertificateJPARepository.findAllAsList(searchParameters).isEmpty());
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

    assertFalse(giftCertificateJPARepository.findAllAsList().isEmpty());
  }
}