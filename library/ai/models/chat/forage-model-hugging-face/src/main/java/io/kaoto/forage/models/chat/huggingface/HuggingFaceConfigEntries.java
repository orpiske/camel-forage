package io.kaoto.forage.models.chat.huggingface;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

public final class HuggingFaceConfigEntries extends ConfigEntries {
    public static final ConfigModule API_KEY = ConfigModule.of(
            HuggingFaceConfig.class,
            "forage.huggingface.api.key",
            "HuggingFace API key for authentication",
            "API Key",
            null,
            "password",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule MODEL_ID = ConfigModule.of(
            HuggingFaceConfig.class,
            "forage.huggingface.model.id",
            "The HuggingFace model ID to use (e.g., microsoft/DialoGPT-medium)",
            "Model ID",
            null,
            "string",
            false,
            ConfigTag.COMMON);
    public static final ConfigModule TEMPERATURE = ConfigModule.of(
            HuggingFaceConfig.class,
            "forage.huggingface.temperature",
            "Temperature for response generation (0.0-2.0)",
            "Temperature",
            null,
            "double",
            false,
            ConfigTag.COMMON);
    public static final ConfigModule MAX_NEW_TOKENS = ConfigModule.of(
            HuggingFaceConfig.class,
            "forage.huggingface.max.new.tokens",
            "Maximum number of new tokens to generate",
            "Max New Tokens",
            null,
            "integer",
            false,
            ConfigTag.COMMON);
    public static final ConfigModule TOP_K = ConfigModule.of(
            HuggingFaceConfig.class,
            "forage.huggingface.top.k",
            "Top-k sampling parameter",
            "Top K",
            null,
            "integer",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule TOP_P = ConfigModule.of(
            HuggingFaceConfig.class,
            "forage.huggingface.top.p",
            "Top-p (nucleus) sampling parameter",
            "Top P",
            null,
            "double",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule DO_SAMPLE = ConfigModule.of(
            HuggingFaceConfig.class,
            "forage.huggingface.do.sample",
            "Whether to use sampling for text generation",
            "Do Sample",
            null,
            "boolean",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule REPETITION_PENALTY = ConfigModule.of(
            HuggingFaceConfig.class,
            "forage.huggingface.repetition.penalty",
            "Penalty for repeating tokens (1.0-2.0)",
            "Repetition Penalty",
            null,
            "double",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule RETURN_FULL_TEXT = ConfigModule.of(
            HuggingFaceConfig.class,
            "forage.huggingface.return.full.text",
            "Whether to return full text including input",
            "Return Full Text",
            null,
            "boolean",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule WAIT_FOR_MODEL = ConfigModule.of(
            HuggingFaceConfig.class,
            "forage.huggingface.wait.for.model",
            "Whether to wait for the model to load if it's not ready",
            "Wait for Model",
            null,
            "boolean",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule TIMEOUT = ConfigModule.of(
            HuggingFaceConfig.class,
            "forage.huggingface.timeout",
            "Request timeout in seconds",
            "Timeout",
            null,
            "integer",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule MAX_RETRIES = ConfigModule.of(
            HuggingFaceConfig.class,
            "forage.huggingface.max.retries",
            "Maximum number of retry attempts",
            "Max Retries",
            null,
            "integer",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule LOG_REQUESTS_AND_RESPONSES = ConfigModule.of(
            HuggingFaceConfig.class,
            "forage.huggingface.log.requests.and.responses",
            "Enable request and response logging",
            "Log Requests/Responses",
            null,
            "boolean",
            false,
            ConfigTag.ADVANCED);

    static {
        initModules(
                HuggingFaceConfigEntries.class,
                API_KEY,
                MODEL_ID,
                TEMPERATURE,
                MAX_NEW_TOKENS,
                TOP_K,
                TOP_P,
                DO_SAMPLE,
                REPETITION_PENALTY,
                RETURN_FULL_TEXT,
                WAIT_FOR_MODEL,
                TIMEOUT,
                MAX_RETRIES,
                LOG_REQUESTS_AND_RESPONSES);
    }
}
