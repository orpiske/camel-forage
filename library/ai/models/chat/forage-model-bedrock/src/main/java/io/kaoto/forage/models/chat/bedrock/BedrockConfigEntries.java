package io.kaoto.forage.models.chat.bedrock;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

public final class BedrockConfigEntries extends ConfigEntries {
    public static final ConfigModule REGION = ConfigModule.of(
            BedrockConfig.class,
            "forage.bedrock.region",
            "AWS region where Bedrock is available",
            "Region",
            "us-east-1",
            "string",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule MODEL_ID = ConfigModule.of(
            BedrockConfig.class,
            "forage.bedrock.model.id",
            "Bedrock model identifier (e.g., anthropic.claude-3-5-sonnet-20240620-v1:0)",
            "Model ID",
            null,
            "string",
            false,
            ConfigTag.COMMON);
    public static final ConfigModule ACCESS_KEY_ID = ConfigModule.of(
            BedrockConfig.class,
            "forage.bedrock.access.key.id",
            "AWS access key ID (optional, uses default credential chain if not provided)",
            "Access Key ID",
            null,
            "password",
            true,
            ConfigTag.SECURITY);
    public static final ConfigModule SECRET_ACCESS_KEY = ConfigModule.of(
            BedrockConfig.class,
            "forage.bedrock.secret.access.key",
            "AWS secret access key (optional, uses default credential chain if not provided)",
            "Secret Access Key",
            null,
            "password",
            true,
            ConfigTag.SECURITY);
    public static final ConfigModule TEMPERATURE = ConfigModule.of(
            BedrockConfig.class,
            "forage.bedrock.temperature",
            "Sampling temperature for response randomness (0.0-1.0)",
            "Temperature",
            null,
            "double",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule MAX_TOKENS = ConfigModule.of(
            BedrockConfig.class,
            "forage.bedrock.max.tokens",
            "Maximum number of tokens to generate",
            "Max Tokens",
            null,
            "integer",
            true,
            ConfigTag.ADVANCED);
    public static final ConfigModule TOP_P = ConfigModule.of(
            BedrockConfig.class,
            "forage.bedrock.top.p",
            "Top-P (nucleus) sampling parameter (0.0-1.0)",
            "Top P",
            null,
            "double",
            true,
            ConfigTag.ADVANCED);

    static {
        initModules(
                BedrockConfigEntries.class,
                REGION,
                MODEL_ID,
                ACCESS_KEY_ID,
                SECRET_ACCESS_KEY,
                TEMPERATURE,
                MAX_TOKENS,
                TOP_P);
    }
}
