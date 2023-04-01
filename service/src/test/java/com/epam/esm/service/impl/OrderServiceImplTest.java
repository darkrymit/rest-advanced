package com.epam.esm.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.epam.esm.persistance.dao.GiftCertificateRepository;
import com.epam.esm.persistance.dao.OrderRepository;
import com.epam.esm.persistance.dao.UserRepository;
import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.persistance.entity.Order;
import com.epam.esm.persistance.entity.OrderItem;
import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.persistance.entity.User;
import com.epam.esm.service.exceptions.NoSuchOrderException;
import com.epam.esm.service.payload.request.OrderCreateRequest;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer1;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

  @Mock
  OrderRepository orderRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  GiftCertificateRepository giftCertificateRepository;

  @InjectMocks
  OrderServiceImpl orderService;

  private static Answer1<Order, Order> getFakeSave(Long id) {
    return order -> {
      order.setId(id);
      return order;
    };
  }

  private User getUser() {
    return new User(UUID.fromString("028fcacb-b19b-4268-9e0c-6d96669b0d5e"), Instant.now());
  }

  private GiftCertificate getGiftCertificate() {
    return new GiftCertificate(4L, "second", "Second grade", BigDecimal.valueOf(40), 2,
        Instant.now(), Instant.now(), getTags());
  }

  private List<Tag> getTags() {
    return List.of(new Tag(1L, "basic"), new Tag(2L, "advanced"));
  }

  private Order getOrder(User owner) {
    Order order = new Order(1L, owner, Set.of(), Instant.now(), owner.getId().toString(),
        Instant.now());
    order.setItems(Set.of(new OrderItem(1L, order, getGiftCertificate(), BigDecimal.TEN)));
    return order;
  }

  @Test
  void getByIdShouldReturnNonNullWhenByExistingId() {
    User owner = getUser();
    Order preparedOrder = getOrder(owner);
    UUID uuid = UUID.randomUUID();

    when(orderRepository.findById(preparedOrder.getId())).thenReturn(Optional.of(preparedOrder));

    assertNotNull(orderService.getById(1L,uuid));
  }

  @Test
  void getByIdShouldThrowNoSuchOrderExceptionWhenByNonExistingId() {

    long id = 404L;

    UUID uuid = UUID.fromString("028fcacb-b20b-4268-9e0c-6d96669b0d5e");

    when(orderRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(NoSuchOrderException.class, () -> orderService.getById(id,uuid));
  }

  @Test
  void createShouldReturnOrderWithOwnerMatchingToUserIdtWhenNormalRequest() {
    OrderCreateRequest request = new OrderCreateRequest(List.of("second"));
    User owner = getUser();
    GiftCertificate giftCertificate = getGiftCertificate();

    when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
    when(giftCertificateRepository.findAllByNames(request.getGiftCertificates())).thenReturn(List.of(giftCertificate));
    when(orderRepository.save(any())).thenAnswer(answer(getFakeSave(1L)));

    assertEquals(orderService.create(request,owner.getId()).getOwner(),owner);
  }
}