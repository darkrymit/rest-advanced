package com.epam.esm.service.impl;

import com.epam.esm.persistance.dao.UserRepository;
import com.epam.esm.persistance.dao.support.page.Page;
import com.epam.esm.persistance.dao.support.page.Pageable;
import com.epam.esm.persistance.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exceptions.NoSuchUserByEmailException;
import com.epam.esm.service.exceptions.NoSuchUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public Page<User> findAll(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  @Override
  public User getById(long id) throws NoSuchUserException {
    return userRepository.findById(id).orElseThrow(() -> new NoSuchUserException(id));
  }

  @Override
  public User getByEmail(String email) throws NoSuchUserByEmailException {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new NoSuchUserByEmailException(email));
  }

}
