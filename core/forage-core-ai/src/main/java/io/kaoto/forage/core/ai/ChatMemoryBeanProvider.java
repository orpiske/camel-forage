package io.kaoto.forage.core.ai;

import io.kaoto.forage.core.common.BeanProvider;
import dev.langchain4j.memory.chat.ChatMemoryProvider;

/**
 * Creates chat memory provider. This is named like this to avoid confusion with the ChatMemoryProvider from LangChain4j
 */
public interface ChatMemoryBeanProvider extends BeanProvider<ChatMemoryProvider> {}
