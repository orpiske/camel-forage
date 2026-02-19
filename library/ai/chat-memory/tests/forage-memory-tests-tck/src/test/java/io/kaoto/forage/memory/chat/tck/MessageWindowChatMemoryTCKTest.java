package io.kaoto.forage.memory.chat.tck;

import io.kaoto.forage.core.ai.ChatMemoryBeanProvider;
import io.kaoto.forage.memory.chat.messagewindow.MessageWindowChatMemoryBeanProvider;

/**
 * Test for MessageWindowChatMemoryFactory using the ChatMemoryFactoryTCK.
 *
 * <p>This test validates the MessageWindowChatMemoryFactory implementation
 * from the forage-memory-message-window module against the comprehensive
 * test suite provided by the TCK.
 * All actual tests are inherited from ChatMemoryBeanProviderTCK
 *
 * @since 1.0
 */
class MessageWindowChatMemoryTCKTest extends ChatMemoryBeanProviderTCK {

    @Override
    protected ChatMemoryBeanProvider createChatMemoryFactory() {
        return new MessageWindowChatMemoryBeanProvider();
    }
}
