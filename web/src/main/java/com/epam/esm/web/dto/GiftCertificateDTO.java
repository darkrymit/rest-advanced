package com.epam.esm.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@Relation(collectionRelation = "giftCertificates", itemRelation = "giftCertificate")
public class GiftCertificateDTO extends RepresentationModel<GiftCertificateDTO> {
  Long id;
  String name;
  String description;
  BigDecimal price;
  Integer duration;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
  ZonedDateTime createDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
  ZonedDateTime lastUpdateDate;
  CollectionModel<TagDTO> tags;
}

