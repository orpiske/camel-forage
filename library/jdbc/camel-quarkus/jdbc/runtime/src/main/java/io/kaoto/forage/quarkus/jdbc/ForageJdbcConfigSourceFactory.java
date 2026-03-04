package io.kaoto.forage.quarkus.jdbc;

import io.kaoto.forage.core.common.ForageModuleDescriptor;
import io.kaoto.forage.core.common.ForageQuarkusConfigSourceAdapter;
import io.kaoto.forage.jdbc.common.DataSourceFactoryConfig;
import io.kaoto.forage.jdbc.common.JdbcModuleDescriptor;

/**
 * SmallRye {@link io.smallrye.config.ConfigSourceFactory} that translates Forage JDBC properties
 * into Quarkus datasource properties at config bootstrap time.
 *
 * <p>Delegates all logic to {@link ForageQuarkusConfigSourceAdapter} using the {@link JdbcModuleDescriptor}.
 *
 * <p>Registered via {@code META-INF/services/io.smallrye.config.ConfigSourceFactory}.
 *
 * @since 1.1
 */
public class ForageJdbcConfigSourceFactory extends ForageQuarkusConfigSourceAdapter<DataSourceFactoryConfig> {

    @Override
    protected ForageModuleDescriptor<DataSourceFactoryConfig, ?> descriptor() {
        return new JdbcModuleDescriptor();
    }
}
