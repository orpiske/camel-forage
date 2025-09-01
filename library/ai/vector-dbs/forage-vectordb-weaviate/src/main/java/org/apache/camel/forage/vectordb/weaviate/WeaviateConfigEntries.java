package org.apache.camel.forage.vectordb.weaviate;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.camel.forage.core.util.config.ConfigEntries;
import org.apache.camel.forage.core.util.config.ConfigEntry;
import org.apache.camel.forage.core.util.config.ConfigModule;

public final class WeaviateConfigEntries extends ConfigEntries {
    public static final ConfigModule API_KEY = ConfigModule.of(WeaviateConfig.class, "weaviate.api.key");
    public static final ConfigModule SCHEME = ConfigModule.of(WeaviateConfig.class, "weaviate.scheme");
    public static final ConfigModule HOST = ConfigModule.of(WeaviateConfig.class, "weaviate.host");
    public static final ConfigModule PORT = ConfigModule.of(WeaviateConfig.class, "weaviate.port");
    public static final ConfigModule USE_GRPC_FOR_INSERTS =
            ConfigModule.of(WeaviateConfig.class, "weaviate.use.grpc.for.inserts");
    public static final ConfigModule SECURED_GRPC = ConfigModule.of(WeaviateConfig.class, "weaviate.secured.grpc");
    public static final ConfigModule GRPC_PORT = ConfigModule.of(WeaviateConfig.class, "weaviate.grpc.port");
    public static final ConfigModule OBJECT_CLASS = ConfigModule.of(WeaviateConfig.class, "weaviate.object.class");
    public static final ConfigModule AVOID_DUPS = ConfigModule.of(WeaviateConfig.class, "weaviate.avoid.dups");
    public static final ConfigModule CONSISTENCY_LEVEL =
            ConfigModule.of(WeaviateConfig.class, "weaviate.consistency.level");
    public static final ConfigModule METADATA_KEYS = ConfigModule.of(WeaviateConfig.class, "weaviate.metadata.keys");
    public static final ConfigModule TEXT_FIELD_NAME =
            ConfigModule.of(WeaviateConfig.class, "weaviate.text.field.name");
    public static final ConfigModule METADATA_FIELD_NAME =
            ConfigModule.of(WeaviateConfig.class, "weaviate.metadata.field.name");

    private static final Map<ConfigModule, ConfigEntry> CONFIG_MODULES = new ConcurrentHashMap<>();

    static {
        init();
    }

    static void init() {
        CONFIG_MODULES.put(API_KEY, ConfigEntry.fromModule());
        CONFIG_MODULES.put(SCHEME, ConfigEntry.fromModule());
        CONFIG_MODULES.put(HOST, ConfigEntry.fromModule());
        CONFIG_MODULES.put(PORT, ConfigEntry.fromModule());
        CONFIG_MODULES.put(USE_GRPC_FOR_INSERTS, ConfigEntry.fromModule());
        CONFIG_MODULES.put(SECURED_GRPC, ConfigEntry.fromModule());
        CONFIG_MODULES.put(GRPC_PORT, ConfigEntry.fromModule());
        CONFIG_MODULES.put(OBJECT_CLASS, ConfigEntry.fromModule());
        CONFIG_MODULES.put(AVOID_DUPS, ConfigEntry.fromModule());
        CONFIG_MODULES.put(CONSISTENCY_LEVEL, ConfigEntry.fromModule());
        CONFIG_MODULES.put(METADATA_KEYS, ConfigEntry.fromModule());
        CONFIG_MODULES.put(TEXT_FIELD_NAME, ConfigEntry.fromModule());
        CONFIG_MODULES.put(METADATA_FIELD_NAME, ConfigEntry.fromModule());
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
