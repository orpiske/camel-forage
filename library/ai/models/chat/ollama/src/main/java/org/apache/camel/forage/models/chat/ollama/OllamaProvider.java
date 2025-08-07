package org.apache.camel.forage.models.chat.ollama;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.apache.camel.forage.core.ai.ModelProvider;

/**
 * Provider for creating Ollama chat models
 */
public class OllamaProvider implements ModelProvider {

    private String baseUrl = "http://localhost:11434";
    private String modelName = "llama2";

    @Override
    public ChatModel newModel() {
        return OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(modelName)
                .build();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}