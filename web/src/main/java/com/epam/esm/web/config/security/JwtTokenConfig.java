package com.epam.esm.web.config.security;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@RequiredArgsConstructor
public class JwtTokenConfig {

  @Bean
  @Order(2000)
  public SecurityFilterChain jwtRegisterFilterChain(HttpSecurity http,Converter<Jwt, AbstractAuthenticationToken> converter) throws Exception {
    return http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .oauth2ResourceServer(
            r -> r.jwt().jwtAuthenticationConverter(converter)).build();
  }

  @Bean
  Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationTokenConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
      JSONArray roles = (JSONArray) ((JSONObject) jwt.getClaim("realm_access")).get("roles");
      return roles.stream().map(String::valueOf).map(s -> new SimpleGrantedAuthority("ROLE_" + s))
          .collect(Collectors.toList());
    });
    return converter;
  }
}
