package io.kaoto.forage.jms.common;

import io.kaoto.forage.core.util.config.AbstractConfig;

import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.BLOCK_IF_FULL;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.BLOCK_IF_FULL_TIMEOUT_MILLIS;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.BROKER_URL;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.CLIENT_ID;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.CONNECTION_TIMEOUT_MILLIS;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.EXPIRY_TIMEOUT_MILLIS;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.IDLE_TIMEOUT_MILLIS;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.JMS_KIND;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.MAX_CONNECTIONS;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.MAX_SESSIONS_PER_CONNECTION;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.PASSWORD;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.POOL_ENABLED;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.TRANSACTION_ENABLED;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.TRANSACTION_ENABLE_RECOVERY;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.TRANSACTION_EXPIRY_SCANNERS;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.TRANSACTION_NODE_ID;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.TRANSACTION_OBJECT_STORE_DIRECTORY;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.TRANSACTION_OBJECT_STORE_ID;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.TRANSACTION_OBJECT_STORE_TYPE;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.TRANSACTION_RECOVERY_MODULES;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.TRANSACTION_TIMEOUT_SECONDS;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.TRANSACTION_XA_RESOURCE_ORPHAN_FILTERS;
import static io.kaoto.forage.jms.common.ConnectionFactoryConfigEntries.USERNAME;

/**
 * Configuration for JMS connection factory with broker connection settings and pool parameters.
 */
public class ConnectionFactoryConfig extends AbstractConfig {

    public ConnectionFactoryConfig() {
        this(null);
    }

    public ConnectionFactoryConfig(String prefix) {
        super(prefix, ConnectionFactoryConfigEntries.class);
    }

    @Override
    public String name() {
        return "forage-connectionfactory";
    }

    // JMS connection methods
    public String jmsKind() {
        return getRequired(JMS_KIND, "JMS kind is required but not configured");
    }

    public String brokerUrl() {
        return getRequired(BROKER_URL, "Broker URL is required but not configured");
    }

    public String username() {
        return get(USERNAME).orElse(null);
    }

    public String password() {
        return get(PASSWORD).orElse(null);
    }

    public String clientId() {
        return get(CLIENT_ID).orElse(null);
    }

    // Connection pool configuration methods
    public boolean poolEnabled() {
        return get(POOL_ENABLED).map(Boolean::parseBoolean).orElse(Boolean.parseBoolean(POOL_ENABLED.defaultValue()));
    }

    public int maxConnections() {
        return get(MAX_CONNECTIONS).map(Integer::parseInt).orElse(Integer.parseInt(MAX_CONNECTIONS.defaultValue()));
    }

    public int maxSessionsPerConnection() {
        return get(MAX_SESSIONS_PER_CONNECTION)
                .map(Integer::parseInt)
                .orElse(Integer.parseInt(MAX_SESSIONS_PER_CONNECTION.defaultValue()));
    }

    public long idleTimeoutMillis() {
        return get(IDLE_TIMEOUT_MILLIS).map(Long::parseLong).orElse(Long.parseLong(IDLE_TIMEOUT_MILLIS.defaultValue()));
    }

    public long expiryTimeoutMillis() {
        return get(EXPIRY_TIMEOUT_MILLIS)
                .map(Long::parseLong)
                .orElse(Long.parseLong(EXPIRY_TIMEOUT_MILLIS.defaultValue()));
    }

    // todo this property is not used, should we remove it?
    public long connectionTimeoutMillis() {
        return get(CONNECTION_TIMEOUT_MILLIS)
                .map(Long::parseLong)
                .orElse(Long.parseLong(CONNECTION_TIMEOUT_MILLIS.defaultValue()));
    }

    public boolean blockIfFull() {
        return get(BLOCK_IF_FULL).map(Boolean::parseBoolean).orElse(Boolean.parseBoolean(BLOCK_IF_FULL.defaultValue()));
    }

    public long blockIfFullTimeoutMillis() {
        return get(BLOCK_IF_FULL_TIMEOUT_MILLIS)
                .map(Long::parseLong)
                .orElse(Long.parseLong(BLOCK_IF_FULL_TIMEOUT_MILLIS.defaultValue()));
    }

    // Transaction configuration methods
    public boolean transactionEnabled() {
        return get(TRANSACTION_ENABLED)
                .map(Boolean::parseBoolean)
                .orElse(Boolean.parseBoolean(TRANSACTION_ENABLED.defaultValue()));
    }

    public int transactionTimeoutSeconds() {
        return get(TRANSACTION_TIMEOUT_SECONDS)
                .map(Integer::parseInt)
                .orElse(Integer.parseInt(TRANSACTION_TIMEOUT_SECONDS.defaultValue()));
    }

    public String transactionNodeId() {
        return get(TRANSACTION_NODE_ID).orElse(null);
    }

    public String transactionObjectStoreId() {
        return get(TRANSACTION_OBJECT_STORE_ID).orElse(null);
    }

    public boolean transactionEnableRecovery() {
        return get(TRANSACTION_ENABLE_RECOVERY)
                .map(Boolean::parseBoolean)
                .orElse(Boolean.parseBoolean(TRANSACTION_ENABLE_RECOVERY.defaultValue()));
    }

    public String transactionRecoveryModules() {
        return get(TRANSACTION_RECOVERY_MODULES).orElse(TRANSACTION_RECOVERY_MODULES.defaultValue());
    }

    public String transactionExpiryScanners() {
        return get(TRANSACTION_EXPIRY_SCANNERS).orElse(TRANSACTION_EXPIRY_SCANNERS.defaultValue());
    }

    public String transactionXaResourceOrphanFilters() {
        return get(TRANSACTION_XA_RESOURCE_ORPHAN_FILTERS)
                .orElse(TRANSACTION_XA_RESOURCE_ORPHAN_FILTERS.defaultValue());
    }

    public String transactionObjectStoreDirectory() {
        return get(TRANSACTION_OBJECT_STORE_DIRECTORY).orElse(TRANSACTION_OBJECT_STORE_DIRECTORY.defaultValue());
    }

    public String transactionObjectStoreType() {
        return get(TRANSACTION_OBJECT_STORE_TYPE).orElse(TRANSACTION_OBJECT_STORE_TYPE.defaultValue());
    }
}
