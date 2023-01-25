package com.epam.esm.persistance.dao.impl.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.esm.persistance.config.EmbeddedDatabaseJpaConfig;
import com.epam.esm.persistance.dao.support.page.Pageable;
import com.epam.esm.persistance.entity.Tag;
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
class TagJPARepositoryTest {

  @PersistenceContext
  EntityManager entityManager;

  TagJPARepository tagJPARepository;

  @BeforeEach
  void setUp() {
    tagJPARepository = new TagJPARepository(entityManager);
  }

  @Test
  void saveShouldInsertEntryWithGeneratedIdWhenEntryHasIdNull() {
    Tag tag = new Tag(null, "nemesis");

    Tag savedTag = tagJPARepository.save(tag);

    assertTrue(tagJPARepository.findById(savedTag.getId()).isPresent());
  }

  @Test
  void saveShouldUpdateEntryWithNewNameWhenEntryExist() {
    String targetName = "Regular";
    Tag tag = tagJPARepository.findById(1L).orElseThrow();

    tag.setName(targetName);

    tagJPARepository.save(tag);

    assertEquals(targetName, tagJPARepository.findById(1L).orElseThrow().getName());
  }

  @Test
  void findByIdShouldReturnPresentOptionalWhenByExistId() {
    assertTrue(tagJPARepository.findById(1L).isPresent());
  }

  @Test
  void findByIdShouldReturnEmptyOptionalWhenByNonExistId() {
    assertTrue(tagJPARepository.findById(-404L).isEmpty());
  }

  @Test
  void existsByIdShouldReturnTrueWhenByExistId() {
    assertTrue(tagJPARepository.existsById(1L));
  }

  @Test
  void existsByIdShouldReturnFalseWhenByNonExistId() {
    assertFalse(tagJPARepository.existsById(-404L));
  }

  @Test
  void findAllAsListShouldReturnNonEmptyListWhenCertificatesEntryExists() {
    assertFalse(tagJPARepository.findAllAsList().isEmpty());
  }

  @Test
  void findAllShouldReturnLimitedTagsWhenByPageable() {
    int requestSize = 2;
    assertEquals(requestSize,
        tagJPARepository.findAll(Pageable.ofSize(requestSize)).getNumberOfElements());
  }

  @Test
  void findAllShouldReturnNotEmptyContentWhenByPageable() {
    int requestSize = 2;
    assertFalse(tagJPARepository.findAll(Pageable.ofSize(requestSize)).getContent().isEmpty());
  }

  @Test
  void findAllShouldReturnFirstPageWhenByPageableWithNoOffset() {
    int requestSize = 2;
    assertTrue(tagJPARepository.findAll(Pageable.ofSize(requestSize)).isFirst());
  }

  @Test
  void deleteShouldDeleteEntryWhenEntryExists() {
    Tag tag = tagJPARepository.findById(1L).orElseThrow();

    tagJPARepository.delete(tag);

    assertFalse(tagJPARepository.existsById(1L));
  }

  @Test
  void deleteShouldNotDeleteAllEntryWhenEntryExists() {
    Tag tag = tagJPARepository.findById(1L).orElseThrow();

    tagJPARepository.delete(tag);

    assertFalse(tagJPARepository.findAllAsList().isEmpty());
  }
}