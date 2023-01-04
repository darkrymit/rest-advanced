package com.epam.esm.persistance.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * Data class represents tag entity.
 *
 * @author Tamerlan Hurbanov
 * @see Entity
 * @since  1.0
 */
@Entity
@Table(name = "tags")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Tag {

  public static final String TABLE_NAME = "tags";

  public static final String CERTIFICATES_TAGS_KEYS_TABLE = GiftCertificate.CERTIFICATES_TAGS_KEYS_TABLE;

  @Id
  @Column(unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  public Tag(String name) {
    this.id = null;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Tag tag = (Tag) o;
    return id != null && Objects.equals(id, tag.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
