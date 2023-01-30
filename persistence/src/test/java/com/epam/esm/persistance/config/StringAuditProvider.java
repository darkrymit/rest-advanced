package com.epam.esm.persistance.config;

import com.epam.esm.persistance.dao.support.audit.AuditingProvider;
import java.util.Optional;

public class StringAuditProvider implements AuditingProvider<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of("test");
  }

}
