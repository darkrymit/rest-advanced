package com.epam.esm.web.config.security;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
      Converter<Jwt, AbstractAuthenticationToken> converter, JwtDecoder decoder,
      CorsConfigurationSource corsConfigurationSource)
      throws Exception {
    // @formatter:off
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource))
        .authorizeRequests()
        .mvcMatchers(HttpMethod.GET,"/certificates/*","/certificates")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .oauth2ResourceServer(
            r -> r.jwt().decoder(decoder).jwtAuthenticationConverter(converter)
        )
        .build();
    // @formatter:on
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration cors = new CorsConfiguration().applyPermitDefaultValues();
    source.registerCorsConfiguration("/**", cors);
    return source;
  }
}
