package io.kaoto.forage.springboot.jdbc;

import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import io.kaoto.forage.core.jdbc.DataSourceProvider;

/**
 * Spring {@link Condition} that matches only when exactly one {@link DataSourceProvider}
 * is available via {@link ServiceLoader}.
 *
 * <p>This prevents the fallback {@code forageDefaultDataSource()} bean from being registered
 * when multiple providers are on the classpath (e.g., postgresql + mysql), which would result
 * in a null bean that breaks Spring Boot Actuator's DataSource health checks.
 *
 * @since 1.1
 */
class SingleDataSourceProviderCondition implements Condition {

    private static final Logger LOG = LoggerFactory.getLogger(SingleDataSourceProviderCondition.class);

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        long count = ServiceLoader.load(DataSourceProvider.class).stream().count();
        if (count == 1) {
            LOG.debug("Single DataSourceProvider found, fallback DataSource bean will be registered");
            return true;
        }
        LOG.debug(
                "Found {} DataSourceProvider(s), skipping fallback DataSource bean (use prefixed configuration instead)",
                count);
        return false;
    }
}
