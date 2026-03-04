package io.kaoto.forage.springboot.jdbc;

import javax.sql.DataSource;

import java.util.List;
import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import io.agroal.api.AgroalDataSource;
import io.kaoto.forage.core.annotations.FactoryType;
import io.kaoto.forage.core.annotations.FactoryVariant;
import io.kaoto.forage.core.annotations.ForageFactory;
import io.kaoto.forage.core.jdbc.DataSourceProvider;
import io.kaoto.forage.jdbc.common.DataSourceFactoryConfig;
import io.kaoto.forage.jdbc.common.ForageDataSource;
import io.kaoto.forage.jdbc.common.JdbcModuleDescriptor;
import io.kaoto.forage.jdbc.common.idempotent.ForageIdRepository;
import io.kaoto.forage.springboot.common.ForageSpringBootModuleAdapter;

/**
 * Auto-configuration for Forage DataSource creation using ServiceLoader discovery.
 * Automatically creates DataSource beans from JDBC configuration properties,
 * supporting both single and multi-instance (prefixed) configurations.
 *
 * <p>Named/prefixed datasources (e.g., {@code forage.ds1.jdbc.url}) are registered dynamically
 * by {@link ForageSpringBootModuleAdapter} using the {@link JdbcModuleDescriptor}.
 *
 * <p>This configuration class handles:
 * <ul>
 *   <li>Transaction management setup (when {@code forage.jdbc.transaction.enabled=true})</li>
 *   <li>The {@link ForageSpringBootModuleAdapter} bean for dynamic registration</li>
 *   <li>Fallback single-provider DataSource when no prefixed configurations are found</li>
 * </ul>
 */
@ForageFactory(
        value = "DataSource (Spring Boot)",
        components = {"camel-sql", "camel-jdbc", "camel-spring-jdbc"},
        description =
                "Auto-configured JDBC DataSource for Spring Boot with transaction management and repository support",
        type = FactoryType.DATA_SOURCE,
        autowired = true,
        configClass = DataSourceFactoryConfig.class,
        variant = FactoryVariant.SPRING_BOOT)
@Configuration
public class ForageDataSourceAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ForageDataSourceAutoConfiguration.class);

    /**
     * Transaction management configuration that enables Spring transaction support
     * when JDBC transactions are configured in Forage DataSource settings.
     */
    @Configuration
    @ConditionalOnProperty(value = "forage.jdbc.transaction.enabled", havingValue = "true")
    @EnableTransactionManagement
    static class ForageTransactionManagement {

        @jakarta.annotation.PostConstruct
        public void init() {
            log.info("ForageTransactionManagement configuration enabled");
        }
    }

    /**
     * Registers the generic module adapter that discovers prefixed DataSource
     * configurations and registers them as proper bean definitions using the
     * {@link JdbcModuleDescriptor}.
     */
    @Bean
    static ForageSpringBootModuleAdapter<DataSourceFactoryConfig, DataSourceProvider> forageJdbcModuleAdapter(
            Environment environment) {
        return new ForageSpringBootModuleAdapter<>(new JdbcModuleDescriptor(), environment);
    }

    /**
     * Fallback DataSource bean created when exactly one DataSourceProvider is on the classpath
     * and no named/prefixed configurations are found.
     */
    @Bean("dataSource")
    @ConditionalOnMissingBean(name = "dataSource")
    public DataSource forageDefaultDataSource() {
        List<ServiceLoader.Provider<DataSourceProvider>> providers =
                ServiceLoader.load(DataSourceProvider.class).stream().toList();
        if (providers.size() == 1) {
            log.info(
                    "Creating default DataSource using single provider: {}",
                    providers.get(0).type().getName());
            DataSourceProvider dsProvider = providers.get(0).get();
            AgroalDataSource dataSource = (AgroalDataSource) dsProvider.create(null);

            ForageIdRepository forageIdRepository = null;
            if (dsProvider instanceof ForageIdRepository forageIdRepo) {
                forageIdRepository = forageIdRepo;
            }

            ForageDataSource forageDataSource = new ForageDataSource(dataSource, forageIdRepository);

            // Register aggregation and idempotent repos for the default datasource
            DataSourceFactoryConfig config = new DataSourceFactoryConfig();
            log.info("Registered default DataSource bean");
            return forageDataSource.dataSource();
        }
        log.debug("No single DataSource provider found, skipping default DataSource creation");
        return null;
    }
}
