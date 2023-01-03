package com.epam.esm.persistance.dao.impl.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.esm.persistance.config.EmbeddedJdbcConfig;
import com.epam.esm.persistance.dao.GiftCertificateSearchParameters;
import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.persistance.entity.Tag;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {EmbeddedJdbcConfig.class})
@ActiveProfiles("integration-test")
@Transactional
class GiftCertificateJdbcRepositoryTest {

  @Autowired
  JdbcTemplate jdbcTemplate;

  GiftCertificateJdbcRepository giftCertificateJdbcRepository;

  @BeforeEach
  void setUp() {
    giftCertificateJdbcRepository = new GiftCertificateJdbcRepository(jdbcTemplate);
  }

  @Test
  void saveShouldInsertEntryWithGeneratedIdWhenEntryHasIdNull() {
    GiftCertificate certificate = new GiftCertificate(null, "test", "testing", BigDecimal.TEN, 405,
        ZonedDateTime.now(), ZonedDateTime.now(), List.of(new Tag(1L, null)));

    GiftCertificate savedCertificate = giftCertificateJdbcRepository.save(certificate);

    assertTrue(giftCertificateJdbcRepository.findById(savedCertificate.getId()).isPresent());
  }

  @Test
  void saveShouldUpdateEntryWithNewDescriptionWhenEntryExist() {
    String targetDescription = "Regular test ...";
    GiftCertificate certificate = giftCertificateJdbcRepository.findById(1L).orElseThrow();

    certificate.setDescription(targetDescription);

    giftCertificateJdbcRepository.save(certificate);

    assertEquals(targetDescription,
        giftCertificateJdbcRepository.findById(1L).orElseThrow().getDescription());
  }

  @Test
  void findByIdShouldReturnPresentOptionalWhenByExistId() {
    assertTrue(giftCertificateJdbcRepository.findById(1L).isPresent());
  }

  @Test
  void findByIdShouldReturnEmptyOptionalWhenByNonExistId() {
    assertTrue(giftCertificateJdbcRepository.findById(-404L).isEmpty());
  }

  @Test
  void existsByIdShouldReturnTrueWhenByExistId() {
    assertTrue(giftCertificateJdbcRepository.existsById(1L));
  }

  @Test
  void existsByIdShouldReturnFalseWhenByNonExistId() {
    assertFalse(giftCertificateJdbcRepository.existsById(-404L));
  }

  @Test
  void findAllAsListShouldReturnNonEmptyListWhenCertificatesEntryExists() {
    assertFalse(giftCertificateJdbcRepository.findAllAsList().isEmpty());
  }

  @Test
  void findAllAsListShouldReturnNonEmptyListWhenCertificatesByParametersExists() {
    GiftCertificateSearchParameters searchParameters = new GiftCertificateSearchParameters("tagName3","gift",null);
    assertFalse(giftCertificateJdbcRepository.findAllAsList(searchParameters).isEmpty());
  }

  @Test
  void deleteShouldDeleteEntryWhenEntryExists() {
    GiftCertificate giftCertificate = giftCertificateJdbcRepository.findById(1L).orElseThrow();

    giftCertificateJdbcRepository.delete(giftCertificate);

    assertFalse(giftCertificateJdbcRepository.existsById(1L));
  }

  @Test
  void deleteShouldNotDeleteAllEntryWhenEntryExists() {
    GiftCertificate giftCertificate = giftCertificateJdbcRepository.findById(1L).orElseThrow();

    giftCertificateJdbcRepository.delete(giftCertificate);

    assertFalse(giftCertificateJdbcRepository.findAllAsList().isEmpty());
  }
}