package com.epam.esm.persistance.dao.support.audit;

import java.time.Instant;
import java.util.Optional;

public class InstantAuditingProvider implements AuditingProvider<Instant>{
  @Override
  public Optional<Instant> getCurrentAuditor() {
    return Optional.of(Instant.now());
  }
}
