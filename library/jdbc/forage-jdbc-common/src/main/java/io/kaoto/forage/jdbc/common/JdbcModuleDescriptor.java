package io.kaoto.forage.jdbc.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import io.agroal.api.AgroalDataSource;
import io.kaoto.forage.core.common.AuxiliaryBeanDescriptor;
import io.kaoto.forage.core.common.ForageModuleDescriptor;
import io.kaoto.forage.core.common.ServiceLoaderHelper;
import io.kaoto.forage.core.jdbc.DataSourceProvider;
import io.kaoto.forage.jdbc.common.aggregation.ForageAggregationRepository;
import io.kaoto.forage.jdbc.common.idempotent.ForageIdRepository;
import io.kaoto.forage.jdbc.common.idempotent.ForageJdbcMessageIdRepository;

/**
 * Module descriptor for Forage JDBC. Captures all JDBC-specific knowledge:
 * prefix discovery, provider resolution, Quarkus property translation, and auxiliary bean creation.
 *
 * @since 1.1
 */
public class JdbcModuleDescriptor implements ForageModuleDescriptor<DataSourceFactoryConfig, DataSourceProvider> {

    @Override
    public String modulePrefix() {
        return "jdbc";
    }

    @Override
    public DataSourceFactoryConfig createConfig(String prefix) {
        return prefix == null ? new DataSourceFactoryConfig() : new DataSourceFactoryConfig(prefix);
    }

    @Override
    public Class<DataSourceProvider> providerClass() {
        return DataSourceProvider.class;
    }

    @Override
    public String resolveProviderClassName(DataSourceFactoryConfig config) {
        return DataSourceCommonExportHelper.transformDbKindIntoProviderClass(config.dbKind());
    }

    @Override
    public String defaultBeanName() {
        return "dataSource";
    }

    @Override
    public Class<?> primaryBeanClass() {
        return AgroalDataSource.class;
    }

    @Override
    public boolean transactionEnabled(DataSourceFactoryConfig config) {
        return config.transactionEnabled();
    }

    @Override
    public Map<String, String> translateProperties(String prefix, DataSourceFactoryConfig config) {
        Map<String, String> props = new HashMap<>();

        String quarkusPrefix = "quarkus.datasource.";
        String effectivePrefix = prefix != null ? prefix : defaultBeanName();
        quarkusPrefix += "\"" + effectivePrefix + "\".";

        props.put(quarkusPrefix + "db-kind", config.dbKind());
        props.put(quarkusPrefix + "password", config.password());
        props.put(quarkusPrefix + "username", config.username());
        props.put(quarkusPrefix + "jdbc.url", config.jdbcUrl());
        props.put(quarkusPrefix + "jdbc.initial-size", String.valueOf(config.initialSize()));
        props.put(quarkusPrefix + "jdbc.min-size", String.valueOf(config.minSize()));
        props.put(quarkusPrefix + "jdbc.max-size", String.valueOf(config.minSize()));
        props.put(quarkusPrefix + "jdbc.acquisition-timeout", config.acquisitionTimeoutSeconds() + "S");
        props.put(quarkusPrefix + "jdbc.validation-query-timeout", config.validationTimeoutSeconds() + "S");
        props.put(quarkusPrefix + "jdbc.leak-detection-interval", config.leakTimeoutMinutes() + "M");

        if (config.transactionEnabled()) {
            props.put(quarkusPrefix + "jdbc.transaction-isolation-level", "READ_COMMITTED");
            props.put(
                    "quarkus.transaction-manager.default-transaction-timeout",
                    config.transactionTimeoutSeconds() + "S");
            if (config.transactionNodeId() != null) {
                props.put("quarkus.transaction-manager.node-name", config.transactionNodeId());
            }
            props.put(
                    "quarkus.transaction-manager.enable-recovery", String.valueOf(config.transactionEnableRecovery()));
            props.put("quarkus.transaction-manager.recovery-modules", config.transactionRecoveryModules());
            props.put(
                    "quarkus.transaction-manager.xa-resource-orphan-filters",
                    config.transactionXaResourceOrphanFilters());
            props.put("quarkus.transaction-manager.object-store.directory", config.transactionObjectStoreDirectory());
            props.put("quarkus.transaction-manager.object-store.type", config.transactionObjectStoreType());
            if (config.transactionObjectStoreDataSource() != null) {
                props.put(
                        "quarkus.transaction-manager.object-store.datasource",
                        config.transactionObjectStoreDataSource());
            }
            props.put(
                    "quarkus.transaction-manager.object-store.drop-table",
                    String.valueOf(config.transactionObjectStoreDropTable()));
            props.put(
                    "quarkus.transaction-manager.object-store.table-prefix",
                    config.transactionObjectStoreTablePrefix());
        }

        return props;
    }

    @Override
    public List<AuxiliaryBeanDescriptor> auxiliaryBeans(String prefix) {
        DataSourceFactoryConfig config = createConfig(prefix);
        List<AuxiliaryBeanDescriptor> beans = new ArrayList<>();

        if (config.transactionEnabled() && config.aggregationRepositoryName() != null) {
            String repoName = config.aggregationRepositoryName();
            beans.add(new AuxiliaryBeanDescriptor(repoName, ForageAggregationRepository.class, () -> {
                DataSourceFactoryConfig c = createConfig(prefix);
                ForageDataSource ds = createDataSource(c, prefix);
                if (ds != null) {
                    return new ForageAggregationRepository(
                            ds.dataSource(), com.arjuna.ats.jta.TransactionManager.transactionManager(), c);
                }
                return null;
            }));
        }

        if (config.enableIdempotentRepository()) {
            String tableName = config.idempotentRepositoryTableName();
            beans.add(new AuxiliaryBeanDescriptor(tableName, ForageJdbcMessageIdRepository.class, () -> {
                DataSourceFactoryConfig c = createConfig(prefix);
                ForageDataSource ds = createDataSource(c, prefix);
                if (ds != null) {
                    return new ForageJdbcMessageIdRepository(c, ds.dataSource(), ds.forageIdRepository());
                }
                return null;
            }));
        }

        return beans;
    }

    /**
     * Creates a DataSource using ServiceLoader to find the appropriate provider.
     */
    ForageDataSource createDataSource(DataSourceFactoryConfig config, String name) {
        String providerClass = resolveProviderClassName(config);
        List<ServiceLoader.Provider<DataSourceProvider>> providers =
                ServiceLoader.load(DataSourceProvider.class).stream().toList();

        ServiceLoader.Provider<DataSourceProvider> provider;
        if (providers.size() == 1) {
            provider = providers.get(0);
        } else {
            provider = ServiceLoaderHelper.findProviderByClassName(providers, providerClass);
        }

        if (provider == null) {
            return null;
        }

        DataSourceProvider dsProvider = provider.get();
        AgroalDataSource dataSource = (AgroalDataSource) dsProvider.create(name);

        ForageIdRepository forageIdRepository = null;
        if (dsProvider instanceof ForageIdRepository forageIdRepo) {
            forageIdRepository = forageIdRepo;
        }

        return new ForageDataSource(dataSource, forageIdRepository);
    }
}
