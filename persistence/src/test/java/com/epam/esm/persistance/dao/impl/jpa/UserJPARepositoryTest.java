package com.epam.esm.persistance.dao.impl.jpa;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.esm.persistance.config.AuditConfig;
import com.epam.esm.persistance.config.EmbeddedDatabaseJpaConfig;
import com.epam.esm.persistance.entity.User;
import java.time.Instant;
import java.util.UUID;
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
  void saveShouldInsertEntryWithIdWhenEntryHasIdNotNull() {
    User user = new User(UUID.fromString("028fcacb-b19b-4268-9e0c-6d96669bfd5e"), Instant.now());

    User savedTag = userJpaRepository.save(user);

    assertTrue(userJpaRepository.findById(savedTag.getId()).isPresent());
  }

  @Test
  void findByIdShouldReturnPresentOptionalWhenByExistId() {
    assertTrue(userJpaRepository.findById(UUID.fromString("028fcacb-b19b-4268-9e0c-6d96669b0d5e")).isPresent());

  }
  @Test
  void findByIdShouldReturnEmptyOptionalWhenByNonExistId() {
    assertTrue(userJpaRepository.findById(UUID.fromString("404fcacb-b19b-4268-9e0c-6d96669b0d6e")).isEmpty());
  }

  @Test
  void existsByIdShouldReturnTrueWhenByExistId() {
    assertTrue(userJpaRepository.existsById(UUID.fromString("028fcacb-b19b-4268-9e0c-6d96669b0d5e")));
  }

  @Test
  void existsByIdShouldReturnFalseWhenByNonExistId() {
    assertFalse(userJpaRepository.existsById(UUID.fromString("404fcacb-b19b-4268-9e0c-6d96669b0d6e")));
  }

  @Test
  void findAllAsListShouldReturnNonEmptyListWhenCertificatesEntryExists() {
    assertFalse(userJpaRepository.findAllAsList().isEmpty());
  }

  @Test
  void deleteShouldDeleteEntryWhenEntryExists() {
    User user = userJpaRepository.findById(UUID.fromString("028fcacb-b19b-4268-9e0c-6d96669b0d5e")).orElseThrow();

    userJpaRepository.delete(user);

    assertFalse(userJpaRepository.existsById(UUID.fromString("028fcacb-b19b-4268-9e0c-6d96669b0d5e")));
  }

  @Test
  void deleteShouldNotDeleteAllEntryWhenEntryExists() {
    User user = userJpaRepository.findById(UUID.fromString("028fcacb-b19b-4268-9e0c-6d96669b0d5e")).orElseThrow();

    userJpaRepository.delete(user);

    assertFalse(userJpaRepository.findAllAsList().isEmpty());
  }
}