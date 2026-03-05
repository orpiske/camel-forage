package io.kaoto.forage.guardrails.input;

import io.kaoto.forage.core.util.config.AbstractConfig;

import static io.kaoto.forage.guardrails.input.PromptInjectionGuardrailConfigEntries.STRICT;

/**
 * Configuration class for prompt injection guardrail.
 */
public class PromptInjectionGuardrailConfig extends AbstractConfig {

    public PromptInjectionGuardrailConfig() {
        this(null);
    }

    public PromptInjectionGuardrailConfig(String prefix) {
        super(prefix, PromptInjectionGuardrailConfigEntries.class);
    }

    @Override
    public String name() {
        return "forage-guardrail-prompt-injection";
    }

    public boolean strict() {
        return get(STRICT).map(Boolean::parseBoolean).orElse(false);
    }
}
