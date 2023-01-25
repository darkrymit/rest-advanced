package com.epam.esm.service.payload.request;

import com.epam.esm.validation.tag.Name;
import java.util.List;
import lombok.Value;
import org.springframework.util.StringUtils;

@Value
public class GiftCertificateSearchRequest {
  List<@Name String> tags;
  String part;

  public boolean isTagsPresent() {
    return tags != null;
  }

  public boolean isPartPresent() {
    return StringUtils.hasText(part);
  }
}
