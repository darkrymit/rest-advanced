package com.epam.esm.service;

import com.epam.esm.persistance.entity.Order;
import com.epam.esm.service.exceptions.NoSuchOrderException;
import com.epam.esm.service.payload.request.OrderCreateRequest;
import java.util.List;


/**
 * Interface describes business logic for working with {@link Order} entity.
 *
 * @author Tamerlan Hurbanov
 * @see Order
 * @since 1.0
 */
public interface OrderService {

  /**
   * Returns {@link Order} entry by id
   *
   * @param id id of requested entity
   * @return entity with given id
   * @throws NoSuchOrderException if there is no entity by given id
   */
  Order getById(long id) throws NoSuchOrderException;

  Order create(OrderCreateRequest request,String userEmail);

  List<Order> getAllByOwnerId(Long id);
}
