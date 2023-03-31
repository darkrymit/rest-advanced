package security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

@Component
public class WithCustomJwtTokenSecurityContextFactory implements
    WithSecurityContextFactory<WithCustomJwtToken> {

  private final JwtEncoder jwtEncoder;

  public WithCustomJwtTokenSecurityContextFactory(JwtEncoder jwtEncoder) {
    this.jwtEncoder = jwtEncoder;
  }

  @Override
  public SecurityContext createSecurityContext(WithCustomJwtToken annotation) {

    Instant now = Instant.now();

    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("http://localhost:8080/realms/oauth2-realm")
        .issuedAt(now)
        .expiresAt(now.plus(2, ChronoUnit.MINUTES))
        .subject("e80113ae-bfc8-4673-befd-732197da81cd")
        .claim("scope", "scope")
        .build();


    Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));

    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

    Authentication authentication = new JwtAuthenticationToken(jwt, List.of(()->"ROLE_User"));

    securityContext.setAuthentication(authentication);

    return securityContext;
  }


}
