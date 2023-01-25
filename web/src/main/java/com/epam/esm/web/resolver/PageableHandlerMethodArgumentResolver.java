package com.epam.esm.web.resolver;

import com.epam.esm.persistance.dao.support.Sort;
import com.epam.esm.persistance.dao.support.page.PageRequest;
import com.epam.esm.persistance.dao.support.page.Pageable;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PageableHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  static final Pageable DEFAULT_PAGE_REQUEST = PageRequest.of(0, 20);
  private static final SortHandlerMethodArgumentResolver DEFAULT_SORT_RESOLVER = new SortHandlerMethodArgumentResolver();
  private static final String DEFAULT_PAGE_PARAMETER = "page";
  private static final String DEFAULT_SIZE_PARAMETER = "size";

  private static final int DEFAULT_MAX_PAGE_SIZE = 2000;
  private int maxPageSize = DEFAULT_MAX_PAGE_SIZE;
  private Pageable fallbackPageable = DEFAULT_PAGE_REQUEST;
  private String pageParameterName = DEFAULT_PAGE_PARAMETER;
  private String sizeParameterName = DEFAULT_SIZE_PARAMETER;
  private SortHandlerMethodArgumentResolver sortResolver = DEFAULT_SORT_RESOLVER;
  private boolean oneIndexedParameters = false;

  public PageableHandlerMethodArgumentResolver() {
  }

  public PageableHandlerMethodArgumentResolver(SortHandlerMethodArgumentResolver sortResolver) {
    this.sortResolver = sortResolver;
  }

  public Pageable getFallbackPageable() {
    return fallbackPageable;
  }

  public void setFallbackPageable(Pageable fallbackPageable) {
    this.fallbackPageable = fallbackPageable;
  }

  public String getPageParameterName() {
    return pageParameterName;
  }

  public void setPageParameterName(String pageParameterName) {
    this.pageParameterName = pageParameterName;
  }

  public String getSizeParameterName() {
    return sizeParameterName;
  }

  public void setSizeParameterName(String sizeParameterName) {
    this.sizeParameterName = sizeParameterName;
  }

  public SortHandlerMethodArgumentResolver getSortResolver() {
    return sortResolver;
  }

  public void setSortResolver(SortHandlerMethodArgumentResolver sortResolver) {
    this.sortResolver = sortResolver;
  }

  public boolean isOneIndexedParameters() {
    return oneIndexedParameters;
  }

  public void setOneIndexedParameters(boolean oneIndexedParameters) {
    this.oneIndexedParameters = oneIndexedParameters;
  }

  public void setMaxPageSize(int maxPageSize) {
    this.maxPageSize = maxPageSize;
  }

  public int getMaxPageSize() {
    return maxPageSize;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return Pageable.class.equals(parameter.getParameterType());
  }

  @Override
  public Pageable resolveArgument(MethodParameter methodParameter,
      ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) throws Exception {
    String page = webRequest.getParameter(getPageParameterName());
    String pageSize = webRequest.getParameter(getSizeParameterName());

    Sort sort = sortResolver.resolveArgument(methodParameter, mavContainer, webRequest,
        binderFactory);
    Pageable pageable = getPageable(page, pageSize);

    if (sort.isSorted()) {
      return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    return pageable;
  }

  protected Pageable getPageable(String pageString, String pageSizeString) {
    Pageable targetFallback = getFallbackPageable();

    Optional<Integer> page = parseAndApplyBoundaries(pageString, Integer.MAX_VALUE, true);
    Optional<Integer> pageSize = parseAndApplyBoundaries(pageSizeString, maxPageSize, false);

    if (!(page.isPresent() && pageSize.isPresent()) && fallbackPageable == null) {
      return Pageable.unpaged();
    }

    int targetPage = page.orElseGet(targetFallback::getPageNumber);
    int targetPageSize = pageSize.orElseGet(targetFallback::getPageSize);

    // Limit lower bound
    targetPageSize = targetPageSize < 1 ? targetFallback.getPageSize() : targetPageSize;
    // Limit upper bound
    targetPageSize = Math.min(targetPageSize, maxPageSize);

    return PageRequest.of(targetPage, targetPageSize, targetFallback.getSort());
  }

  private Optional<Integer> parseAndApplyBoundaries(@Nullable String parameter, int upper,
      boolean shiftIndex) {

    if (!StringUtils.hasText(parameter)) {
      return Optional.empty();
    }

    boolean shiftParameter = oneIndexedParameters && shiftIndex;

    try {
      int parsed = Integer.parseInt(parameter);
      parsed = shiftParameter ? parsed - 1 : parsed;
      parsed = parsed < 0 ? 0 : Math.min(parsed, upper);
      return Optional.of(parsed);
    } catch (NumberFormatException e) {
      return Optional.of(0);
    }
  }
}
