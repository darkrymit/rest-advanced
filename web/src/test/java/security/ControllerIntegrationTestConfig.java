package security;

import com.epam.esm.web.config.HateoasConfig;
import com.epam.esm.web.config.ModelMapperConfig;
import com.epam.esm.web.config.ValidationConfig;
import com.epam.esm.web.config.language.LocaleConfig;
import com.epam.esm.web.config.security.SecurityConfig;
import com.epam.esm.web.exceptions.handler.GeneralControllerAdvice;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({SecurityConfig.class,HateoasConfig.class,
    ModelMapperConfig.class, JwtTestConfig.class, JwtTokenTestConfig.class,
    GeneralControllerAdvice.class, LocaleConfig.class, ValidationConfig.class})
@EnableAutoConfiguration
public class ControllerIntegrationTestConfig {

}
