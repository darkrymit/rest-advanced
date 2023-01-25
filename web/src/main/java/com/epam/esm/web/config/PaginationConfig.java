package com.epam.esm.web.config;

import com.epam.esm.web.dto.assembler.PagedResourcesAssembler;
import com.epam.esm.web.resolver.PageableHandlerMethodArgumentResolver;
import com.epam.esm.web.resolver.SortHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaginationConfig {

  @Bean
  SortHandlerMethodArgumentResolver customSortHandlerMethodArgumentResolver(){
    return new SortHandlerMethodArgumentResolver();
  }

  @Bean
  PageableHandlerMethodArgumentResolver customPageableHandlerMethodArgumentResolver(SortHandlerMethodArgumentResolver sortHandlerMethodArgumentResolver){
    return new PageableHandlerMethodArgumentResolver(sortHandlerMethodArgumentResolver);
  }

  @Bean
  PagedResourcesAssembler<?> generalPagedResourcesAssembler(PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver){
    return new PagedResourcesAssembler<>(pageableHandlerMethodArgumentResolver);
  }
}
