package org.apache.camel.forage.models.chat.google;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.apache.camel.forage.core.ai.ModelProvider;

import static java.time.Duration.ofSeconds;

/**
 * Provider for creating Google Gemini chat models
 */
public class GoogleGeminiProvider implements ModelProvider {

    private String apiKey = System.getenv("GOOGLE_API_KEY");
    private String modelName = System.getenv("GOOGLE_MODEL_NAME");

    @Override
    public ChatModel newModel() {
        System.out.println("Creating google chat model");
        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(1.0)
                .timeout(ofSeconds(60))
                .logRequestsAndResponses(true)
                .build();
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}