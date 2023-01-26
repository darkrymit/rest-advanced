package com.epam.esm.persistance.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * Data class represents gift certificate entity.
 *
 * @author Tamerlan Hurbanov
 * @see Entity
 * @since 1.0
 */
@Entity
@Table(name = "certificates")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class GiftCertificate {

  public static final String TABLE_NAME = "certificates";

  public static final String CERTIFICATES_TAGS_KEYS_TABLE = "certificates_has_tags";

  @Id
  @Column(unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String description;
  private BigDecimal price;
  private Integer duration;

  @Column(name = "create_date")
  private Instant createDate;
  @Column(name = "last_update_date")
  private Instant lastUpdateDate;

  @ManyToMany
  @JoinTable(name = "certificates_has_tags",
      joinColumns = {@JoinColumn(name = "certificates_id")},
      inverseJoinColumns = {@JoinColumn(name = "tags_id")})
  @ToString.Exclude
  private List<Tag> tags;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    GiftCertificate that = (GiftCertificate) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
