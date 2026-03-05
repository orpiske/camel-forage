package io.kaoto.forage.models.chat.watsonxai;

import java.util.Arrays;
import java.util.List;
import io.kaoto.forage.core.util.config.AbstractConfig;

import static io.kaoto.forage.models.chat.watsonxai.WatsonxAiConfigEntries.API_KEY;
import static io.kaoto.forage.models.chat.watsonxai.WatsonxAiConfigEntries.LOG_REQUESTS_AND_RESPONSES;
import static io.kaoto.forage.models.chat.watsonxai.WatsonxAiConfigEntries.MAX_NEW_TOKENS;
import static io.kaoto.forage.models.chat.watsonxai.WatsonxAiConfigEntries.MAX_RETRIES;
import static io.kaoto.forage.models.chat.watsonxai.WatsonxAiConfigEntries.MIN_NEW_TOKENS;
import static io.kaoto.forage.models.chat.watsonxai.WatsonxAiConfigEntries.MODEL_NAME;
import static io.kaoto.forage.models.chat.watsonxai.WatsonxAiConfigEntries.PROJECT_ID;
import static io.kaoto.forage.models.chat.watsonxai.WatsonxAiConfigEntries.RANDOM_SEED;
import static io.kaoto.forage.models.chat.watsonxai.WatsonxAiConfigEntries.REPETITION_PENALTY;
import static io.kaoto.forage.models.chat.watsonxai.WatsonxAiConfigEntries.STOP_SEQUENCES;
import static io.kaoto.forage.models.chat.watsonxai.WatsonxAiConfigEntries.TEMPERATURE;
import static io.kaoto.forage.models.chat.watsonxai.WatsonxAiConfigEntries.TIMEOUT;
import static io.kaoto.forage.models.chat.watsonxai.WatsonxAiConfigEntries.TOP_K;
import static io.kaoto.forage.models.chat.watsonxai.WatsonxAiConfigEntries.TOP_P;
import static io.kaoto.forage.models.chat.watsonxai.WatsonxAiConfigEntries.URL;

/**
 * Configuration class for IBM Watsonx.ai integration in the Forage framework.
 *
 * <p>This configuration class manages the settings required to connect to and use IBM Watsonx.ai
 * services. Watsonx.ai is IBM's enterprise AI platform that provides foundation models and
 * AI services for building AI applications.
 *
 * <p><strong>Required Configuration:</strong>
 * <ul>
 *   <li><strong>WATSONXAI_API_KEY</strong> - The IBM Cloud API key</li>
 *   <li><strong>WATSONXAI_URL</strong> - The Watsonx.ai service URL</li>
 *   <li><strong>WATSONXAI_PROJECT_ID</strong> - The Watsonx.ai project ID</li>
 * </ul>
 *
 * <p><strong>Optional Configuration:</strong>
 * <ul>
 *   <li><strong>WATSONXAI_MODEL_NAME</strong> - The model to use (defaults to "llama-3-405b-instruct")</li>
 *   <li><strong>WATSONXAI_TEMPERATURE</strong> - Controls randomness (0.0-2.0)</li>
 *   <li><strong>WATSONXAI_MAX_NEW_TOKENS</strong> - Maximum new tokens in response</li>
 *   <li><strong>WATSONXAI_TOP_P</strong> - Nucleus sampling parameter (0.0-1.0)</li>
 *   <li><strong>WATSONXAI_TOP_K</strong> - Top-k sampling parameter</li>
 *   <li><strong>WATSONXAI_RANDOM_SEED</strong> - Random seed for reproducible results</li>
 *   <li><strong>WATSONXAI_REPETITION_PENALTY</strong> - Penalty for repetition (1.0-2.0)</li>
 *   <li><strong>WATSONXAI_MIN_NEW_TOKENS</strong> - Minimum new tokens in response</li>
 *   <li><strong>WATSONXAI_STOP_SEQUENCES</strong> - Stop sequences for response generation</li>
 *   <li><strong>WATSONXAI_TIMEOUT</strong> - Request timeout in seconds</li>
 *   <li><strong>WATSONXAI_MAX_RETRIES</strong> - Maximum retry attempts</li>
 *   <li><strong>WATSONXAI_LOG_REQUESTS_AND_RESPONSES</strong> - Enable request/response logging</li>
 * </ul>
 *
 * <p><strong>Configuration Sources:</strong>
 * Configuration values are resolved in the following order of precedence:
 * <ol>
 *   <li>Environment variables (WATSONXAI_API_KEY, WATSONXAI_URL, etc.)</li>
 *   <li>System properties (watsonxai.api.key, watsonxai.url, etc.)</li>
 *   <li>forage-model-watsonx-ai.properties file in classpath</li>
 * </ol>
 *
 * <p><strong>Usage Example:</strong>
 * <pre>{@code
 * // Set environment variables
 * export WATSONXAI_API_KEY="your-api-key"
 * export WATSONXAI_URL="https://us-south.ml.cloud.ibm.com"
 * export WATSONXAI_PROJECT_ID="your-project-id"
 *
 * // Create and use configuration
 * WatsonxAiConfig config = new WatsonxAiConfig();
 * String apiKey = config.apiKey();     // Returns the configured API key
 * String url = config.url();           // Returns the configured URL
 * }</pre>
 *
 * <p><strong>Security Considerations:</strong>
 * Never commit API keys to version control. Use environment variables or secure configuration
 * management systems for production deployments.
 *
 * @see AbstractConfig
 * @see io.kaoto.forage.core.util.config.ConfigStore
 * @see io.kaoto.forage.core.util.config.ConfigModule
 * @since 1.0
 */
