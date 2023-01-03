package com.epam.esm.web.converter;

import com.epam.esm.persistance.dao.Sort;
import com.epam.esm.persistance.dao.Sort.Direction;
import com.epam.esm.persistance.dao.Sort.Order;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class StringArrayToSortConverter implements Converter<String[], Sort> {

  public static final String DELIMITER = ",";

  @Override
  public Sort convert(String[] source) {
    log.debug("Start convert String[] to Sort");
    if (source.length == 0) {
      return new Sort(List.of());
    }

    if (source.length == 1 && !StringUtils.hasText(source[0])) {
      return new Sort(List.of());
    }

    return parseParameterIntoSort(Arrays.asList(source));
  }

  Sort parseParameterIntoSort(List<String> orders) {
    List<Sort.Order> allOrders = new ArrayList<>();

    for (String order : orders) {
      if (StringUtils.hasText(order)) {
        processOrderString(allOrders, order);
      }
    }

    return allOrders.isEmpty() ? new Sort(List.of()) : new Sort(allOrders);
  }

  private void processOrderString(List<Order> allOrders, String order) {
    List<String> parsed = Arrays.stream(order.split(DELIMITER)).limit(2)
        .collect(Collectors.toList());
    if (parsed.size() == 1) {
      allOrders.add(new Order(parsed.get(0), Direction.ASC));
    } else {
      allOrders.add(new Order(parsed.get(0), Direction.valueOf(parsed.get(1))));
    }
  }
}