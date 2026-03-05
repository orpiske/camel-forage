package io.kaoto.forage.guardrails.input;

import io.kaoto.forage.core.util.config.AbstractConfig;

import static io.kaoto.forage.guardrails.input.InputLengthGuardrailConfigEntries.MAX_CHARS;
import static io.kaoto.forage.guardrails.input.InputLengthGuardrailConfigEntries.MIN_CHARS;

/**
 * Configuration class for input length guardrail.
 */
public class InputLengthGuardrailConfig extends AbstractConfig {

    public InputLengthGuardrailConfig() {
        this(null);
    }

    public InputLengthGuardrailConfig(String prefix) {
        super(prefix, InputLengthGuardrailConfigEntries.class);
    }

    @Override
    public String name() {
        return "forage-guardrail-input-length";
    }

    public int maxChars() {
        return get(MAX_CHARS).map(Integer::parseInt).orElse(10000);
    }

    public int minChars() {
        return get(MIN_CHARS).map(Integer::parseInt).orElse(1);
    }
}
