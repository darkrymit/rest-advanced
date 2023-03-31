package security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class JwtTokenTestConfig {
  @Bean
  @Order(2000)
  public SecurityFilterChain jwtRegisterFilterChain(HttpSecurity http,JwtDecoder jwtDecoder) throws Exception {
    // @formatter:off
    return http
        .oauth2ResourceServer(
            r -> r.jwt().decoder(jwtDecoder)
        )
        .build();
    // @formatter:on
  }
}
