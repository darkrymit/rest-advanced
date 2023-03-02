package com.epam.esm.persistance;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan("com.epam.esm.persistance.entity")
@ComponentScan("com.epam.esm.persistance.config")
public class TestApplication {

}