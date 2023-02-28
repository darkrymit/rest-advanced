package com.epam.esm.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@Relation(collectionRelation = "orders", itemRelation = "order")
public class OrderDTO extends RepresentationModel<OrderDTO> {
  private Long id;

  private UUID ownerId;

  private Set<OrderItemDTO> items;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
  private ZonedDateTime creationDate;

  private String lastModifiedBy;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
  private ZonedDateTime lastModifiedDate;
}
