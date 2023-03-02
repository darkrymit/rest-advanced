package com.epam.esm.persistance.dao;

import com.epam.esm.persistance.entity.Order;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


  Page<Order> findAllByOwnerId(UUID id, Pageable pageable);
}
