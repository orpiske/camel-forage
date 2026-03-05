package io.kaoto.forage.vectordb.chroma;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

public final class ChromaConfigEntries extends ConfigEntries {
    public static final ConfigModule URL = ConfigModule.of(
            ChromaConfig.class,
            "forage.chroma.url",
            "The URL of the Chroma server",
            "URL",
            null,
            "string",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule COLLECTION_NAME = ConfigModule.of(
            ChromaConfig.class,
            "forage.chroma.collection.name",
            "The name of the collection to use",
            "Collection Name",
            null,
            "string",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule TIMEOUT = ConfigModule.of(
            ChromaConfig.class,
            "forage.chroma.timeout",
            "Request timeout in seconds",
            "Timeout",
            "5",
            "integer",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule LOG_REQUESTS = ConfigModule.of(
            ChromaConfig.class,
            "forage.chroma.log.requests",
            "Enable request logging",
            "Log Requests",
            "true",
            "boolean",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule LOG_RESPONSES = ConfigModule.of(
            ChromaConfig.class,
            "forage.chroma.log.responses",
            "Enable response logging",
            "Log Responses",
            "true",
            "boolean",
            false,
            ConfigTag.ADVANCED);

    static {
        initModules(ChromaConfigEntries.class, URL, COLLECTION_NAME, TIMEOUT, LOG_REQUESTS, LOG_RESPONSES);
    }
}
