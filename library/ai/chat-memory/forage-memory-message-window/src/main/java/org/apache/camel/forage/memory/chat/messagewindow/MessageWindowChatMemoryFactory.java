package org.apache.camel.forage.memory.chat.messagewindow;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.apache.camel.forage.core.ai.ChatMemoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageWindowChatMemoryFactory implements ChatMemoryFactory {
    private static final Logger LOG = LoggerFactory.getLogger(MessageWindowChatMemoryFactory.class);

    private static final PersistentChatMemoryStore PERSISTENT_CHAT_MEMORY_STORE = new PersistentChatMemoryStore();
    private final ChatMemoryProvider chatMemoryProvider;

    public MessageWindowChatMemoryFactory() {
        chatMemoryProvider = getChatMemoryProvider();
    }

    @Override
    public synchronized ChatMemoryProvider newChatMemory() {
        return chatMemoryProvider;
    }

    private static ChatMemoryProvider getChatMemoryProvider() {
        LOG.trace("Creating MessageWindowChatMemoryFactory");
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(10)
                .chatMemoryStore(PERSISTENT_CHAT_MEMORY_STORE)
                .build();
    }
}
