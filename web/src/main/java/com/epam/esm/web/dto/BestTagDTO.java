package com.epam.esm.web.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
public class BestTagDTO extends RepresentationModel<BestTagDTO> {

  private Long id;
  private String name;
  private Long occurrences;
  private UUID ownerId;
  private BigDecimal totalPrice;

}
