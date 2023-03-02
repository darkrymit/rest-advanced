package com.epam.esm.service;


import com.epam.esm.persistance.entity.Order;
import com.epam.esm.service.exceptions.NoSuchOrderException;
import com.epam.esm.service.payload.request.OrderCreateRequest;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;


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
   * @param orderId id of requested entity
   * @param userId  id of user who request entity
   * @return entity with given id
   * @throws NoSuchOrderException if there is no entity by given id
   */
  @PreAuthorize("hasAnyRole('Administrator','User')")
  @PostAuthorize("hasRole('Administrator') || @seh.hasSameId(returnObject.owner.id)")
  Order getById(long orderId, UUID userId) throws NoSuchOrderException;

  @PreAuthorize("hasRole('Administrator') || @seh.hasSameId(#id)")
  Order create(OrderCreateRequest request, UUID id);

  @PreAuthorize("hasRole('Administrator') || @seh.hasSameId(#id)")
  Page<Order> getAllByOwnerId(UUID id, Pageable pageable);
}
