package com.epam.esm.persistance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Profile("integration-test")
public class EmbeddedJdbcConfig {
  @Bean
  public EmbeddedDatabase embeddedDatabase() {
      return new EmbeddedDatabaseBuilder().generateUniqueName(true)
        .setType(EmbeddedDatabaseType.H2)
        .setScriptEncoding("UTF-8")
        .addDefaultScripts().build();
  }

  @Bean
  public JdbcTemplate jdbcTemplate(EmbeddedDatabase embeddedDatabase) {
    return new JdbcTemplate(embeddedDatabase);
  }

  @Bean
  PlatformTransactionManager transactionManager(EmbeddedDatabase embeddedDatabase) {
    return new DataSourceTransactionManager(embeddedDatabase);
  }
}
