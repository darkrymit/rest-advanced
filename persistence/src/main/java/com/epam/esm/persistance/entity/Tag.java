package com.epam.esm.persistance.entity;

import com.epam.esm.persistance.projection.BestTag;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
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
 * @since 1.0
 */
@Entity
@Table(name = "tags")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SqlResultSetMapping(//
    name = "BestTagMapping",//
    classes = @ConstructorResult(//
        targetClass = BestTag.class,//
        columns = {@ColumnResult(name = "id", type = Long.class),//
            @ColumnResult(name = "name", type = String.class),//
            @ColumnResult(name = "occurrences", type = Long.class),//
            @ColumnResult(name = "owner_id", type = Long.class),//
            @ColumnResult(name = "total_price", type = BigDecimal.class),//
        }//
    )//
)//
@NamedNativeQuery(name = "findMostUsedTagForBestBuyer", //
    query = "SELECT  "//
        + "    t.id, "//
        + "    t.name, "//
        + "    COUNT(t.id) AS occurrences, "//
        + "    u.id AS owner_id, "//
        + "    sub.total_price "//
        + "FROM "//
        + "    users AS u "//
        + "        JOIN "//
        + "    orders AS o ON u.id = o.owner_id "//
        + "        JOIN "//
        + "    order_items AS oi ON o.id = oi.order_id "//
        + "        JOIN "//
        + "    certificates AS c ON oi.gift_certificate_id = c.id "//
        + "        JOIN "//
        + "    certificates_has_tags AS cht ON c.id = cht.certificates_id "//
        + "        JOIN "//
        + "    tags AS t ON cht.tags_id = t.id "//
        + "        JOIN "//
        + "    (SELECT  "//
        + "        o.owner_id, SUM(oi.price) AS total_price "//
        + "    FROM "//
        + "        orders AS o "//
        + "    JOIN order_items AS oi ON o.id = oi.order_id "//
        + "    GROUP BY o.owner_id "//
        + "    ORDER BY total_price DESC "//
        + "    LIMIT 1) AS sub ON u.id IN (sub.owner_id) "//
        + "WHERE "//
        + "    sub.owner_id IS NOT NULL "//
        + "GROUP BY t.id "//
        + "ORDER BY occurrences DESC , t.id ASC "//
        + "LIMIT 1 ",//
    resultSetMapping = "BestTagMapping")
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
