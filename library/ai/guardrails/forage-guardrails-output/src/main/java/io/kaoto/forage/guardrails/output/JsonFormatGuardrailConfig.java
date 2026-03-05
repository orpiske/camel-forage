package io.kaoto.forage.guardrails.output;

import java.util.Arrays;
import io.kaoto.forage.core.util.config.AbstractConfig;

import static io.kaoto.forage.guardrails.output.JsonFormatGuardrailConfigEntries.ALLOW_ARRAY;
import static io.kaoto.forage.guardrails.output.JsonFormatGuardrailConfigEntries.EXTRACT_JSON;
import static io.kaoto.forage.guardrails.output.JsonFormatGuardrailConfigEntries.REQUIRED_FIELDS;

/**
 * Configuration class for JSON format guardrail.
 */
public class JsonFormatGuardrailConfig extends AbstractConfig {

    public JsonFormatGuardrailConfig() {
        this(null);
    }

    public JsonFormatGuardrailConfig(String prefix) {
        super(prefix, JsonFormatGuardrailConfigEntries.class);
    }

    @Override
    public String name() {
        return "forage-guardrail-json-format";
    }

    public String[] requiredFields() {
        return get(REQUIRED_FIELDS)
                .map(s -> Arrays.stream(s.split(","))
                        .map(String::trim)
                        .filter(f -> !f.isEmpty())
                        .toArray(String[]::new))
                .orElse(new String[0]);
    }

    public boolean extractJson() {
        return get(EXTRACT_JSON).map(Boolean::parseBoolean).orElse(true);
    }

    public boolean allowArray() {
        return get(ALLOW_ARRAY).map(Boolean::parseBoolean).orElse(true);
    }
}
