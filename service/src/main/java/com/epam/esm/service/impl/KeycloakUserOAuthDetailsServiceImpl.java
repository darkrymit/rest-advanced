package com.epam.esm.service.impl;

import com.epam.esm.service.UserOAuthDetails;
import com.epam.esm.service.UserOAuthDetailsService;
import com.epam.esm.service.exceptions.NoSuchUserOAuthDetailsException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class KeycloakUserOAuthDetailsServiceImpl implements UserOAuthDetailsService {

  private final WebClient webClient;

  public UserOAuthDetails getById(UUID id) throws NoSuchUserOAuthDetailsException {
    return webClient
        .get()
        .uri("/users/{id}",id.toString())
        .retrieve()
        .bodyToMono(UserOAuthDetails.class)
        .blockOptional()
        .orElseThrow(() -> new NoSuchUserOAuthDetailsException(id));
  }


}
