package com.epam.esm.web.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemDTO {
  private Long giftCertificateId;

  private BigDecimal price;

}
