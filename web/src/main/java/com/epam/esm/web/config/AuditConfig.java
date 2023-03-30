package com.epam.esm.web.config;

import java.security.Principal;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class AuditConfig {

  @Bean
  AuditorAware<String> idAuditingProvider() {
    return () -> Optional.of(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication).map(Principal::getName);
  }

}
