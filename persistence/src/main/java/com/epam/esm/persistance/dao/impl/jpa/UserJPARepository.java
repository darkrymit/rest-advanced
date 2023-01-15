package com.epam.esm.persistance.dao.impl.jpa;

import com.epam.esm.persistance.dao.UserRepository;
import com.epam.esm.persistance.entity.User;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserJPARepository extends SimpleJpaRepository<User, Long> implements UserRepository {

  @Autowired
  public UserJPARepository() {
    super(User.class);
  }

  public UserJPARepository(EntityManager entityManager) {
    super(User.class, entityManager);
  }

  @Override
  protected <S extends User> Long getIdFromEntity(S entity) {
    return entity.getId();
  }

}
