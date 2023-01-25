package com.epam.esm.web.config.spring;

import com.epam.esm.web.resolver.PageableHandlerMethodArgumentResolver;
import com.epam.esm.web.resolver.SortHandlerMethodArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final SortHandlerMethodArgumentResolver sortHandlerMethodArgumentResolver;

  private final PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver;


  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(sortHandlerMethodArgumentResolver);
    resolvers.add(pageableHandlerMethodArgumentResolver);
  }
}