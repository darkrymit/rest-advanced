package com.epam.esm.web.config;

import com.epam.esm.persistance.dao.support.audit.InstantAuditingProvider;
import com.epam.esm.web.security.EmailAuditingProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditConfig {

  @Bean
  InstantAuditingProvider instantAuditingProvider(){
    return new InstantAuditingProvider();
  }

  @Bean
  EmailAuditingProvider emailAuditingProvider(){
    return new EmailAuditingProvider();
  }
}
