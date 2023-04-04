package com.epam.esm.web.config;

import com.epam.esm.web.filter.OAuthUserDetailsCreateFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class OAuthUserDetailsConfig {

  @Bean
  FilterRegistrationBean<OAuthUserDetailsCreateFilter> myFilterRegistration(
      OAuthUserDetailsCreateFilter filter) {
    FilterRegistrationBean<OAuthUserDetailsCreateFilter> filterRegistrationBean = new FilterRegistrationBean<>();
    filterRegistrationBean.setFilter(filter);
    filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
    return filterRegistrationBean;
  }

}
