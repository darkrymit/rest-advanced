package com.epam.esm.persistance.dao;

import com.epam.esm.persistance.dao.support.page.PagingRepository;
import com.epam.esm.persistance.entity.User;
import java.util.Optional;

public interface UserRepository extends PagingRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
