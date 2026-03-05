package io.kaoto.forage.guardrails.output;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

/**
 * Configuration entries for JSON format guardrail.
 */
public final class JsonFormatGuardrailConfigEntries extends ConfigEntries {

    public static final ConfigModule REQUIRED_FIELDS = ConfigModule.of(
            JsonFormatGuardrailConfig.class,
            "forage.guardrail.json.format.required.fields",
            "Comma-separated list of required field names in the JSON",
            "Required Fields",
            null,
            "string",
            false,
            ConfigTag.COMMON);

    public static final ConfigModule EXTRACT_JSON = ConfigModule.of(
            JsonFormatGuardrailConfig.class,
            "forage.guardrail.json.format.extract.json",
            "Whether to extract JSON from surrounding text",
            "Extract JSON",
            "true",
            "boolean",
            false,
            ConfigTag.COMMON);

    public static final ConfigModule ALLOW_ARRAY = ConfigModule.of(
            JsonFormatGuardrailConfig.class,
            "forage.guardrail.json.format.allow.array",
            "Whether to allow JSON arrays (not just objects)",
            "Allow Array",
            "true",
            "boolean",
            false,
            ConfigTag.COMMON);

    static {
        initModules(JsonFormatGuardrailConfigEntries.class, REQUIRED_FIELDS, EXTRACT_JSON, ALLOW_ARRAY);
    }
}
