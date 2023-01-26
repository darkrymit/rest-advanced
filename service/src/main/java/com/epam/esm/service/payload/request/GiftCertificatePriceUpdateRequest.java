package com.epam.esm.service.payload.request;


import com.epam.esm.validation.giftcertificate.Price;
import java.math.BigDecimal;
import lombok.Value;

@Value
public class GiftCertificatePriceUpdateRequest {
  @Price BigDecimal price;
}