public class WatsonxAiConfig extends AbstractConfig {

    /**
     * Constructs a new WatsonxAiConfig and registers configuration parameters with the ConfigStore.
     */
    public WatsonxAiConfig() {
        this(null);
    }

    public WatsonxAiConfig(String prefix) {
        super(prefix, WatsonxAiConfigEntries.class);
    }

    /**
     * Returns the unique identifier for this Watsonx.ai configuration module.
     *
     * @return the module name "forage-model-watsonx-ai"
     */
    @Override
    public String name() {
        return "forage-model-watsonx-ai";
    }

    /**
     * Returns the IBM Cloud API key for authentication.
     *
     * <p>This is required to authenticate with the IBM Cloud and Watsonx.ai services.
     * You can obtain an API key from the IBM Cloud console.
     *
     * <p><strong>Configuration Sources (in order of precedence):</strong>
     * <ol>
     *   <li>WATSONXAI_API_KEY environment variable</li>
     *   <li>watsonxai.api.key system property</li>
     *   <li>api-key property in forage-model-watsonx-ai.properties</li>
     * </ol>
     *
     * @return the IBM Cloud API key
     * @throws io.kaoto.forage.core.util.config.MissingConfigException if no API key is configured
     */
    public String apiKey() {
        return getRequired(API_KEY, "Missing Watsonx.ai API key");
    }

    /**
     * Returns the Watsonx.ai service URL.
     *
     * <p>This is the endpoint where your Watsonx.ai service is running.
     * Different regions have different URLs.
     *
     * <p><strong>Example URLs:</strong>
     * <ul>
     *   <li><strong>https://us-south.ml.cloud.ibm.com</strong> - US South region</li>
     *   <li><strong>https://eu-de.ml.cloud.ibm.com</strong> - EU Germany region</li>
     *   <li><strong>https://jp-tok.ml.cloud.ibm.com</strong> - Japan Tokyo region</li>
     * </ul>
     *
     * <p><strong>Configuration Sources (in order of precedence):</strong>
     * <ol>
     *   <li>WATSONXAI_URL environment variable</li>
     *   <li>watsonxai.url system property</li>
     *   <li>url property in forage-model-watsonx-ai.properties</li>
     * </ol>
     *
     * @return the Watsonx.ai service URL
     * @throws io.kaoto.forage.core.util.config.MissingConfigException if no URL is configured
     */
    public String url() {
        return getRequired(URL, "Missing Watsonx.ai URL");
    }

    /**
     * Returns the Watsonx.ai project ID.
     *
     * <p>This identifies the specific project within Watsonx.ai where your models
     * and resources are configured.
     *
     * <p><strong>Configuration Sources (in order of precedence):</strong>
     * <ol>
     *   <li>WATSONXAI_PROJECT_ID environment variable</li>
     *   <li>watsonxai.project.id system property</li>
     *   <li>project-id property in forage-model-watsonx-ai.properties</li>
     * </ol>
     *
     * @return the Watsonx.ai project ID
     * @throws io.kaoto.forage.core.util.config.MissingConfigException if no project ID is configured
     */
    public String projectId() {
        return getRequired(PROJECT_ID, "Missing Watsonx.ai project ID");
    }

