package io.kaoto.forage.springboot.common;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
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
        if (!(environment instanceof ConfigurableEnvironment configurableEnv)) {
            return Collections.emptySet();
        }

        Pattern pattern = Pattern.compile(regexp);
        return StreamSupport.stream(configurableEnv.getPropertySources().spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource<?>)
                .flatMap(ps -> {
                    String[] names = ((EnumerablePropertySource<?>) ps).getPropertyNames();
                    return java.util.Arrays.stream(names);
                })
                .map(key -> {
                    Matcher m = pattern.matcher(key);
                    if (m.find()) {
                        return m.group(1);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public int priority() {
        return 10;
    }
}
