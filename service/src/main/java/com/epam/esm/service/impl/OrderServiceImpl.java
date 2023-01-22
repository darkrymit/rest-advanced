package com.epam.esm.service.impl;

import com.epam.esm.persistance.dao.GiftCertificateRepository;
import com.epam.esm.persistance.dao.OrderRepository;
import com.epam.esm.persistance.dao.UserRepository;
import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.persistance.entity.Order;
import com.epam.esm.persistance.entity.OrderItem;
import com.epam.esm.persistance.entity.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exceptions.NoSuchOrderException;
import com.epam.esm.service.payload.request.OrderCreateRequest;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;

  private final UserRepository userRepository;

  private final GiftCertificateRepository giftCertificateRepository;

  @Override
  @Transactional
  public Order getById(long id) throws NoSuchOrderException {
    return orderRepository.findById(id).orElseThrow(() -> new NoSuchOrderException(id));
  }

  @Override
  @Transactional
  public Order create(OrderCreateRequest request, String userEmail) {
    User owner = userRepository.findByEmail(userEmail).orElseThrow(
        () -> new IllegalArgumentException(String.format("Invalid user email %s", userEmail)));
    List<GiftCertificate> certificateList = giftCertificateRepository.findAllByNames(
        request.getGiftCertificates());
    Order order = new Order(null, owner, null, Instant.now(), owner.getEmail(), Instant.now());
    Set<OrderItem> orderItems = certificateList.stream().map(
            giftCertificate -> new OrderItem(null, order, giftCertificate, giftCertificate.getPrice()))
        .collect(Collectors.toSet());
    order.setItems(orderItems);
    return orderRepository.save(order);
  }
}
