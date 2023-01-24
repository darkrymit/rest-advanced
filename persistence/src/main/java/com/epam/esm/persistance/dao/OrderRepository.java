package com.epam.esm.persistance.dao;

import com.epam.esm.persistance.dao.support.SimpleCrudRepository;
import com.epam.esm.persistance.entity.Order;
import java.util.List;

public interface OrderRepository extends SimpleCrudRepository<Order, Long> {

  List<Order> findAllByOwnerId(Long id);
}
