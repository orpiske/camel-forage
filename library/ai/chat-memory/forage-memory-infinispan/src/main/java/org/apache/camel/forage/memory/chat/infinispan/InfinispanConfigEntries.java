package org.apache.camel.forage.memory.chat.infinispan;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.camel.forage.core.util.config.ConfigEntries;
import org.apache.camel.forage.core.util.config.ConfigEntry;
import org.apache.camel.forage.core.util.config.ConfigModule;

public final class InfinispanConfigEntries extends ConfigEntries {
    public static final ConfigModule SERVER_LIST = ConfigModule.of(InfinispanConfig.class, "infinispan.server-list");
    public static final ConfigModule CACHE_NAME = ConfigModule.of(InfinispanConfig.class, "infinispan.cache-name");
    public static final ConfigModule USERNAME = ConfigModule.of(InfinispanConfig.class, "infinispan.username");
    public static final ConfigModule PASSWORD = ConfigModule.of(InfinispanConfig.class, "infinispan.password");
    public static final ConfigModule REALM = ConfigModule.of(InfinispanConfig.class, "infinispan.realm");
    public static final ConfigModule SASL_MECHANISM =
            ConfigModule.of(InfinispanConfig.class, "infinispan.sasl-mechanism");
    public static final ConfigModule CONNECTION_TIMEOUT =
            ConfigModule.of(InfinispanConfig.class, "infinispan.connection-timeout");
    public static final ConfigModule SOCKET_TIMEOUT =
            ConfigModule.of(InfinispanConfig.class, "infinispan.socket-timeout");
    public static final ConfigModule MAX_RETRIES = ConfigModule.of(InfinispanConfig.class, "infinispan.max-retries");
    public static final ConfigModule POOL_MAX_ACTIVE =
            ConfigModule.of(InfinispanConfig.class, "infinispan.pool.max-active");
    public static final ConfigModule POOL_MIN_IDLE =
            ConfigModule.of(InfinispanConfig.class, "infinispan.pool.min-idle");
    public static final ConfigModule POOL_MAX_WAIT =
            ConfigModule.of(InfinispanConfig.class, "infinispan.pool.max-wait");

    private static final Map<ConfigModule, ConfigEntry> CONFIG_MODULES = new ConcurrentHashMap<>();

    static {
        init();
    }

    static void init() {
        CONFIG_MODULES.put(SERVER_LIST, ConfigEntry.fromModule());
        CONFIG_MODULES.put(CACHE_NAME, ConfigEntry.fromModule());
        CONFIG_MODULES.put(USERNAME, ConfigEntry.fromModule());
        CONFIG_MODULES.put(PASSWORD, ConfigEntry.fromModule());
        CONFIG_MODULES.put(REALM, ConfigEntry.fromModule());
        CONFIG_MODULES.put(SASL_MECHANISM, ConfigEntry.fromModule());
        CONFIG_MODULES.put(CONNECTION_TIMEOUT, ConfigEntry.fromModule());
        CONFIG_MODULES.put(SOCKET_TIMEOUT, ConfigEntry.fromModule());
        CONFIG_MODULES.put(MAX_RETRIES, ConfigEntry.fromModule());
        CONFIG_MODULES.put(POOL_MAX_ACTIVE, ConfigEntry.fromModule());
        CONFIG_MODULES.put(POOL_MIN_IDLE, ConfigEntry.fromModule());
        CONFIG_MODULES.put(POOL_MAX_WAIT, ConfigEntry.fromModule());
    }

    public static Map<ConfigModule, ConfigEntry> entries() {
        return Collections.unmodifiableMap(CONFIG_MODULES);
    }

    public static Optional<ConfigModule> find(String prefix, String name) {
        return find(CONFIG_MODULES, prefix, name);
    }

    /**
     * Registers new known configuration if a prefix is provided (otherwise is ignored)
     * @param prefix the prefix to register
     */
    public static void register(String prefix) {
        if (prefix != null) {
            for (Map.Entry<ConfigModule, ConfigEntry> entry : entries().entrySet()) {
                ConfigModule configModule = entry.getKey().asNamed(prefix);
                CONFIG_MODULES.put(configModule, ConfigEntry.fromModule());
            }
        }
    }

    /**
     * Load override configurations (which are defined via environment variables and/or system properties)
     * @param prefix and optional prefix to use
     */
    public static void loadOverrides(String prefix) {
        load(CONFIG_MODULES, prefix);
    }
}
