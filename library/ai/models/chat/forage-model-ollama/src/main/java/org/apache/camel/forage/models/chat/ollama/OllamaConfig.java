package org.apache.camel.forage.models.chat.ollama;

import org.apache.camel.forage.core.util.config.Config;
import org.apache.camel.forage.core.util.config.ConfigEntry;
import org.apache.camel.forage.core.util.config.ConfigModule;
import org.apache.camel.forage.core.util.config.ConfigStore;
import org.apache.camel.forage.core.util.config.MissingConfigException;

/**
 * Configuration class for Ollama AI model integration in the Camel Forage framework.
 * 
 * <p>This configuration class manages the settings required to connect to and use Ollama
 * AI models. It handles server connection details and model selection through environment
 * variables with appropriate fallback mechanisms and default values.
 * 
 * <p><strong>Configuration Parameters:</strong>
 * <ul>
 *   <li><strong>OLLAMA_BASE_URL</strong> - The base URL of the Ollama server (default: "http://localhost:11434")</li>
 *   <li><strong>OLLAMA_MODEL_NAME</strong> - The specific Ollama model to use (default: "llama3")</li>
 * </ul>
 * 
 * <p><strong>Configuration Sources:</strong>
 * Configuration values are resolved in the following order of precedence:
 * <ol>
 *   <li>Environment variables (OLLAMA_BASE_URL, OLLAMA_MODEL_NAME)</li>
 *   <li>System properties (ollama.base.url, ollama.model.name)</li>
 *   <li>forage-model-ollama.properties file in classpath</li>
 *   <li>Default values if none of the above are provided</li>
 * </ol>
 * 
 * <p><strong>Usage Example:</strong>
 * <pre>{@code
 * // Set environment variables (optional)
 * export OLLAMA_BASE_URL="http://my-ollama-server:11434"
 * export OLLAMA_MODEL_NAME="llama3.1"
 * 
 * // Create and use configuration
 * OllamaConfig config = new OllamaConfig();
 * String baseUrl = config.baseUrl();      // Returns the configured base URL
 * String model = config.modelName();      // Returns the configured model name
 * }</pre>
 * 
 * <p><strong>Default Values:</strong>
 * <ul>
 *   <li>Base URL: "http://localhost:11434" (standard Ollama local installation)</li>
 *   <li>Model Name: "llama3" (popular general-purpose model)</li>
 * </ul>
 * 
 * <p>This class automatically registers itself and its configuration parameters with the
 * {@link ConfigStore} during construction, making the configuration values available
 * to other components in the framework.
 * 
 * @see Config
 * @see ConfigStore
 * @see ConfigModule
 * @since 1.0
 */
public class OllamaConfig implements Config {

    private static final ConfigModule BASE_URL = ConfigModule.of(OllamaConfig.class, "base-url");
    private static final ConfigModule MODEL_NAME = ConfigModule.of(OllamaConfig.class, "model-name");

    private static final String DEFAULT_BASE_URL = "http://localhost:11434";
    private static final String DEFAULT_MODEL_NAME = "llama3";

    /**
     * Constructs a new OllamaConfig and registers configuration parameters with the ConfigStore.
     * 
     * <p>During construction, this class:
     * <ul>
     *   <li>Registers the base URL configuration to be sourced from OLLAMA_BASE_URL environment variable</li>
     *   <li>Registers the model name configuration to be sourced from OLLAMA_MODEL_NAME environment variable</li>
     *   <li>Attempts to load additional properties from forage-model-ollama.properties</li>
     * </ul>
     * 
     * <p>Configuration values are resolved when this constructor is called, with default values
     * used when no configuration is provided through environment variables, system properties,
     * or configuration files.
     */
    public OllamaConfig() {
        ConfigStore.getInstance().add(BASE_URL, ConfigEntry.fromEnv("OLLAMA_BASE_URL"));
        ConfigStore.getInstance().add(MODEL_NAME, ConfigEntry.fromEnv("OLLAMA_MODEL_NAME"));
        ConfigStore.getInstance().add(OllamaConfig.class, this);
    }

    /**
     * Returns the unique identifier for this Ollama configuration module.
     * 
     * <p>This name corresponds to the module artifact and is used for:
     * <ul>
     *   <li>Loading configuration files (forage-model-ollama.properties)</li>
     *   <li>Identifying this module in logs and error messages</li>
     *   <li>Distinguishing this configuration from other AI model configurations</li>
     * </ul>
     * 
     * @return the module name "forage-model-ollama"
     */
    @Override
    public String name() {
        return "forage-model-ollama";
    }

    /**
     * Returns the base URL of the Ollama server.
     * 
     * <p>This method retrieves the base URL that was configured through environment variables,
     * system properties, or configuration files. The base URL specifies where the Ollama
     * server is running and should include the protocol and port.
     * 
     * <p><strong>Configuration Sources (in order of precedence):</strong>
     * <ol>
     *   <li>OLLAMA_BASE_URL environment variable</li>
     *   <li>ollama.base.url system property</li>
     *   <li>base-url property in forage-model-ollama.properties</li>
     *   <li>Default value: "http://localhost:11434"</li>
     * </ol>
     * 
     * <p><strong>Example Values:</strong>
     * <ul>
     *   <li>"http://localhost:11434" - Local Ollama installation</li>
     *   <li>"http://ollama-server:11434" - Remote server</li>
     *   <li>"https://my-ollama.example.com" - HTTPS endpoint</li>
     * </ul>
     * 
     * @return the Ollama server base URL, never null
     */
    public String baseUrl() {
        return ConfigStore.getInstance()
                .get(BASE_URL)
                .orElse(DEFAULT_BASE_URL);
    }

    /**
     * Returns the name of the Ollama model to use.
     * 
     * <p>This method retrieves the model name that specifies which Ollama model
     * should be used for AI operations. The model must be available on the
     * configured Ollama server.
     * 
     * <p><strong>Common Model Names:</strong>
     * <ul>
     *   <li><strong>llama3</strong> - Meta's Llama 3 model (8B parameters)</li>
     *   <li><strong>llama3.1</strong> - Updated Llama 3.1 model</li>
     *   <li><strong>mistral</strong> - Mistral 7B model</li>
     *   <li><strong>codellama</strong> - Code-specialized Llama model</li>
     *   <li><strong>phi3</strong> - Microsoft's Phi-3 model</li>
     * </ul>
     * 
     * <p><strong>Configuration Sources (in order of precedence):</strong>
     * <ol>
     *   <li>OLLAMA_MODEL_NAME environment variable</li>
     *   <li>ollama.model.name system property</li>
     *   <li>model-name property in forage-model-ollama.properties</li>
     *   <li>Default value: "llama3"</li>
     * </ol>
     * 
     * @return the Ollama model name, never null
     */
    public String modelName() {
        return ConfigStore.getInstance()
                .get(MODEL_NAME)
                .orElse(DEFAULT_MODEL_NAME);
    }
}