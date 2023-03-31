package com.epam.esm.web.config.security;

import com.epam.esm.web.security.SecurityExpressionHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class SecurityExpressionConfig {

  @Bean
  SecurityExpressionHelper seh() {
    return new SecurityExpressionHelper(
        () -> SecurityContextHolder.getContext().getAuthentication());
  }
}
