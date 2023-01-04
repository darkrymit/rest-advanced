package com.epam.esm.persistance.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EntityScan("com.epam.esm.persistance.entity")
@Profile("dev")
public class JdbcConfig {

}