package io.kaoto.forage.springboot.common;

import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import io.kaoto.forage.core.util.config.ConfigResolver;

/**
 * Spring Boot {@link ConfigResolver} that delegates property resolution to Spring's {@link Environment}.
 *
 * <p>This resolver wraps {@code environment.getProperty()} calls, enabling Forage's {@code ConfigStore}
 * to transparently read from all Spring property sources: profiles, YAML, {@code ${...}} placeholders,
 * config imports, and Spring Cloud Config.
 *
 * <p>Priority is set to 10 (higher than {@code DefaultConfigResolver} at 0), so Spring Environment
 * values take precedence over Forage's default resolution (env vars, system props, properties files).
 *
 * @since 1.1
 */
public class SpringConfigResolver implements ConfigResolver {

    private static final Logger LOG = LoggerFactory.getLogger(SpringConfigResolver.class);

    private final Environment environment;

    public SpringConfigResolver(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Optional<String> resolve(String propertyName) {
        String value = environment.getProperty(propertyName);
        if (value != null) {
            LOG.debug("Resolved '{}' from Spring Environment", propertyName);
            return Optional.of(value);
        }
        return Optional.empty();
    }

    @Override
    public Set<String> discoverPrefixes(String regexp) {
        return SpringPropertyHelper.discoverPrefixes(environment, regexp);
    }

    @Override
    public int priority() {
        return 10;
    }
}
