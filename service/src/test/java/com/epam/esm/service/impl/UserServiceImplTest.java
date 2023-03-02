package com.epam.esm.service.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.epam.esm.persistance.dao.UserRepository;
import com.epam.esm.persistance.entity.User;
import com.epam.esm.service.UserOAuthDetails;
import com.epam.esm.service.UserOAuthDetailsService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  UserRepository userRepository;

  @Mock
  UserOAuthDetailsService userOAuthDetailsService;

  @InjectMocks
  UserServiceImpl userService;

  private List<User> getUsers() {
    return List.of(
        new User(UUID.fromString("028fcacb-b19b-4268-9e0c-6d96669b0d5e"), Instant.now()),
        new User(UUID.fromString("01e5aa8b-52ef-45ac-bc27-d5ae60dd9481"), Instant.now()));
  }

  private User getUser() {
    return new User(UUID.fromString("028fcacb-b19b-4268-9e0c-6d96669b0d5e"), Instant.now());
  }

  private UserOAuthDetails getUserInfo() {
    return new UserOAuthDetails("test1@gmail.com", "FirstTest", "LastTest");
  }

  private UserOAuthDetails getUserInfo2() {
    return new UserOAuthDetails("test2@gmail.com", "FirstTestTwo", "LastTestTwo");
  }

  @Test
  void findAllShouldReturnPageWithNotEmptyContentWhenEntriesExists() {
    List<User> preparedUsers = getUsers();
    UserOAuthDetails details1 = getUserInfo();
    UserOAuthDetails details2 = getUserInfo2();

    Pageable pageable = Pageable.unpaged();

    when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(preparedUsers));
    when(userOAuthDetailsService.getById(preparedUsers.get(0).getId())).thenReturn(details1);
    when(userOAuthDetailsService.getById(preparedUsers.get(1).getId())).thenReturn(details2);

    assertFalse(userService.findAll(pageable).getContent().isEmpty());
  }

  @Test
  void getByIdShouldReturnNonNullWhenByExistingId() {
    User preparedUser = getUser();
    UserOAuthDetails details = getUserInfo();
    UUID uuid = preparedUser.getId();

    when(userRepository.findById(preparedUser.getId())).thenReturn(Optional.of(preparedUser));
    when(userOAuthDetailsService.getById(preparedUser.getId())).thenReturn(details);

    assertNotNull(userService.getById(uuid));
  }

}