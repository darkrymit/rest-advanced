package com.epam.esm.persistance.dao.impl.jpa;

import com.epam.esm.persistance.dao.OrderRepository;
import com.epam.esm.persistance.dao.support.page.Page;
import com.epam.esm.persistance.dao.support.page.Pageable;
import com.epam.esm.persistance.entity.Order;
import com.epam.esm.persistance.entity.Order_;
import com.epam.esm.persistance.entity.User_;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderJpaRepository extends SimpleJpaRepository<Order, Long> implements
    OrderRepository {

  @Autowired
  public OrderJpaRepository() {
    super(Order.class);
  }

  public OrderJpaRepository(EntityManager entityManager) {
    super(Order.class, entityManager);
  }

  @Override
  protected <S extends Order> Long getIdFromEntity(S entity) {
    return entity.getId();
  }

  @Override
  public Page<Order> findAllByOwnerId(UUID id, Pageable pageable) {
    return findAll(
        (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Order_.owner).get(User_.id), id),
        pageable);
  }
}
