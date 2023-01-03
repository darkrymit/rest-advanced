package com.epam.esm.service.payload.request;

import com.epam.esm.persistance.dao.Sort;
import com.epam.esm.validation.tag.optional.OptionalName;
import lombok.Value;

@Value
public class GiftCertificateSearchRequest {
  @OptionalName
  String tagName;
  String part;
  Sort sort;

}
