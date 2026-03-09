package io.kaoto.forage.quarkus.jdbc.deployment;

import io.kaoto.forage.jdbc.common.DataSourceFactoryConfig;
import io.quarkus.builder.item.MultiBuildItem;

/**
 * Build item carrying a discovered Forage JDBC datasource prefix and its configuration.
 *
 * <p>This build item is produced by a discovery build step and consumed by subsequent
 * build steps that create aggregation/idempotent repository beans, replacing the need
 * to access {@link io.kaoto.forage.core.util.config.ConfigStore} directly from build steps.
 *
 * @since 1.1
 */
public final class ForageDataSourceBuildItem extends MultiBuildItem {

    private final String name;
    private final String prefix;
    private final DataSourceFactoryConfig config;

    public ForageDataSourceBuildItem(String name, String prefix, DataSourceFactoryConfig config) {
        this.name = name;
        this.prefix = prefix;
        this.config = config;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns the configuration prefix used to create the config, or null for default (unprefixed) config.
     * This value is safe to pass to Quarkus recorders (it is a simple String).
     */
    public String getPrefix() {
        return prefix;
    }

    public DataSourceFactoryConfig getConfig() {
        return config;
    }
}
