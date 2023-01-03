package com.epam.esm.persistance.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data class represents tag entity.
 *
 * @author Tamerlan Hurbanov
 * @see Entity
 * @since  1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag implements Entity {

  public static final String TABLE_NAME = "tags";

  public static final String CERTIFICATES_TAGS_KEYS_TABLE = GiftCertificate.CERTIFICATES_TAGS_KEYS_TABLE;

  private Long id;

  private String name;

  public Tag(String name) {
    this.id = null;
    this.name = name;
  }
}
