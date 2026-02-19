package io.kaoto.forage.memory.chat.messagewindow;

import java.util.Optional;
import io.kaoto.forage.core.util.config.Config;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigStore;

public class MessageWindowConfig implements Config {

    private static final int DEFAULT_MAX_MESSAGES = 10;

    private final String prefix;

    public MessageWindowConfig() {
        this(null);
    }

    public MessageWindowConfig(String prefix) {
        this.prefix = prefix;

        MessageWindowConfigEntries.register(prefix);
        ConfigStore.getInstance().load(MessageWindowConfig.class, this, this::register);
        MessageWindowConfigEntries.loadOverrides(prefix);
    }

    public int maxMessages() {
        return ConfigStore.getInstance()
                .get(MessageWindowConfigEntries.MAX_MESSAGES.asNamed(prefix))
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

    @Override
    public void register(String name, String value) {
        Optional<ConfigModule> config = MessageWindowConfigEntries.find(prefix, name);
        config.ifPresent(module -> ConfigStore.getInstance().set(module, value));
    }
}
