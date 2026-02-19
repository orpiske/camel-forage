package io.kaoto.forage.memory.chat.messagewindow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.kaoto.forage.core.ai.ChatMemoryBeanProvider;
import io.kaoto.forage.core.annotations.ForageBean;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

@ForageBean(
        value = "message-window",
        components = {"camel-langchain4j-agent"},
        feature = "Memory",
        configClass = MessageWindowConfig.class,
        description = "In-memory storage with configurable message window size")
public class MessageWindowChatMemoryBeanProvider implements ChatMemoryBeanProvider {
    private static final Logger LOG = LoggerFactory.getLogger(MessageWindowChatMemoryBeanProvider.class);

    private static final PersistentChatMemoryStore PERSISTENT_CHAT_MEMORY_STORE = new PersistentChatMemoryStore();
    private static final MessageWindowConfig CONFIG = new MessageWindowConfig();
    private final ChatMemoryProvider chatMemoryProvider;

    public MessageWindowChatMemoryBeanProvider() {
        chatMemoryProvider = getChatMemoryProvider();
    }

    @Override
    public synchronized ChatMemoryProvider create() {
        return chatMemoryProvider;
    }

    @Override
    public ChatMemoryProvider create(String id) {
        throw new UnsupportedOperationException(
                "Named chat memory stores are not yet supported for the memory chat window");
    }

    private static ChatMemoryProvider getChatMemoryProvider() {
        LOG.trace("Creating MessageWindowChatMemoryFactory with maxMessages={}", CONFIG.maxMessages());
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(CONFIG.maxMessages())
                .chatMemoryStore(PERSISTENT_CHAT_MEMORY_STORE)
                .build();
    }
}
