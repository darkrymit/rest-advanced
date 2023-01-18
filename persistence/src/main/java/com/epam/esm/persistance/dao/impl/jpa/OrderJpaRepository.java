package com.epam.esm.persistance.dao.impl.jpa;

import com.epam.esm.persistance.dao.OrderRepository;
import com.epam.esm.persistance.entity.Order;
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

}
