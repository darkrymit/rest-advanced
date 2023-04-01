package com.epam.esm.persistance.entity;

import static com.epam.esm.persistance.TestUtils.getEqualsVerifier;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  void equalForHibernate() {
    getEqualsVerifier().forClass(Order.class)
        .withPrefabValues(OrderItem.class, new OrderItem(1L, null, null, BigDecimal.TEN),
            new OrderItem(2L, null, null, BigDecimal.ONE)).verify();
  }
}