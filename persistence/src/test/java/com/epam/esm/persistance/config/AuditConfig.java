package com.epam.esm.persistance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("integration-test")
public class AuditConfig {

  @Bean
  StringAuditProvider stringAuditProvider(){
    return new StringAuditProvider();
  }

}
