package org.apache.camel.forage.models.chat.huggingface;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.camel.forage.core.util.config.ConfigEntries;
import org.apache.camel.forage.core.util.config.ConfigEntry;
import org.apache.camel.forage.core.util.config.ConfigModule;

public final class HuggingFaceConfigEntries extends ConfigEntries {
    public static final ConfigModule API_KEY = ConfigModule.of(HuggingFaceConfig.class, "huggingface.api.key");
    public static final ConfigModule MODEL_ID = ConfigModule.of(HuggingFaceConfig.class, "huggingface.model.id");
    public static final ConfigModule TEMPERATURE = ConfigModule.of(HuggingFaceConfig.class, "huggingface.temperature");
    public static final ConfigModule MAX_NEW_TOKENS =
            ConfigModule.of(HuggingFaceConfig.class, "huggingface.max.new.tokens");
    public static final ConfigModule TOP_K = ConfigModule.of(HuggingFaceConfig.class, "huggingface.top.k");
    public static final ConfigModule TOP_P = ConfigModule.of(HuggingFaceConfig.class, "huggingface.top.p");
    public static final ConfigModule DO_SAMPLE = ConfigModule.of(HuggingFaceConfig.class, "huggingface.do.sample");
    public static final ConfigModule REPETITION_PENALTY =
            ConfigModule.of(HuggingFaceConfig.class, "huggingface.repetition.penalty");
    public static final ConfigModule RETURN_FULL_TEXT =
            ConfigModule.of(HuggingFaceConfig.class, "huggingface.return.full.text");
    public static final ConfigModule WAIT_FOR_MODEL =
            ConfigModule.of(HuggingFaceConfig.class, "huggingface.wait.for.model");
    public static final ConfigModule TIMEOUT = ConfigModule.of(HuggingFaceConfig.class, "huggingface.timeout");
    public static final ConfigModule MAX_RETRIES = ConfigModule.of(HuggingFaceConfig.class, "huggingface.max.retries");
    public static final ConfigModule LOG_REQUESTS_AND_RESPONSES =
            ConfigModule.of(HuggingFaceConfig.class, "huggingface.log.requests.and.responses");

    private static final Map<ConfigModule, ConfigEntry> CONFIG_MODULES = new ConcurrentHashMap<>();

    static {
        init();
    }

    static void init() {
        CONFIG_MODULES.put(API_KEY, ConfigEntry.fromModule());
        CONFIG_MODULES.put(MODEL_ID, ConfigEntry.fromModule());
        CONFIG_MODULES.put(TEMPERATURE, ConfigEntry.fromModule());
        CONFIG_MODULES.put(MAX_NEW_TOKENS, ConfigEntry.fromModule());
        CONFIG_MODULES.put(TOP_K, ConfigEntry.fromModule());
        CONFIG_MODULES.put(TOP_P, ConfigEntry.fromModule());
        CONFIG_MODULES.put(DO_SAMPLE, ConfigEntry.fromModule());
        CONFIG_MODULES.put(REPETITION_PENALTY, ConfigEntry.fromModule());
        CONFIG_MODULES.put(RETURN_FULL_TEXT, ConfigEntry.fromModule());
        CONFIG_MODULES.put(WAIT_FOR_MODEL, ConfigEntry.fromModule());
        CONFIG_MODULES.put(TIMEOUT, ConfigEntry.fromModule());
        CONFIG_MODULES.put(MAX_RETRIES, ConfigEntry.fromModule());
        CONFIG_MODULES.put(LOG_REQUESTS_AND_RESPONSES, ConfigEntry.fromModule());
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
