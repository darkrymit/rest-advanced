package com.epam.esm.service.payload.request;

import com.epam.esm.validation.giftcertificate.Description;
import com.epam.esm.validation.giftcertificate.Duration;
import com.epam.esm.validation.giftcertificate.Name;
import com.epam.esm.validation.giftcertificate.Price;
import com.epam.esm.validation.giftcertificate.Tags;
import java.math.BigDecimal;
import java.util.List;
import lombok.Value;

@Value
public class GiftCertificateCreateRequest {

  @Name String name;

  @Description String description;

  @Price BigDecimal price;

  @Duration Integer duration;

  @Tags List<@com.epam.esm.validation.tag.Name String> tags;

}
