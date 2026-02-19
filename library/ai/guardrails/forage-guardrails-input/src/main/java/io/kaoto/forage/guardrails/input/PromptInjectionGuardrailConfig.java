package io.kaoto.forage.guardrails.input;

import java.util.Optional;
import io.kaoto.forage.core.util.config.Config;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigStore;

import static io.kaoto.forage.guardrails.input.PromptInjectionGuardrailConfigEntries.STRICT;

/**
 * Configuration class for prompt injection guardrail.
 */
public class PromptInjectionGuardrailConfig implements Config {

    private final String prefix;

    public PromptInjectionGuardrailConfig() {
        this(null);
    }

    public PromptInjectionGuardrailConfig(String prefix) {
        this.prefix = prefix;

        PromptInjectionGuardrailConfigEntries.register(prefix);
        ConfigStore.getInstance().load(PromptInjectionGuardrailConfig.class, this, this::register);
        PromptInjectionGuardrailConfigEntries.loadOverrides(prefix);
    }

    @Override
    public void register(String name, String value) {
        Optional<ConfigModule> config = PromptInjectionGuardrailConfigEntries.find(prefix, name);
        config.ifPresent(module -> ConfigStore.getInstance().set(module, value));
    }

    @Override
    public String name() {
        return "forage-guardrail-prompt-injection";
    }

    public boolean strict() {
        return ConfigStore.getInstance()
                .get(STRICT.asNamed(prefix))
                .map(Boolean::parseBoolean)
                .orElse(false);
    }
}
