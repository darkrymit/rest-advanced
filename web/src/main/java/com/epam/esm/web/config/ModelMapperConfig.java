package com.epam.esm.web.config;

import java.time.Instant;
import java.time.ZonedDateTime;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

@Configuration
public class ModelMapperConfig {

  @Bean
  public ModelMapper modelMapper(Converter<Instant, ZonedDateTime> instantZonedDateTimeConverter) {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setFieldMatchingEnabled(true)
        .setFieldAccessLevel(AccessLevel.PRIVATE);

    modelMapper.typeMap(Instant.class, ZonedDateTime.class)
        .setConverter(instantZonedDateTimeConverter);

    return modelMapper;
  }

  @Bean
  public Converter<Instant, ZonedDateTime> instantZonedDateTimeConverter() {
    return mappingContext -> mappingContext.getSource()
        .atZone(LocaleContextHolder.getTimeZone().toZoneId());
  }
}
