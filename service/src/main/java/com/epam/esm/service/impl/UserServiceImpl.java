package com.epam.esm.service.impl;

import com.epam.esm.persistance.dao.UserRepository;
import com.epam.esm.persistance.dao.support.page.Page;
import com.epam.esm.persistance.dao.support.page.Pageable;
import com.epam.esm.persistance.entity.User;
import com.epam.esm.service.UserInfo;
import com.epam.esm.service.UserOAuthDetails;
import com.epam.esm.service.UserOAuthDetailsService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exceptions.NoSuchUserException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final UserOAuthDetailsService userOAuthDetailsService;

  @Override
  public Page<UserInfo> findAll(Pageable pageable) {
    return userRepository.findAll(pageable).map(this::fetchData);
  }

  @Override
  public UserInfo getById(UUID id) throws NoSuchUserException {
    return userRepository.findById(id).map(this::fetchData)
        .orElseThrow(() -> new NoSuchUserException(id));
  }
  private UserInfo fetchData(User user) {
    UserOAuthDetails details = userOAuthDetailsService.getById(user.getId());
    return new UserInfo(user.getId(), details.getEmail(), details.getFirstName(),
        details.getFirstName(), user.getCreationDate());
  }
}
