package io.kaoto.forage.memory.chat.messagewindow;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

public final class MessageWindowConfigEntries extends ConfigEntries {

    public static final ConfigModule MAX_MESSAGES = ConfigModule.of(
            MessageWindowConfig.class,
            "forage.memory.message-window.max.messages",
            "Maximum number of messages to retain in memory",
            "Max Messages",
            "10",
            "integer",
            false,
            ConfigTag.COMMON);

    static {
        initModules(MessageWindowConfigEntries.class, MAX_MESSAGES);
    }
}
