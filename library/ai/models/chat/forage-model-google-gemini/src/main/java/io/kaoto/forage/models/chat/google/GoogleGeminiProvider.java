package io.kaoto.forage.models.chat.google;

import static java.time.Duration.ofSeconds;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import io.kaoto.forage.core.ai.ModelProvider;
import io.kaoto.forage.core.annotations.ForageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provider for creating Google Gemini chat models
 */
@ForageBean(
        value = "google-gemini",
        components = {"camel-langchain4j-agent"},
        feature = "Chat Model",
        description = "Google Gemini models")
public class GoogleGeminiProvider implements ModelProvider {
    private static final Logger LOG = LoggerFactory.getLogger(GoogleGeminiProvider.class);

    @Override
    public ChatModel create(String id) {
        final GoogleConfig config = new GoogleConfig(id);
        LOG.trace("Creating google chat model");

        GoogleAiGeminiChatModel.GoogleAiGeminiChatModelBuilder builder =
                GoogleAiGeminiChatModel.builder().apiKey(config.apiKey()).modelName(config.modelName());

        Double temperature = config.temperature();
        if (temperature != null) {
            builder.temperature(temperature);
        }

        Integer timeout = config.timeout();
        if (timeout != null) {
            builder.timeout(ofSeconds(timeout));
        }

        Boolean logRequests = config.logRequestsAndResponses();
        if (logRequests != null) {
            builder.logRequestsAndResponses(logRequests);
        }

        return builder.build();
    }
}
