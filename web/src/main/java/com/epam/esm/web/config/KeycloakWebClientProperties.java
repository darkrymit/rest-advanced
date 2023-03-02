package com.epam.esm.web.config;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "keycloak.webclient")
@Value
public class KeycloakWebClientProperties {
    String url;

    String registrationId;
}