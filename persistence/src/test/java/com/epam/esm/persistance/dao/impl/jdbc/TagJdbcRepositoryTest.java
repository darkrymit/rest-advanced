package com.epam.esm.persistance.dao.impl.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.esm.persistance.config.EmbeddedJdbcConfig;
import com.epam.esm.persistance.entity.Tag;
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
class TagJdbcRepositoryTest {

  @Autowired
  JdbcTemplate jdbcTemplate;

  TagJdbcRepository tagJdbcRepository;

  @BeforeEach
  void setUp() {
    tagJdbcRepository = new TagJdbcRepository(jdbcTemplate);
  }

  @Test
  void saveShouldInsertEntryWithGeneratedIdWhenEntryHasIdNull() {
    Tag tag = new Tag(null,"nemesis");

    Tag savedTag = tagJdbcRepository.save(tag);

    assertTrue(tagJdbcRepository.findById(savedTag.getId()).isPresent());
  }

  @Test
  void saveShouldUpdateEntryWithNewNameWhenEntryExist() {
    String targetName = "Regular";
    Tag tag = tagJdbcRepository.findById(1L).orElseThrow();

    tag.setName(targetName);

    tagJdbcRepository.save(tag);

    assertEquals(targetName,
        tagJdbcRepository.findById(1L).orElseThrow().getName());
  }

  @Test
  void findByIdShouldReturnPresentOptionalWhenByExistId() {
    assertTrue(tagJdbcRepository.findById(1L).isPresent());
  }

  @Test
  void findByIdShouldReturnEmptyOptionalWhenByNonExistId() {
    assertTrue(tagJdbcRepository.findById(-404L).isEmpty());
  }

  @Test
  void existsByIdShouldReturnTrueWhenByExistId() {
    assertTrue(tagJdbcRepository.existsById(1L));
  }

  @Test
  void existsByIdShouldReturnFalseWhenByNonExistId() {
    assertFalse(tagJdbcRepository.existsById(-404L));
  }

  @Test
  void findAllAsListShouldReturnNonEmptyListWhenCertificatesEntryExists() {
    assertFalse(tagJdbcRepository.findAllAsList().isEmpty());
  }

  @Test
  void deleteShouldDeleteEntryWhenEntryExists() {
    Tag tag = tagJdbcRepository.findById(1L).orElseThrow();

    tagJdbcRepository.delete(tag);

    assertFalse(tagJdbcRepository.existsById(1L));
  }

  @Test
  void deleteShouldNotDeleteAllEntryWhenEntryExists() {
    Tag tag = tagJdbcRepository.findById(1L).orElseThrow();

    tagJdbcRepository.delete(tag);

    assertFalse(tagJdbcRepository.findAllAsList().isEmpty());
  }
}