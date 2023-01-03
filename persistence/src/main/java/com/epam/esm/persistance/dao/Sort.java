package com.epam.esm.persistance.dao;

import java.util.List;
import lombok.Value;


/**
 * Data class to store sort information
 *
 * @author Tamerlan Hurbanov
 * @see Order
 * @since 1.0
 */
@Value
public class Sort {

  List<Order> order;

  boolean isEmpty() {
    return order.isEmpty();
  }

  /**
   * Enum to store direction of sorting
   *
   * @author Tamerlan Hurbanov
   * @since 1.0
   */
  public enum Direction {
    ASC, DESC;

    Direction() {
    }

    public boolean isAscending() {
      return this.equals(ASC);
    }

    public boolean isDescending() {
      return this.equals(DESC);
    }

  }

  /**
   * Data class to store order information such as property and {@link Direction}
   *
   * @author Tamerlan Hurbanov
   * @see Direction
   * @since 1.0
   */
  @Value
  public static class Order {

    String property;
    Direction direction;

    public boolean isAscending() {
      return direction.isAscending();
    }

    public boolean isDescending() {
      return direction.isDescending();
    }
  }

}
