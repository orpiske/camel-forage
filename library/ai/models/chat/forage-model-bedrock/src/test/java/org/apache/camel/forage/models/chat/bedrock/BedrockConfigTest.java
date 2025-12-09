package org.apache.camel.forage.models.chat.bedrock;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive tests for BedrockConfig focusing on configuration loading mechanisms.
 */
@DisplayName("BedrockConfig Configuration Loading Tests")
class BedrockConfigTest {

    @Test
    @DisplayName("Should load region from system property")
    void shouldLoadRegionFromSystemProperty() {
        System.setProperty("bedrock.region", "us-west-2");

        BedrockConfig config = new BedrockConfig();

        assertThat(config.region()).isEqualTo("us-west-2");
    }

    @Test
    @DisplayName("Should use default region when not configured")
    void shouldUseDefaultRegionWhenNotConfigured() {
        BedrockConfig config = new BedrockConfig();

        assertThat(config.region()).isEqualTo("us-east-1");
    }

    @Test
    @DisplayName("Should load model ID from system property")
    void shouldLoadModelIdFromSystemProperty() {
        System.setProperty("bedrock.model.id", "anthropic.claude-3-5-sonnet-20240620-v1:0");

        BedrockConfig config = new BedrockConfig();

        assertThat(config.modelId()).isEqualTo("anthropic.claude-3-5-sonnet-20240620-v1:0");
    }

    @Test
    @DisplayName("Should load AWS credentials from system properties")
    void shouldLoadAwsCredentialsFromSystemProperties() {
        System.setProperty("bedrock.access.key.id", "AKIAIOSFODNN7EXAMPLE");
        System.setProperty("bedrock.secret.access.key", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");

        BedrockConfig config = new BedrockConfig();

        assertThat(config.accessKeyId()).isEqualTo("AKIAIOSFODNN7EXAMPLE");
        assertThat(config.secretAccessKey()).isEqualTo("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
    }

    @Test
    @DisplayName("Should return null for optional credentials when not configured")
    void shouldReturnNullForOptionalCredentialsWhenNotConfigured() {
        BedrockConfig config = new BedrockConfig();

        assertThat(config.accessKeyId()).isNull();
        assertThat(config.secretAccessKey()).isNull();
    }

    @Test
    @DisplayName("Should load all optional parameters from system properties")
    void shouldLoadOptionalParametersFromSystemProperties() {
        System.setProperty("bedrock.temperature", "0.7");
        System.setProperty("bedrock.max.tokens", "2048");
        System.setProperty("bedrock.top.p", "0.9");

        BedrockConfig config = new BedrockConfig();

        assertThat(config.temperature()).isEqualTo(0.7);
        assertThat(config.maxTokens()).isEqualTo(2048);
        assertThat(config.topP()).isEqualTo(0.9);
    }

    @Test
    @DisplayName("Should return null for optional parameters when not configured")
    void shouldReturnNullForOptionalParametersWhenNotConfigured() {
        BedrockConfig config = new BedrockConfig();

        assertThat(config.temperature()).isNull();
        assertThat(config.maxTokens()).isNull();
        assertThat(config.topP()).isNull();
    }
}
