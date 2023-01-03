package com.epam.esm.persistance.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data class represents gift certificate entity.
 *
 * @author Tamerlan Hurbanov
 * @see Entity
 * @since  1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificate implements Entity {

  public static final String TABLE_NAME = "certificates";

  public static final String CERTIFICATES_TAGS_KEYS_TABLE = "certificates_has_tags";

  private Long id;
  private String name;
  private String description;
  private BigDecimal price;
  private Integer duration;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
  private ZonedDateTime createDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
  private ZonedDateTime lastUpdateDate;
  private List<Tag> tags;
}
