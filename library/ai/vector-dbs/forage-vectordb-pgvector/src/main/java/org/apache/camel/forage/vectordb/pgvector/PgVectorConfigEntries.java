package org.apache.camel.forage.vectordb.pgvector;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.camel.forage.core.util.config.ConfigEntries;
import org.apache.camel.forage.core.util.config.ConfigEntry;
import org.apache.camel.forage.core.util.config.ConfigModule;

public final class PgVectorConfigEntries extends ConfigEntries {
    public static final ConfigModule HOST = ConfigModule.of(PgVectorConfig.class, "pgvector.host");
    public static final ConfigModule PORT = ConfigModule.of(PgVectorConfig.class, "pgvector.port");
    public static final ConfigModule USER = ConfigModule.of(PgVectorConfig.class, "pgvector.user");
    public static final ConfigModule PASSWORD = ConfigModule.of(PgVectorConfig.class, "pgvector.password");
    public static final ConfigModule DATABASE = ConfigModule.of(PgVectorConfig.class, "pgvector.database");
    public static final ConfigModule TABLE = ConfigModule.of(PgVectorConfig.class, "pgvector.table");
    public static final ConfigModule DIMENSION = ConfigModule.of(PgVectorConfig.class, "pgvector.dimension");
    public static final ConfigModule USE_INDEX = ConfigModule.of(PgVectorConfig.class, "pgvector.use.index");
    public static final ConfigModule INDEX_LIST_SIZE =
            ConfigModule.of(PgVectorConfig.class, "pgvector.index.list.size");
    public static final ConfigModule CREATE_TABLE = ConfigModule.of(PgVectorConfig.class, "pgvector.create.table");
    public static final ConfigModule DROP_TABLE_FIRST =
            ConfigModule.of(PgVectorConfig.class, "pgvector.drop.table.first");
    public static final ConfigModule METADATA_STORAGE_CONFIG =
            ConfigModule.of(PgVectorConfig.class, "pgvector.metadata.storage.config");

    private static final Map<ConfigModule, ConfigEntry> CONFIG_MODULES = new ConcurrentHashMap<>();

    static {
        init();
    }

    static void init() {
        CONFIG_MODULES.put(HOST, ConfigEntry.fromModule());
        CONFIG_MODULES.put(PORT, ConfigEntry.fromModule());
        CONFIG_MODULES.put(USER, ConfigEntry.fromModule());
        CONFIG_MODULES.put(PASSWORD, ConfigEntry.fromModule());
        CONFIG_MODULES.put(DATABASE, ConfigEntry.fromModule());
        CONFIG_MODULES.put(TABLE, ConfigEntry.fromModule());
        CONFIG_MODULES.put(DIMENSION, ConfigEntry.fromModule());
        CONFIG_MODULES.put(USE_INDEX, ConfigEntry.fromModule());
        CONFIG_MODULES.put(INDEX_LIST_SIZE, ConfigEntry.fromModule());
        CONFIG_MODULES.put(CREATE_TABLE, ConfigEntry.fromModule());
        CONFIG_MODULES.put(DROP_TABLE_FIRST, ConfigEntry.fromModule());
        CONFIG_MODULES.put(METADATA_STORAGE_CONFIG, ConfigEntry.fromModule());
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
