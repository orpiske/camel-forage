package org.apache.camel.forage.vectordb.milvus;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.camel.forage.core.util.config.ConfigEntries;
import org.apache.camel.forage.core.util.config.ConfigEntry;
import org.apache.camel.forage.core.util.config.ConfigModule;

public final class MilvusConfigEntries extends ConfigEntries {
    public static final ConfigModule HOST = ConfigModule.of(MilvusConfig.class, "milvus.host");
    public static final ConfigModule PORT = ConfigModule.of(MilvusConfig.class, "milvus.port");
    public static final ConfigModule COLLECTION_NAME = ConfigModule.of(MilvusConfig.class, "milvus.collection.name");
    public static final ConfigModule DIMENSION = ConfigModule.of(MilvusConfig.class, "milvus.dimension");
    public static final ConfigModule INDEX_TYPE = ConfigModule.of(MilvusConfig.class, "milvus.index.type");
    public static final ConfigModule METRIC_TYPE = ConfigModule.of(MilvusConfig.class, "milvus.metric.type");
    public static final ConfigModule URI = ConfigModule.of(MilvusConfig.class, "milvus.uri");
    public static final ConfigModule TOKEN = ConfigModule.of(MilvusConfig.class, "milvus.token");
    public static final ConfigModule USERNAME = ConfigModule.of(MilvusConfig.class, "milvus.username");
    public static final ConfigModule PASSWORD = ConfigModule.of(MilvusConfig.class, "milvus.password");
    public static final ConfigModule CONSISTENCY_LEVEL =
            ConfigModule.of(MilvusConfig.class, "milvus.consistency.level");
    public static final ConfigModule RETRIEVE_EMBEDDINGS_ON_SEARCH =
            ConfigModule.of(MilvusConfig.class, "milvus.retrieve.embeddings.on.search");
    public static final ConfigModule AUTO_FLUSH_ON_INSERT =
            ConfigModule.of(MilvusConfig.class, "milvus.auto.flush.on.insert");
    public static final ConfigModule DATABASE_NAME = ConfigModule.of(MilvusConfig.class, "milvus.database.name");
    public static final ConfigModule ID_FIELD_NAME = ConfigModule.of(MilvusConfig.class, "milvus.id.field.name");
    public static final ConfigModule TEXT_FIELD_NAME = ConfigModule.of(MilvusConfig.class, "milvus.text.field.name");
    public static final ConfigModule METADATA_FIELD_NAME =
            ConfigModule.of(MilvusConfig.class, "milvus.metadata.field.name");
    public static final ConfigModule VECTOR_FIELD_NAME =
            ConfigModule.of(MilvusConfig.class, "milvus.vector.field.name");

    private static final Map<ConfigModule, ConfigEntry> CONFIG_MODULES = new ConcurrentHashMap<>();

    static {
        init();
    }

    static void init() {
        CONFIG_MODULES.put(HOST, ConfigEntry.fromModule());
        CONFIG_MODULES.put(PORT, ConfigEntry.fromModule());
        CONFIG_MODULES.put(COLLECTION_NAME, ConfigEntry.fromModule());
        CONFIG_MODULES.put(DIMENSION, ConfigEntry.fromModule());
        CONFIG_MODULES.put(INDEX_TYPE, ConfigEntry.fromModule());
        CONFIG_MODULES.put(METRIC_TYPE, ConfigEntry.fromModule());
        CONFIG_MODULES.put(URI, ConfigEntry.fromModule());
        CONFIG_MODULES.put(TOKEN, ConfigEntry.fromModule());
        CONFIG_MODULES.put(USERNAME, ConfigEntry.fromModule());
        CONFIG_MODULES.put(PASSWORD, ConfigEntry.fromModule());
        CONFIG_MODULES.put(CONSISTENCY_LEVEL, ConfigEntry.fromModule());
        CONFIG_MODULES.put(RETRIEVE_EMBEDDINGS_ON_SEARCH, ConfigEntry.fromModule());
        CONFIG_MODULES.put(AUTO_FLUSH_ON_INSERT, ConfigEntry.fromModule());
        CONFIG_MODULES.put(DATABASE_NAME, ConfigEntry.fromModule());
        CONFIG_MODULES.put(ID_FIELD_NAME, ConfigEntry.fromModule());
        CONFIG_MODULES.put(TEXT_FIELD_NAME, ConfigEntry.fromModule());
        CONFIG_MODULES.put(METADATA_FIELD_NAME, ConfigEntry.fromModule());
        CONFIG_MODULES.put(VECTOR_FIELD_NAME, ConfigEntry.fromModule());
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
