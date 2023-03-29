package com.epam.esm.web.config;

import com.epam.esm.web.security.EmailAuditingProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditConfig {

  @Bean
  EmailAuditingProvider emailAuditingProvider(){
    return new EmailAuditingProvider();
  }
}
