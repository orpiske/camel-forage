package org.apache.camel.forage.core.ai;

import dev.langchain4j.model.chat.ChatModel;

/**
 * Provider interface for creating AI models
 */
public interface ModelProvider {

    /**
     * Creates a new model instance
     * @return the created model
     */
    ChatModel newModel();

}