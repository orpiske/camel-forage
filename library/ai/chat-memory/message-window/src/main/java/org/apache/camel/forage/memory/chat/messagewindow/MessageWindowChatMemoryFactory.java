package org.apache.camel.forage.memory.chat.messagewindow;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.apache.camel.forage.core.ai.ChatMemoryFactory;

public class MessageWindowChatMemoryFactory implements ChatMemoryFactory {


    @Override
    public ChatMemoryProvider newChatMemory() {
        System.out.println("Creating MessageWindowChatMemoryFactory");
        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(10)
                .chatMemoryStore(new PersistentChatMemoryStore())
                .build();
        return chatMemoryProvider;
    }
}