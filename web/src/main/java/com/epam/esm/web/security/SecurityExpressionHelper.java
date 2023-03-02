package com.epam.esm.web.security;

import java.util.UUID;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@AllArgsConstructor
@Slf4j
public class SecurityExpressionHelper {

  private final Supplier<Authentication> authenticationSupplier;

  public boolean hasSameId(UUID id){
    log.info("esx");
    if (id == null){
      return false;
    }
    JwtAuthenticationToken authentication = (JwtAuthenticationToken) authenticationSupplier.get();
    return UUID.fromString(authentication.getName()).equals(id);
  }

}
