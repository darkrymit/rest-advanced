package com.epam.esm.web.resolver;

import com.epam.esm.persistance.dao.support.Sort;
import com.epam.esm.persistance.dao.support.Sort.Direction;
import com.epam.esm.persistance.dao.support.Sort.Order;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SortHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  private static final String DEFAULT_PARAMETER = "sort";
  private static final String DEFAULT_PROPERTY_DELIMITER = ",";
  private static final Sort DEFAULT_SORT = Sort.unsorted();

  private Sort fallbackSort = DEFAULT_SORT;
  private String sortParameter = DEFAULT_PARAMETER;
  private String propertyDelimiter = DEFAULT_PROPERTY_DELIMITER;

  public void setFallbackSort(Sort fallbackSort) {
    this.fallbackSort = fallbackSort;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return Sort.class.equals(parameter.getParameterType());
  }

  @Override
  public Sort resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    String[] directionParameter = webRequest.getParameterValues(getSortParameter());

    if (directionParameter == null) {
      return getFallback();
    }

    // Single empty parameter, e.g "sort="
    if (directionParameter.length == 1 && !StringUtils.hasText(directionParameter[0])) {
      return getFallback();
    }

    return parseParameterIntoSort(Arrays.asList(directionParameter), getPropertyDelimiter());
  }

  private Sort parseParameterIntoSort(List<String> orders, String propertyDelimiter) {
    List<Order> allOrders = new ArrayList<>();

    for (String order : orders) {
      if (StringUtils.hasText(order)) {
        processOrderString(allOrders, order, propertyDelimiter);
      }
    }
    return allOrders.isEmpty() ? new Sort(List.of()) : new Sort(allOrders);
  }

  private void processOrderString(List<Sort.Order> allOrders, String order,
      String propertyDelimiter) {
    List<String> parsed = Arrays.stream(order.split(propertyDelimiter)).limit(2)
        .collect(Collectors.toList());
    if (parsed.size() == 1) {
      allOrders.add(new Sort.Order(parsed.get(0), Direction.ASC));
    } else {
      allOrders.add(new Sort.Order(parsed.get(0), Direction.valueOf(parsed.get(1))));
    }
  }

  public List<String> foldIntoExpressions(Sort sort) {
    return sort.stream().map(order -> order.getProperty() + propertyDelimiter + order.getDirection())
        .collect(Collectors.toList());
  }

  public String getPropertyDelimiter() {
    return propertyDelimiter;
  }

  public void setPropertyDelimiter(String propertyDelimiter) {
    this.propertyDelimiter = propertyDelimiter;
  }

  public Sort getFallback() {
    return fallbackSort;
  }

  public String getSortParameter() {
    return sortParameter;
  }

  public void setSortParameter(String sortParameter) {
    this.sortParameter = sortParameter;
  }
}
