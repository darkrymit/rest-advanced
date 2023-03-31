package com.epam.esm.web.config;

import com.epam.esm.web.filter.OAuthUserDetailsCreateFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class OAuthUserDetailsConfig {

  @Bean
  @Order(3000)
  public SecurityFilterChain oAuthUserDetailsRegisterFilterChain(HttpSecurity http, OAuthUserDetailsCreateFilter filter)
      throws Exception {
    return http.addFilterAfter(filter, BearerTokenAuthenticationFilter.class).build();
  }

}
