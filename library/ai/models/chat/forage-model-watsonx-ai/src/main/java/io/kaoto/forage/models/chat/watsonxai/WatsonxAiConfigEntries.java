package io.kaoto.forage.models.chat.watsonxai;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

public final class WatsonxAiConfigEntries extends ConfigEntries {
    public static final ConfigModule API_KEY = ConfigModule.of(
            WatsonxAiConfig.class,
            "forage.watsonxai.api.key",
            "IBM Cloud API key for authentication",
            "API Key",
            null,
            "password",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule URL = ConfigModule.of(
            WatsonxAiConfig.class,
            "forage.watsonxai.url",
            "The Watsonx.ai service URL (e.g., https://us-south.ml.cloud.ibm.com)",
            "Service URL",
            null,
            "string",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule PROJECT_ID = ConfigModule.of(
            WatsonxAiConfig.class,
            "forage.watsonxai.project.id",
            "The Watsonx.ai project ID",
            "Project ID",
            null,
            "string",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule MODEL_NAME = ConfigModule.of(
            WatsonxAiConfig.class,
            "forage.watsonxai.model.name",
            "The foundation model to use",
            "Model Name",
            "llama-3-405b-instruct",
            "string",
            false,
            ConfigTag.COMMON);
    public static final ConfigModule TEMPERATURE = ConfigModule.of(
            WatsonxAiConfig.class,
            "forage.watsonxai.temperature",
            "Temperature for response generation (0.0-2.0)",
            "Temperature",
            null,
            "double",
            false,
            ConfigTag.COMMON);
    public static final ConfigModule MAX_NEW_TOKENS = ConfigModule.of(
            WatsonxAiConfig.class,
            "forage.watsonxai.max.new.tokens",
            "Maximum number of new tokens in response",
            "Max New Tokens",
            null,
            "integer",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule TOP_P = ConfigModule.of(
            WatsonxAiConfig.class,
            "forage.watsonxai.top.p",
            "Top-p (nucleus sampling) parameter (0.0-1.0)",
            "Top P",
            null,
            "double",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule TOP_K = ConfigModule.of(
            WatsonxAiConfig.class,
            "forage.watsonxai.top.k",
            "Top-k sampling parameter",
            "Top K",
            null,
            "integer",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule RANDOM_SEED = ConfigModule.of(
            WatsonxAiConfig.class,
            "forage.watsonxai.random.seed",
            "Random seed for reproducible results",
            "Random Seed",
            null,
            "integer",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule REPETITION_PENALTY = ConfigModule.of(
            WatsonxAiConfig.class,
            "forage.watsonxai.repetition.penalty",
            "Penalty for repetition (1.0-2.0)",
            "Repetition Penalty",
            null,
            "double",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule MIN_NEW_TOKENS = ConfigModule.of(
            WatsonxAiConfig.class,
            "forage.watsonxai.min.new.tokens",
            "Minimum number of new tokens in response",
            "Min New Tokens",
            null,
            "integer",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule STOP_SEQUENCES = ConfigModule.of(
            WatsonxAiConfig.class,
            "forage.watsonxai.stop.sequences",
            "Stop sequences for response generation (comma-separated)",
            "Stop Sequences",
            null,
            "string",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule TIMEOUT = ConfigModule.of(
            WatsonxAiConfig.class,
            "forage.watsonxai.timeout",
            "Request timeout in seconds",
            "Timeout",
            null,
            "integer",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule MAX_RETRIES = ConfigModule.of(
            WatsonxAiConfig.class,
            "forage.watsonxai.max.retries",
            "Maximum number of retry attempts",
            "Max Retries",
            null,
            "integer",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule LOG_REQUESTS_AND_RESPONSES = ConfigModule.of(
            WatsonxAiConfig.class,
            "forage.watsonxai.log.requests.and.responses",
            "Enable request and response logging",
            "Log Requests/Responses",
            null,
            "boolean",
            false,
            ConfigTag.ADVANCED);

    static {
        initModules(
                WatsonxAiConfigEntries.class,
                API_KEY,
                URL,
                PROJECT_ID,
                MODEL_NAME,
                TEMPERATURE,
                MAX_NEW_TOKENS,
                TOP_P,
                TOP_K,
                RANDOM_SEED,
                REPETITION_PENALTY,
                MIN_NEW_TOKENS,
                STOP_SEQUENCES,
                TIMEOUT,
                MAX_RETRIES,
                LOG_REQUESTS_AND_RESPONSES);
    }
}
