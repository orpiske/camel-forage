package io.kaoto.forage.memory.chat.messagewindow;

import io.kaoto.forage.core.util.config.AbstractConfig;

public class MessageWindowConfig extends AbstractConfig {

    private static final int DEFAULT_MAX_MESSAGES = 10;

    public MessageWindowConfig() {
        this(null);
    }

    public MessageWindowConfig(String prefix) {
        super(prefix, MessageWindowConfigEntries.class);
    }

    public int maxMessages() {
        return get(MessageWindowConfigEntries.MAX_MESSAGES)
                .map(value -> {
                    try {
                        return Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid max messages value: " + value, e);
                    }
                })
                .orElse(DEFAULT_MAX_MESSAGES);
    }

    @Override
    public String name() {
        return "forage-memory-message-window";
    }
}
