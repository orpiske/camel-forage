package org.apache.camel.forage.memory.chat.redis;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.camel.forage.core.util.config.ConfigEntries;
import org.apache.camel.forage.core.util.config.ConfigEntry;
import org.apache.camel.forage.core.util.config.ConfigModule;

public final class RedisConfigEntries extends ConfigEntries {
    public static final ConfigModule HOST = ConfigModule.of(RedisConfig.class, "redis.host");
    public static final ConfigModule PORT = ConfigModule.of(RedisConfig.class, "redis.port");
    public static final ConfigModule PASSWORD = ConfigModule.of(RedisConfig.class, "redis.password");
    public static final ConfigModule DATABASE = ConfigModule.of(RedisConfig.class, "redis.database");
    public static final ConfigModule TIMEOUT = ConfigModule.of(RedisConfig.class, "redis.timeout");
    public static final ConfigModule POOL_MAX_TOTAL = ConfigModule.of(RedisConfig.class, "redis.pool.max-total");
    public static final ConfigModule POOL_MAX_IDLE = ConfigModule.of(RedisConfig.class, "redis.pool.max-idle");
    public static final ConfigModule POOL_MIN_IDLE = ConfigModule.of(RedisConfig.class, "redis.pool.min-idle");
    public static final ConfigModule POOL_TEST_ON_BORROW =
            ConfigModule.of(RedisConfig.class, "redis.pool.test-on-borrow");
    public static final ConfigModule POOL_TEST_ON_RETURN =
            ConfigModule.of(RedisConfig.class, "redis.pool.test-on-return");
    public static final ConfigModule POOL_TEST_WHILE_IDLE =
            ConfigModule.of(RedisConfig.class, "redis.pool.test-while-idle");
    public static final ConfigModule POOL_MAX_WAIT_MILLIS =
            ConfigModule.of(RedisConfig.class, "redis.pool.max-wait-millis");

    private static final Map<ConfigModule, ConfigEntry> CONFIG_MODULES = new ConcurrentHashMap<>();

    static {
        init();
    }

    static void init() {
        CONFIG_MODULES.put(HOST, ConfigEntry.fromModule());
        CONFIG_MODULES.put(PORT, ConfigEntry.fromModule());
        CONFIG_MODULES.put(PASSWORD, ConfigEntry.fromModule());
        CONFIG_MODULES.put(DATABASE, ConfigEntry.fromModule());
        CONFIG_MODULES.put(TIMEOUT, ConfigEntry.fromModule());
        CONFIG_MODULES.put(POOL_MAX_TOTAL, ConfigEntry.fromModule());
        CONFIG_MODULES.put(POOL_MAX_IDLE, ConfigEntry.fromModule());
        CONFIG_MODULES.put(POOL_MIN_IDLE, ConfigEntry.fromModule());
        CONFIG_MODULES.put(POOL_TEST_ON_BORROW, ConfigEntry.fromModule());
        CONFIG_MODULES.put(POOL_TEST_ON_RETURN, ConfigEntry.fromModule());
        CONFIG_MODULES.put(POOL_TEST_WHILE_IDLE, ConfigEntry.fromModule());
        CONFIG_MODULES.put(POOL_MAX_WAIT_MILLIS, ConfigEntry.fromModule());
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
