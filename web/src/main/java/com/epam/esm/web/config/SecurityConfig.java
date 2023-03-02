package com.epam.esm.web.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.epam.esm.web.filter.OAuthUserDetailsCreateFilter;
import com.epam.esm.web.security.SecurityExpressionHelper;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.JSONArray;
import java.util.stream.Collectors;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {

  OAuthUserDetailsCreateFilter oAuthUserDetailsCreateFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // @formatter:off
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeRequests()
          .mvcMatchers(HttpMethod.GET,"/certificates/*","/certificates")
          .permitAll()
          .anyRequest()
          .authenticated()
        .and()
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .oauth2ResourceServer(
            r -> r.jwt().jwtAuthenticationConverter(jwtAuthenticationTokenConverter())
        )
        .addFilterAfter(oAuthUserDetailsCreateFilter,BearerTokenAuthenticationFilter.class)
        .build();
    // @formatter:on
  }

  @Bean
  public OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager(
      ClientRegistrationRepository clientRegistrationRepository,
      OAuth2AuthorizedClientRepository auth2AuthorizedClientRepository) {
    OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder()
        .authorizationCode().refreshToken().clientCredentials().build();

    DefaultOAuth2AuthorizedClientManager defaultOAuth2AuthorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
        clientRegistrationRepository, auth2AuthorizedClientRepository);
    defaultOAuth2AuthorizedClientManager.setAuthorizedClientProvider(provider);

    return defaultOAuth2AuthorizedClientManager;
  }

  @Bean
  WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
    ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
        authorizedClientManager);
    oauth2Client.setDefaultClientRegistrationId("keycloak");
    return WebClient.builder().baseUrl("http://localhost:8080/admin/realms/oauth2-realm")
        .apply(oauth2Client.oauth2Configuration()).build();
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

  @Bean
  SecurityExpressionHelper seh() {
    return new SecurityExpressionHelper(
        () -> SecurityContextHolder.getContext().getAuthentication());
  }
}
