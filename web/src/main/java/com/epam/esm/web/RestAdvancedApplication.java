package com.epam.esm.web;

import com.epam.esm.web.config.KeycloakWebClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
@EnableConfigurationProperties(KeycloakWebClientProperties.class)
public class RestAdvancedApplication {

  public static void main(String[] args) {
    SpringApplication.run(RestAdvancedApplication.class, args);
  }

}
