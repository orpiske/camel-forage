package io.kaoto.forage.models.embeddings.ollama;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

public final class OllamaEmbeddingConfigEntries extends ConfigEntries {
    public static final ConfigModule BASE_URL = ConfigModule.of(
            OllamaEmbedddingConfig.class,
            "forage.ollama.embedding.model.base.url",
            "The base URL of the Ollama server",
            "Base URL",
            "http://localhost:11434",
            "string",
            false,
            ConfigTag.COMMON);
    public static final ConfigModule MODEL_NAME = ConfigModule.of(
            OllamaEmbedddingConfig.class,
            "forage.ollama.embedding.model.name",
            "The Ollama model to use",
            "Model Name",
            "llama3",
            "string",
            false,
            ConfigTag.COMMON);
    public static final ConfigModule TIMEOUT = ConfigModule.of(
            OllamaEmbedddingConfig.class,
            "forage.ollama.embedding.model.timeout",
            "Used for the HttpClientBuilder that will be used to communicate with Ollama",
            "Timeout",
            null,
            "Duration",
            false,
            ConfigTag.COMMON);
    public static final ConfigModule MAX_RETRIES = ConfigModule.of(
            OllamaEmbedddingConfig.class,
            "forage.ollama.embedding.model.max.retries",
            "Used for the HttpClientBuilder that will be used to communicate with Ollama",
            "Max retries",
            null,
            "int",
            false,
            ConfigTag.COMMON);
    public static final ConfigModule LOG_REQUESTS = ConfigModule.of(
            OllamaEmbedddingConfig.class,
            "forage.ollama.embedding.model.log.requests",
            "Enable request logging",
            "Log Requests",
            null,
            "boolean",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule LOG_RESPONSES = ConfigModule.of(
            OllamaEmbedddingConfig.class,
            "forage.ollama.embedding.model.log.responses",
            "Enable response logging",
            "Log Responses",
            null,
            "boolean",
            false,
            ConfigTag.ADVANCED);

    static {
        initModules(
                OllamaEmbeddingConfigEntries.class,
                BASE_URL,
                MODEL_NAME,
                TIMEOUT,
                MAX_RETRIES,
                LOG_REQUESTS,
                LOG_RESPONSES);
    }
}
