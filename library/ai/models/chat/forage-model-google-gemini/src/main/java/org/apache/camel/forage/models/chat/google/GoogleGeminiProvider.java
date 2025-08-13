package org.apache.camel.forage.models.chat.google;

import static java.time.Duration.ofSeconds;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.apache.camel.forage.core.ai.ModelProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provider for creating Google Gemini chat models
 */
public class GoogleGeminiProvider implements ModelProvider {
    private static final Logger LOG = LoggerFactory.getLogger(GoogleGeminiProvider.class);

    private static final GoogleConfig CONFIG = new GoogleConfig();

    @Override
    public ChatModel newModel() {
        LOG.trace("Creating google chat model");

        return GoogleAiGeminiChatModel.builder()
                .apiKey(CONFIG.apiKey())
                .modelName(CONFIG.modelName())
                .temperature(1.0)
                .timeout(ofSeconds(60))
                .logRequestsAndResponses(true)
                .build();
    }
}
