package com.epam.esm.service.impl;

import com.epam.esm.persistance.dao.GiftCertificateRepository;
import com.epam.esm.persistance.dao.OrderRepository;
import com.epam.esm.persistance.dao.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.util.UUID;
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
  public Order create(OrderCreateRequest request, UUID id) {
    User owner = userRepository.findById(id).orElseThrow(
        () -> new IllegalArgumentException(String.format("Invalid user id %s", id)));
    List<GiftCertificate> certificateList = giftCertificateRepository.findAllByNames(
        request.getGiftCertificates());
    Order order = new Order(null, owner, null, Instant.now(), owner.getId().toString(), Instant.now());
    Set<OrderItem> orderItems = certificateList.stream().map(
            giftCertificate -> new OrderItem(null, order, giftCertificate, giftCertificate.getPrice()))
        .collect(Collectors.toSet());
    order.setItems(orderItems);
    return orderRepository.save(order);
  }

  @Override
  public Page<Order> getAllByOwnerId(UUID id, Pageable pageable) {
    return orderRepository.findAllByOwnerId(id,pageable);
  }
}
