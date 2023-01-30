package com.epam.esm.persistance.config;

import com.epam.esm.persistance.dao.support.audit.InstantAuditingProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("integration-test")
public class AuditConfig {

  @Bean
  InstantAuditingProvider instantAuditingProvider(){
    return new InstantAuditingProvider();
  }

  @Bean
  StringAuditProvider stringAuditProvider(){
    return new StringAuditProvider();
  }

}
