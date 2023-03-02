package com.epam.esm.persistance.dao.impl.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.esm.persistance.dao.OrderRepository;
import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.persistance.entity.Order;
import com.epam.esm.persistance.entity.OrderItem;
import com.epam.esm.persistance.entity.User;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class OrderJPARepositoryTest {

  @Autowired
  TestEntityManager testEntityManager;

  @Autowired
  OrderRepository orderJpaRepository;


  private User getUser() {
    return new User(UUID.fromString("028fcacb-b19b-4268-9e0c-6d96669b0d5e"), Instant.now());
  }

  private OrderItem getOrderItem() {
    return new OrderItem(null, null, testEntityManager.getEntityManager().getReference(GiftCertificate.class, 1L),
        BigDecimal.TEN);
  }

  @Test
  void saveShouldInsertEntryWithGeneratedIdWhenEntryHasIdNull() {
    User creator = getUser();
    OrderItem orderItem = getOrderItem();
    Order order = new Order(null, creator, new HashSet<>(), Instant.now(), creator.getId().toString(),
        Instant.now());
    order.addOrderItem(orderItem);

    Order saved = orderJpaRepository.save(order);

    assertTrue(orderJpaRepository.findById(saved.getId()).isPresent());
  }

  @Test
  void saveShouldInsertOrderItemAlsoWhenOrderHasIdNull() {
    User creator = getUser();
    OrderItem orderItem = getOrderItem();
    Order order = new Order(null, creator, new HashSet<>(), Instant.now(), creator.getId().toString(),
        Instant.now());
    order.addOrderItem(orderItem);

    Order saved = orderJpaRepository.save(order);

    assertFalse(orderJpaRepository.findById(saved.getId()).orElseThrow().getItems().isEmpty());
  }

  @Test
  void saveShouldUpdateEntryWithLimitedItemsWhenEntryExist() {
    Order order = orderJpaRepository.findById(1L).orElseThrow();
    Set<OrderItem> items = order.getItems().stream().limit(1).collect(Collectors.toSet());

    order.setItems(items);

    orderJpaRepository.save(order);

    assertEquals(1, orderJpaRepository.findById(1L).orElseThrow().getItems().size());
  }

  @Test
  void findByIdShouldReturnPresentOptionalWhenByExistId() {
    assertTrue(orderJpaRepository.findById(1L).isPresent());
  }

  @Test
  void findByIdShouldReturnEmptyOptionalWhenByNonExistId() {
    assertTrue(orderJpaRepository.findById(-404L).isEmpty());
  }

  @Test
  void existsByIdShouldReturnTrueWhenByExistId() {
    assertTrue(orderJpaRepository.existsById(1L));
  }

  @Test
  void existsByIdShouldReturnFalseWhenByNonExistId() {
    assertFalse(orderJpaRepository.existsById(-404L));
  }

  @Test
  void findAllAsListShouldReturnNonEmptyListWhenCertificatesEntryExists() {
    assertFalse(orderJpaRepository.findAll().isEmpty());
  }

  @Test
  void deleteShouldDeleteEntryWhenEntryExists() {
    Order order = orderJpaRepository.findById(1L).orElseThrow();

    orderJpaRepository.delete(order);

    assertFalse(orderJpaRepository.existsById(1L));
  }

  @Test
  void deleteShouldNotDeleteAllEntryWhenEntryExists() {
    Order order = orderJpaRepository.findById(1L).orElseThrow();

    orderJpaRepository.delete(order);

    assertFalse(orderJpaRepository.findAll().isEmpty());
  }

  @Test
  void findAllByOwnerIdShouldReturnPageWithContentWhenOwnerHaveOrders() {
    Pageable pageable = Pageable.unpaged();
    assertTrue(orderJpaRepository.findAllByOwnerId(UUID.fromString("028fcacb-b19b-4268-9e0c-6d96669b0d5e"),pageable).hasContent());
  }
}