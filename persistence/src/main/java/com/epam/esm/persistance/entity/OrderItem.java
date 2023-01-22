package com.epam.esm.persistance.entity;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class OrderItem {
  @Id
  @Column(unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY,optional = false)
  @ToString.Exclude
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gift_certificate_id")
  @ToString.Exclude
  private GiftCertificate giftCertificate;

  @Column(precision=19, scale=4)
  private BigDecimal price;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    OrderItem orderItem = (OrderItem) o;
    return id != null && Objects.equals(id, orderItem.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
