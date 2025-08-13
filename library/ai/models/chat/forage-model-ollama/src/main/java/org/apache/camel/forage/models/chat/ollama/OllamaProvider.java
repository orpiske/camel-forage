package org.apache.camel.forage.models.chat.ollama;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.apache.camel.forage.core.ai.ModelProvider;

/**
 * Provider for creating Ollama chat models with configurable parameters.
 * 
 * <p>This provider creates instances of {@link OllamaChatModel} using configuration
 * values managed by {@link OllamaConfig}. The configuration supports environment
 * variables, system properties, and configuration files for flexible deployment.
 * 
 * <p><strong>Configuration:</strong>
 * <ul>
 *   <li>Base URL: Configured via OLLAMA_BASE_URL environment variable or defaults to "http://localhost:11434"</li>
 *   <li>Model Name: Configured via OLLAMA_MODEL_NAME environment variable or defaults to "llama3"</li>
 * </ul>
 * 
 * <p><strong>Usage:</strong>
 * <pre>{@code
 * // Configuration is automatic through environment variables or defaults
 * OllamaProvider provider = new OllamaProvider();
 * ChatModel model = provider.newModel();
 * }</pre>
 * 
 * @see OllamaConfig
 * @see ModelProvider
 * @since 1.0
 */
public class OllamaProvider implements ModelProvider {

    private final OllamaConfig config;

    /**
     * Constructs a new OllamaProvider with default configuration.
     * 
     * <p>The configuration is automatically loaded from environment variables,
     * system properties, or configuration files during construction.
     */
    public OllamaProvider() {
        this.config = new OllamaConfig();
    }

    /**
     * Creates a new Ollama chat model instance with the configured parameters.
     * 
     * <p>This method creates an {@link OllamaChatModel} using the base URL and
     * model name from the configuration. The model is ready to use for chat
     * operations once created.
     * 
     * @return a new configured Ollama chat model instance
     */
    @Override
    public ChatModel newModel() {
        String baseUrl = config.baseUrl();
        String modelName = config.modelName();
        
        System.out.printf("Creating Ollama model: %s at %s%n", modelName, baseUrl);
        
        return OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(modelName)
                .build();
    }

    /**
     * Returns the configured base URL for the Ollama server.
     * 
     * @return the base URL, never null
     */
    public String getBaseUrl() {
        return config.baseUrl();
    }

    /**
     * Returns the configured model name.
     * 
     * @return the model name, never null
     */
    public String getModelName() {
        return config.modelName();
    }
}
