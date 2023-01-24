package com.epam.esm.web.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
public class BestTagDTO extends RepresentationModel<BestTagDTO> {

  private Long id;
  private String name;
  private Long occurrences;
  private Long ownerId;
  private BigDecimal totalPrice;

}
