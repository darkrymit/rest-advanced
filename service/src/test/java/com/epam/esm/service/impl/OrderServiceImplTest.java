package com.epam.esm.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.epam.esm.persistance.dao.OrderRepository;
import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.persistance.entity.Order;
import com.epam.esm.persistance.entity.OrderItem;
import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.persistance.entity.User;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

  @Mock
  OrderRepository orderRepository;

  @InjectMocks
  OrderServiceImpl orderService;

  private User getUser() {
    return new User(1L, "test1@gmail.com", "hash", "FirstTest", "LastTest", Instant.now(),
        "test1@gmail.com", Instant.now());
  }

  private GiftCertificate getGiftCertificate() {
    return new GiftCertificate(4L, "second", "Second grade", BigDecimal.valueOf(40), 2,
        Instant.now(), Instant.now(), getTags());
  }

  private List<Tag> getTags() {
    return List.of(new Tag(1L, "basic"), new Tag(2L, "advanced"));
  }

  private Order getOrder(User owner) {
    Order order = new Order(1L, owner, Set.of(), Instant.now(), owner.getEmail(),
        Instant.now());
    order.setItems(Set.of(new OrderItem(1L, order, getGiftCertificate(), BigDecimal.TEN)));
    return order;
  }

  @Test
  void getByIdShouldReturnNonNullWhenByExistingId() {
    User owner = getUser();
    Order preparedOrder = getOrder(owner);

    when(orderRepository.findById(preparedOrder.getId())).thenReturn(Optional.of(preparedOrder));

    assertNotNull(orderService.getById(1L));
  }
}