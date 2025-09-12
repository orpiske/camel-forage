package org.apache.camel.forage.models.chat.watsonxai;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.camel.forage.core.util.config.ConfigEntries;
import org.apache.camel.forage.core.util.config.ConfigEntry;
import org.apache.camel.forage.core.util.config.ConfigModule;

public final class WatsonxAiConfigEntries extends ConfigEntries {
    public static final ConfigModule API_KEY = ConfigModule.of(WatsonxAiConfig.class, "watsonxai.api.key");
    public static final ConfigModule URL = ConfigModule.of(WatsonxAiConfig.class, "watsonxai.url");
    public static final ConfigModule PROJECT_ID = ConfigModule.of(WatsonxAiConfig.class, "watsonxai.project.id");
    public static final ConfigModule MODEL_NAME = ConfigModule.of(WatsonxAiConfig.class, "watsonxai.model.name");
    public static final ConfigModule TEMPERATURE = ConfigModule.of(WatsonxAiConfig.class, "watsonxai.temperature");
    public static final ConfigModule MAX_NEW_TOKENS =
            ConfigModule.of(WatsonxAiConfig.class, "watsonxai.max.new.tokens");
    public static final ConfigModule TOP_P = ConfigModule.of(WatsonxAiConfig.class, "watsonxai.top.p");
    public static final ConfigModule TOP_K = ConfigModule.of(WatsonxAiConfig.class, "watsonxai.top.k");
    public static final ConfigModule RANDOM_SEED = ConfigModule.of(WatsonxAiConfig.class, "watsonxai.random.seed");
    public static final ConfigModule REPETITION_PENALTY =
            ConfigModule.of(WatsonxAiConfig.class, "watsonxai.repetition.penalty");
    public static final ConfigModule MIN_NEW_TOKENS =
            ConfigModule.of(WatsonxAiConfig.class, "watsonxai.min.new.tokens");
    public static final ConfigModule STOP_SEQUENCES =
            ConfigModule.of(WatsonxAiConfig.class, "watsonxai.stop.sequences");
    public static final ConfigModule TIMEOUT = ConfigModule.of(WatsonxAiConfig.class, "watsonxai.timeout");
    public static final ConfigModule MAX_RETRIES = ConfigModule.of(WatsonxAiConfig.class, "watsonxai.max.retries");
    public static final ConfigModule LOG_REQUESTS_AND_RESPONSES =
            ConfigModule.of(WatsonxAiConfig.class, "watsonxai.log.requests.and.responses");

    private static final Map<ConfigModule, ConfigEntry> CONFIG_MODULES = new ConcurrentHashMap<>();

    static {
        init();
    }

    static void init() {
        CONFIG_MODULES.put(API_KEY, ConfigEntry.fromModule());
        CONFIG_MODULES.put(URL, ConfigEntry.fromModule());
        CONFIG_MODULES.put(PROJECT_ID, ConfigEntry.fromModule());
        CONFIG_MODULES.put(MODEL_NAME, ConfigEntry.fromModule());
        CONFIG_MODULES.put(TEMPERATURE, ConfigEntry.fromModule());
        CONFIG_MODULES.put(MAX_NEW_TOKENS, ConfigEntry.fromModule());
        CONFIG_MODULES.put(TOP_P, ConfigEntry.fromModule());
        CONFIG_MODULES.put(TOP_K, ConfigEntry.fromModule());
        CONFIG_MODULES.put(RANDOM_SEED, ConfigEntry.fromModule());
        CONFIG_MODULES.put(REPETITION_PENALTY, ConfigEntry.fromModule());
        CONFIG_MODULES.put(MIN_NEW_TOKENS, ConfigEntry.fromModule());
        CONFIG_MODULES.put(STOP_SEQUENCES, ConfigEntry.fromModule());
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
