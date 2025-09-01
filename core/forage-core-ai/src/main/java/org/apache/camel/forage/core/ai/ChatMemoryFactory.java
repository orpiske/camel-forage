package org.apache.camel.forage.core.ai;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import org.apache.camel.forage.core.common.BeanProvider;

/**
 * Creates chat memory provider. This is named like this to avoid confusion with the ChatMemoryProvider from LangChain4j
 */
public interface ChatMemoryFactory extends BeanProvider<ChatMemoryProvider> {}
