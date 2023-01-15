package com.epam.esm.service.impl;

import com.epam.esm.persistance.dao.UserRepository;
import com.epam.esm.persistance.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exceptions.NoSuchUserException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public List<User> findAll() {
    return userRepository.findAllAsList();
  }

  @Override
  public User getById(long id) throws NoSuchUserException {
    return userRepository.findById(id).orElseThrow(() -> new NoSuchUserException(id));
  }
}