    /**
     * Returns the name of the model to use.
     *
     * <p>Watsonx.ai offers various foundation models with different capabilities.
     *
     * <p><strong>Common Model Examples:</strong>
     * <ul>
     *   <li><strong>llama-3-405b-instruct</strong> - Large Llama 3 model (default)</li>
     *   <li><strong>llama-3-70b-instruct</strong> - Medium Llama 3 model</li>
     *   <li><strong>llama-3-8b-instruct</strong> - Small Llama 3 model</li>
     *   <li><strong>granite-13b-chat-v2</strong> - IBM Granite chat model</li>
     *   <li><strong>granite-13b-instruct-v2</strong> - IBM Granite instruction model</li>
     * </ul>
     *
     * @return the model name, defaults to "llama-3-405b-instruct" if not configured
     */
    public String modelName() {
        return get(MODEL_NAME).orElse(MODEL_NAME.defaultValue());
    }

    /**
     * Returns the temperature setting for response generation.
     *
     * <p>Temperature controls the randomness of the model's output. Lower values make responses
     * more focused and deterministic, while higher values make them more creative and varied.
     *
     * <p><strong>Value Range:</strong> 0.0 to 2.0
     *
     * @return the temperature value, or null if not configured
     */
    public Double temperature() {
        return get(TEMPERATURE).map(Double::parseDouble).orElse(null);
    }

    /**
     * Returns the maximum number of new tokens for model responses.
     *
     * <p>This setting limits the length of the model's response.
     *
     * @return the maximum new tokens limit, or null if not configured
     */
    public Integer maxNewTokens() {
        return get(MAX_NEW_TOKENS).map(Integer::parseInt).orElse(null);
    }

    /**
     * Returns the top-p (nucleus sampling) probability threshold.
     *
     * <p>An alternative to temperature for controlling response diversity.
     *
     * <p><strong>Value Range:</strong> 0.0 to 1.0
     *
     * @return the top-p value, or null if not configured
     */
    public Double topP() {
        return get(TOP_P).map(Double::parseDouble).orElse(null);
    }

    /**
     * Returns the top-k sampling parameter.
     *
     * <p>Limits the number of highest probability tokens to consider during generation.
     *
     * @return the top-k value, or null if not configured
     */
    public Integer topK() {
        return get(TOP_K).map(Integer::parseInt).orElse(null);
    }

    /**
     * Returns the random seed for deterministic response generation.
     *
     * @return the random seed value, or null if not configured
     */
    public Integer randomSeed() {
        return get(RANDOM_SEED).map(Integer::parseInt).orElse(null);
    }

    /**
     * Returns the repetition penalty for discouraging repetitive content.
     *
     * <p><strong>Value Range:</strong> 1.0 to 2.0
     *
     * @return the repetition penalty value, or null if not configured
     */
    public Double repetitionPenalty() {
        return get(REPETITION_PENALTY).map(Double::parseDouble).orElse(null);
    }

    /**
     * Returns the minimum number of new tokens for model responses.
     *
     * @return the minimum new tokens, or null if not configured
     */
    public Integer minNewTokens() {
        return get(MIN_NEW_TOKENS).map(Integer::parseInt).orElse(null);
    }

    /**
     * Returns the stop sequences for response generation.
     *
     * <p>These sequences will cause the model to stop generating when encountered.
     *
     * @return the stop sequences as a list, or null if not configured
     */
    public List<String> stopSequences() {
        return get(STOP_SEQUENCES)
                .map(sequences -> Arrays.stream(sequences.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList())
                .orElse(null);
    }

    /**
     * Returns the request timeout duration in seconds.
     *
     * @return the timeout in seconds, or null if not configured
     */
    public Integer timeoutSeconds() {
        return get(TIMEOUT).map(Integer::parseInt).orElse(null);
    }

    /**
     * Returns the maximum number of retry attempts for failed requests.
     *
     * @return the maximum retry attempts, or null if not configured
     */
    public Integer maxRetries() {
        return get(MAX_RETRIES).map(Integer::parseInt).orElse(null);
    }

    /**
     * Returns whether to log both request and response details.
     *
     * @return true if request and response logging is enabled, false if disabled, or null if not configured
     */
    public Boolean logRequestsAndResponses() {
        return get(LOG_REQUESTS_AND_RESPONSES).map(Boolean::parseBoolean).orElse(null);
    }
}
