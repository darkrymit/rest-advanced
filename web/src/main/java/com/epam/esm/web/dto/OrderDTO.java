package com.epam.esm.web.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@Relation(collectionRelation = "orders", itemRelation = "order")
public class OrderDTO extends RepresentationModel<OrderDTO> {
  private Long id;

  private Long ownerId;

  private BigDecimal totalPrice;

  private Set<OrderItemDTO> items;

  private Instant creationDate;

  private String lastModifiedBy;

  private Instant lastModifiedDate;
}
