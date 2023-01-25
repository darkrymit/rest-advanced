package com.epam.esm.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
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
  List<TagDTO> tags;
}

