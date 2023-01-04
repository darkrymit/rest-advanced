package com.epam.esm.persistance.config;

import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EntityScan("com.epam.esm.persistance.entity")
@Profile("integration-test")
public class EmbeddedDatabaseJpaConfig {
  @Bean
  public EmbeddedDatabase embeddedDatabase() {
      return new EmbeddedDatabaseBuilder().generateUniqueName(true)
        .setType(EmbeddedDatabaseType.H2)
        .setScriptEncoding("UTF-8")
        .addDefaultScripts().build();
  }

  @Bean
  public EntityManager entityManager(LocalSessionFactoryBean sessionFactory) {
    SessionFactory localSessionFactoryBean = sessionFactory.getObject();
    assert localSessionFactoryBean != null;
    return localSessionFactoryBean.createEntityManager();
  }


  @Bean
  public LocalSessionFactoryBean sessionFactory(@Autowired EmbeddedDatabase embeddedDatabase) {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(embeddedDatabase);
    sessionFactory.setPackagesToScan("com.epam.esm.persistance.entity");
    sessionFactory.setHibernateProperties(hibernateProperties());
    return sessionFactory;
  }

  private Properties hibernateProperties() {
    Properties hibernateProperties = new Properties();
    hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "none");
    hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    return hibernateProperties;
  }

  @Bean
  public PlatformTransactionManager platformTransactionManager(EntityManagerFactory emf){
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(emf);
    return transactionManager;
  }
}
