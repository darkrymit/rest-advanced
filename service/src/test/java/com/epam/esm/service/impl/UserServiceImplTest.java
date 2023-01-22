package com.epam.esm.service.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.epam.esm.persistance.dao.UserRepository;
import com.epam.esm.persistance.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserServiceImpl userService;

  private List<User> getUsers() {
    return List.of(new User(1L, "test1@gmail.com", "hash", "FirstTest", "LastTest", Instant.now(),
            "test1@gmail.com", Instant.now()),
        new User(2L, "test2@gmail.com", "hash2", "FirstTestTwo", "LastTestTwo", Instant.now(),
            "test2@gmail.com", Instant.now()));
  }

  private User getUser() {
    return new User(1L, "test1@gmail.com", "hash", "FirstTest", "LastTest", Instant.now(),
        "test1@gmail.com", Instant.now());
  }

  @Test
  void findAllShouldReturnNotEmptyListWhenEntriesExists() {
    List<User> preparedUsers = getUsers();

    when(userRepository.findAllAsList()).thenReturn(preparedUsers);

    assertFalse(userService.findAll().isEmpty());
  }

  @Test
  void getByIdShouldReturnNonNullWhenByExistingId() {
    User preparedUser = getUser();

    when(userRepository.findById(preparedUser.getId())).thenReturn(Optional.of(preparedUser));

    assertNotNull(userService.getById(1L));
  }

  @Test
  void getByEmailShouldReturnNonNullWhenByExistingEmail() {
    User preparedUser = getUser();

    when(userRepository.findByEmail(preparedUser.getEmail())).thenReturn(Optional.of(preparedUser));

    assertNotNull(userService.getByEmail(preparedUser.getEmail()));
  }
}