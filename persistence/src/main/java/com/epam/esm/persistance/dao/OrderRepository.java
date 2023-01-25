package com.epam.esm.persistance.dao;

import com.epam.esm.persistance.dao.support.SimpleCrudRepository;
import com.epam.esm.persistance.dao.support.page.Page;
import com.epam.esm.persistance.dao.support.page.Pageable;
import com.epam.esm.persistance.entity.Order;

public interface OrderRepository extends SimpleCrudRepository<Order, Long> {

  Page<Order> findAllByOwnerId(Long id, Pageable pageable);
}
