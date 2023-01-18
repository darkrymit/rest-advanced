package com.epam.esm.service.impl;

import com.epam.esm.persistance.dao.OrderRepository;
import com.epam.esm.persistance.entity.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exceptions.NoSuchOrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;

  @Override
  @Transactional
  public Order getById(long id) throws NoSuchOrderException {
    return orderRepository.findById(id).orElseThrow(() -> new NoSuchOrderException(id));
  }
}
