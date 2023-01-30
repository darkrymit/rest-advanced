package com.epam.esm.persistance.dao.support.audit;

import java.util.Optional;

@FunctionalInterface
public interface AuditingProvider<T> {
  Optional<T> getCurrentAuditor();
}
