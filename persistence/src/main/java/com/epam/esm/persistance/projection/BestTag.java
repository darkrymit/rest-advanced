package com.epam.esm.persistance.projection;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Value;

@Value
public class BestTag {

  Long id;
  String name;
  Long occurrences;
  UUID ownerId;
  BigDecimal totalPrice;
}
