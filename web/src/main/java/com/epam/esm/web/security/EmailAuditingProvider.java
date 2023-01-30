package com.epam.esm.web.security;

import com.epam.esm.persistance.dao.support.audit.AuditingProvider;
import java.security.Principal;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class EmailAuditingProvider implements AuditingProvider<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .map(Principal::getName);
  }
}
