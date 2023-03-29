package com.epam.esm.persistance.config;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;

public class StringAuditProvider implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of("test");
  }

}
