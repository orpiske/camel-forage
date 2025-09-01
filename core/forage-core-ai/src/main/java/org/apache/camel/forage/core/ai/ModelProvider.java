package org.apache.camel.forage.core.ai;

import dev.langchain4j.model.chat.ChatModel;
import org.apache.camel.forage.core.common.BeanProvider;

/**
 * Provider interface for creating AI models
 */
public interface ModelProvider extends BeanProvider<ChatModel> {}
