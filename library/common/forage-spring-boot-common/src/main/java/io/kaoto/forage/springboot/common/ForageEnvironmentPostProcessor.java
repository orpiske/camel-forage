package io.kaoto.forage.springboot.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import io.kaoto.forage.core.util.config.ConfigStore;

/**
 * Spring Boot {@link EnvironmentPostProcessor} that bridges Forage's configuration system
 * with Spring's {@link org.springframework.core.env.Environment}.
 *
 * <p>This post-processor runs early in the Spring Boot lifecycle and performs two actions:
 * <ol>
 *   <li>Registers a {@link SpringConfigResolver} into {@link ConfigStore}, so that Forage's
 *       config system can resolve values from Spring's Environment (profiles, YAML, placeholders,
 *       Cloud Config, etc.)</li>
 *   <li>Adds a {@link ForagePropertySource} to Spring's Environment, so that standard Spring
 *       features ({@code @Value}, {@code @ConditionalOnProperty}) can access Forage properties</li>
 * </ol>
 *
 * <p>Registered via {@code META-INF/spring/org.springframework.boot.env.EnvironmentPostProcessor.imports}.
 *
 * @since 1.1
 */
public class ForageEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ForageEnvironmentPostProcessor.class);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        LOG.info("Registering Forage Spring Boot integration: SpringConfigResolver and ForagePropertySource");

        // 1. Register SpringConfigResolver so ConfigStore can read from Spring Environment
        ConfigStore.getInstance().registerResolver(new SpringConfigResolver(environment));

        // 2. Add ForagePropertySource so Spring can see forage.* properties
        if (!environment.getPropertySources().contains(ForagePropertySource.FORAGE_PROPERTY_SOURCE_NAME)) {
            environment.getPropertySources().addLast(new ForagePropertySource());
        }
    }
}
