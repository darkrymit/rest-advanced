package com.epam.esm.persistance.dao.impl.jpa;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.esm.persistance.dao.UserRepository;
import com.epam.esm.persistance.entity.User;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserJPARepositoryTest {

  @Autowired
  UserRepository userJpaRepository;

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
    assertFalse(userJpaRepository.findAll().isEmpty());
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

    assertFalse(userJpaRepository.findAll().isEmpty());
  }
}