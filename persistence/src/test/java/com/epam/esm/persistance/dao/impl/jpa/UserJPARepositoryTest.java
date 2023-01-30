package com.epam.esm.persistance.dao.impl.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.esm.persistance.config.AuditConfig;
import com.epam.esm.persistance.config.EmbeddedDatabaseJpaConfig;
import com.epam.esm.persistance.entity.User;
import java.time.Instant;
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
@ContextConfiguration(classes = {EmbeddedDatabaseJpaConfig.class, AuditConfig.class})
@ActiveProfiles("integration-test")
@Transactional
class UserJPARepositoryTest {

  @PersistenceContext
  EntityManager entityManager;

  UserJPARepository userJpaRepository;

  @BeforeEach
  void setUp() {
    userJpaRepository = new UserJPARepository(entityManager);
  }

  @Test
  void saveShouldInsertEntryWithGeneratedIdWhenEntryHasIdNull() {
    User user = new User(null, "test1@gmail.com", "hash", "FirstTest", "LastTest", Instant.now(),
        "test1@gmail.com", Instant.now());

    User savedTag = userJpaRepository.save(user);

    assertTrue(userJpaRepository.findById(savedTag.getId()).isPresent());
  }

  @Test
  void saveShouldUpdateEntryWithNewFirstNameWhenEntryExist() {
    String targetName = "Regular";
    User user = userJpaRepository.findById(1L).orElseThrow();

    user.setFirstName(targetName);

    userJpaRepository.save(user);

    assertEquals(targetName, userJpaRepository.findById(1L).orElseThrow().getFirstName());
  }

  @Test
  void findByIdShouldReturnPresentOptionalWhenByExistId() {
    assertTrue(userJpaRepository.findById(1L).isPresent());

  }
  @Test
  void findByIdShouldReturnEmptyOptionalWhenByNonExistId() {
    assertTrue(userJpaRepository.findById(-404L).isEmpty());
  }

  @Test
  void findByEmailShouldReturnPresentOptionalWhenByExistEmail() {
    assertTrue(userJpaRepository.findByEmail("test@gmail.com").isPresent());

  }

  @Test
  void existsByIdShouldReturnTrueWhenByExistId() {
    assertTrue(userJpaRepository.existsById(1L));
  }

  @Test
  void existsByIdShouldReturnFalseWhenByNonExistId() {
    assertFalse(userJpaRepository.existsById(-404L));
  }

  @Test
  void findAllAsListShouldReturnNonEmptyListWhenCertificatesEntryExists() {
    assertFalse(userJpaRepository.findAllAsList().isEmpty());
  }

  @Test
  void deleteShouldDeleteEntryWhenEntryExists() {
    User user = userJpaRepository.findById(1L).orElseThrow();

    userJpaRepository.delete(user);

    assertFalse(userJpaRepository.existsById(1L));
  }

  @Test
  void deleteShouldNotDeleteAllEntryWhenEntryExists() {
    User user = userJpaRepository.findById(1L).orElseThrow();

    userJpaRepository.delete(user);

    assertFalse(userJpaRepository.findAllAsList().isEmpty());
  }
}