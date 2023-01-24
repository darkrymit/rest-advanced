package com.epam.esm.persistance.projection;

import java.math.BigDecimal;
import lombok.Value;

@Value
public class BestTag {

  Long id;
  String name;
  Long occurrences;
  Long ownerId;
  BigDecimal totalPrice;
}
