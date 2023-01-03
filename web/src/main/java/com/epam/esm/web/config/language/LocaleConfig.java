package com.epam.esm.web.config.language;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;


@Configuration
@Profile("dev")
public class LocaleConfig {

  private static final List<Locale> LOCALES = Arrays.asList(Locale.ENGLISH,
      Locale.forLanguageTag("ua"));

  @Bean
  AcceptHeaderLocaleResolver acceptHeaderLocaleResolver() {
    AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
    resolver.setSupportedLocales(LOCALES);
    return resolver;
  }

  @Bean
  public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setUseCodeAsDefaultMessage(true);
    return messageSource;
  }
}
