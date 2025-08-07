package org.apache.camel.forage.core.ai;

import dev.langchain4j.memory.chat.ChatMemoryProvider;

public interface ChatMemoryFactory {
    
    ChatMemoryProvider newChatMemory();
}