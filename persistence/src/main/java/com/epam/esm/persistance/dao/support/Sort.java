package com.epam.esm.persistance.dao.support;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;
import lombok.Value;


/**
 * Data class to store sort information
 *
 * @author Tamerlan Hurbanov
 * @see Order
 * @since 1.0
 */
@Value
public class Sort implements Serializable {

  private static final Sort UNSORTED = new Sort(List.of());

  List<Order> order;

  public boolean isEmpty() {
    return order.isEmpty();
  }

  public boolean isSorted() {
    return !this.isEmpty();
  }

  public boolean isUnsorted() {
    return !this.isSorted();
  }

  public static Sort unsorted() {
    return UNSORTED;
  }

  public Stream<Order> stream(){
    return order.stream();
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
  public static class Order implements Serializable{

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
