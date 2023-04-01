package com.epam.esm.persistance.entity;

import static com.epam.esm.persistance.TestUtils.getEqualsVerifier;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OrderItemTest {

  private User getUser() {
    return new User(UUID.fromString("028fcacb-b19b-4268-9e0c-6d96669b0d5e"), Instant.now());
  }

  @Test
  void equalForHibernate() {
    User owner = getUser();
    Order order1 = new Order(1L, owner, Set.of(), Instant.now(), owner.getId().toString(),
        Instant.now());
    Order order2 = new Order(2L, owner, Set.of(), Instant.now(), owner.getId().toString(),
        Instant.now());

    getEqualsVerifier().forClass(OrderItem.class)
        .withPrefabValues(Order.class,order1,order2)
        .verify();
  }
}