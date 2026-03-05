package io.kaoto.forage.guardrails.output;

import io.kaoto.forage.core.util.config.AbstractConfig;

import static io.kaoto.forage.guardrails.output.OutputLengthGuardrailConfigEntries.MAX_CHARS;
import static io.kaoto.forage.guardrails.output.OutputLengthGuardrailConfigEntries.MIN_CHARS;
import static io.kaoto.forage.guardrails.output.OutputLengthGuardrailConfigEntries.TRUNCATE_ON_OVERFLOW;

/**
 * Configuration class for output length guardrail.
 */
public class OutputLengthGuardrailConfig extends AbstractConfig {

    public OutputLengthGuardrailConfig() {
        this(null);
    }

    public OutputLengthGuardrailConfig(String prefix) {
        super(prefix, OutputLengthGuardrailConfigEntries.class);
    }

    @Override
    public String name() {
        return "forage-guardrail-output-length";
    }

    public int maxChars() {
        return get(MAX_CHARS).map(Integer::parseInt).orElse(50000);
    }

    public int minChars() {
        return get(MIN_CHARS).map(Integer::parseInt).orElse(1);
    }

    public boolean truncateOnOverflow() {
        return get(TRUNCATE_ON_OVERFLOW).map(Boolean::parseBoolean).orElse(false);
    }
}
