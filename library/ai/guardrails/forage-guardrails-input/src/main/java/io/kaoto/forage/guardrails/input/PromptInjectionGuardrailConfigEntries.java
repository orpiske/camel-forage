package io.kaoto.forage.guardrails.input;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

/**
 * Configuration entries for prompt injection guardrail.
 */
public final class PromptInjectionGuardrailConfigEntries extends ConfigEntries {

    public static final ConfigModule STRICT = ConfigModule.of(
            PromptInjectionGuardrailConfig.class,
            "forage.guardrail.prompt.injection.strict",
            "Enable strict mode (fail on any single pattern match)",
            "Strict Mode",
            "false",
            "boolean",
            false,
            ConfigTag.COMMON);

    static {
        initModules(PromptInjectionGuardrailConfigEntries.class, STRICT);
    }
}
