package com.epam.esm.service.payload.request;


import com.epam.esm.validation.giftcertificate.optional.OptionalDescription;
import com.epam.esm.validation.giftcertificate.optional.OptionalDuration;
import com.epam.esm.validation.giftcertificate.optional.OptionalName;
import com.epam.esm.validation.giftcertificate.optional.OptionalPrice;
import com.epam.esm.validation.giftcertificate.optional.OptionalTags;
import com.epam.esm.validation.tag.Name;
import java.math.BigDecimal;
import java.util.List;
import lombok.Value;

@Value
public class GiftCertificateUpdateRequest {

  @OptionalName String name;

  @OptionalDescription String description;

  @OptionalPrice BigDecimal price;

  @OptionalDuration Integer duration;

  @OptionalTags List<@Name String> tags;

  public boolean isNamePresent() {
    return name != null;
  }

  public boolean isDescriptionPresent() {
    return description != null;
  }

  public boolean isPricePresent() {
    return price != null;
  }

  public boolean isDurationPresent() {
    return duration != null;
  }

  public boolean isTagsPresent() {
    return tags != null;
  }

}
