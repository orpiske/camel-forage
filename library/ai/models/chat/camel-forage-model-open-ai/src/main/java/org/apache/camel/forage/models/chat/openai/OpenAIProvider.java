package org.apache.camel.forage.models.chat.openai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.apache.camel.forage.core.ai.ModelProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provider for creating OpenAI chat models with configurable parameters.
 *
 * <p>This provider creates instances of {@link OpenAiChatModel} using configuration
 * values managed by {@link OpenAIConfig}. The configuration supports environment
 * variables, system properties, and configuration files for flexible deployment.
 *
 * <p><strong>Configuration:</strong>
 * <ul>
 *   <li>API Key: Configured via OPENAI_API_KEY environment variable (required)</li>
 *   <li>Model Name: Configured via OPENAI_MODEL_NAME environment variable or defaults to "gpt-3.5-turbo"</li>
 *   <li>Base URL: Optionally configured via OPENAI_BASE_URL environment variable (no default)</li>
 *   <li>Temperature: Optionally configured via OPENAI_TEMPERATURE environment variable (no default)</li>
 *   <li>Max Tokens: Optionally configured via OPENAI_MAX_TOKENS environment variable (no default)</li>
 *   <li>Top-P: Optionally configured via OPENAI_TOP_P environment variable (no default)</li>
 *   <li>Frequency Penalty: Optionally configured via OPENAI_FREQUENCY_PENALTY environment variable (no default)</li>
 *   <li>Presence Penalty: Optionally configured via OPENAI_PRESENCE_PENALTY environment variable (no default)</li>
 *   <li>Request Logging: Optionally configured via OPENAI_LOG_REQUESTS environment variable (no default)</li>
 *   <li>Response Logging: Optionally configured via OPENAI_LOG_RESPONSES environment variable (no default)</li>
 * </ul>
 *
 * <p><strong>Usage:</strong>
 * <pre>{@code
 * // Configuration is automatic through environment variables or defaults
 * OpenAIProvider provider = new OpenAIProvider();
 * ChatModel model = provider.newModel();
 * }</pre>
 *
 * @see OpenAIConfig
 * @see ModelProvider
 * @since 1.0
 */
public class OpenAIProvider implements ModelProvider {
    private static final Logger LOG = LoggerFactory.getLogger(OpenAIProvider.class);

    private final OpenAIConfig config;

    /**
     * Constructs a new OpenAIProvider with default configuration.
     *
     * <p>The configuration is automatically loaded from environment variables,
     * system properties, or configuration files during construction.
     */
    public OpenAIProvider() {
        this.config = new OpenAIConfig();
    }

    /**
     * Creates a new OpenAI chat model instance with the configured parameters.
     *
     * <p>This method creates an {@link OpenAiChatModel} using the API key, model name,
     * and other parameters from the configuration. The model is ready to use for chat
     * operations once created.
     *
     * @return a new configured OpenAI chat model instance
     */
    @Override
    public ChatModel newModel() {
        String apiKey = config.apiKey();
        String modelName = config.modelName();
        String baseUrl = config.baseUrl();
        Double temperature = config.temperature();
        Integer maxTokens = config.maxTokens();
        Double topP = config.topP();
        Double frequencyPenalty = config.frequencyPenalty();
        Double presencePenalty = config.presencePenalty();
        Boolean logRequests = config.logRequests();
        Boolean logResponses = config.logResponses();

        LOG.trace(
                "Creating OpenAI model: {} with configuration: baseUrl={}, temperature={}, maxTokens={}, topP={}, frequencyPenalty={}, presencePenalty={}, logRequests={}, logResponses={}",
                modelName,
                baseUrl,
                temperature,
                maxTokens,
                topP,
                frequencyPenalty,
                presencePenalty,
                logRequests,
                logResponses);

        OpenAiChatModel.OpenAiChatModelBuilder builder =
                OpenAiChatModel.builder().apiKey(apiKey).modelName(modelName);

        // Only set optional parameters if they are configured
        if (baseUrl != null) {
            builder.baseUrl(baseUrl);
        }

        if (temperature != null) {
            builder.temperature(temperature);
        }

        if (maxTokens != null) {
            builder.maxTokens(maxTokens);
        }

        if (topP != null) {
            builder.topP(topP);
        }

        if (frequencyPenalty != null) {
            builder.frequencyPenalty(frequencyPenalty);
        }

        if (presencePenalty != null) {
            builder.presencePenalty(presencePenalty);
        }

        if (logRequests != null) {
            builder.logRequests(logRequests);
        }

        if (logResponses != null) {
            builder.logResponses(logResponses);
        }

        return builder.build();
    }

    /**
     * Returns the configured API key for OpenAI.
     *
     * @return the API key, never null
     */
    public String getApiKey() {
        return config.apiKey();
    }

    /**
     * Returns the configured model name.
     *
     * @return the model name, never null
     */
    public String getModelName() {
        return config.modelName();
    }

    /**
     * Returns the configured base URL.
     *
     * @return the base URL, or null if not configured
     */
    public String getBaseUrl() {
        return config.baseUrl();
    }

    /**
     * Returns the configured temperature setting.
     *
     * @return the temperature value, or null if not configured
     */
    public Double getTemperature() {
        return config.temperature();
    }

    /**
     * Returns the configured maximum tokens setting.
     *
     * @return the max tokens value, or null if not configured
     */
    public Integer getMaxTokens() {
        return config.maxTokens();
    }

    /**
     * Returns the configured top-P sampling parameter.
     *
     * @return the top-P value, or null if not configured
     */
    public Double getTopP() {
        return config.topP();
    }

    /**
     * Returns the configured frequency penalty parameter.
     *
     * @return the frequency penalty value, or null if not configured
     */
    public Double getFrequencyPenalty() {
        return config.frequencyPenalty();
    }

    /**
     * Returns the configured presence penalty parameter.
     *
     * @return the presence penalty value, or null if not configured
     */
    public Double getPresencePenalty() {
        return config.presencePenalty();
    }

    /**
     * Returns whether request logging is enabled.
     *
     * @return true if enabled, false if disabled, null if not configured
     */
    public Boolean getLogRequests() {
        return config.logRequests();
    }

    /**
     * Returns whether response logging is enabled.
     *
     * @return true if enabled, false if disabled, null if not configured
     */
    public Boolean getLogResponses() {
        return config.logResponses();
    }
}
