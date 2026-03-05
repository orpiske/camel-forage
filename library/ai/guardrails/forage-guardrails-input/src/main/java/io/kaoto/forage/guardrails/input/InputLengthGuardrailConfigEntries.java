package io.kaoto.forage.guardrails.input;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

/**
 * Configuration entries for input length guardrail.
 */
public final class InputLengthGuardrailConfigEntries extends ConfigEntries {

    public static final ConfigModule MAX_CHARS = ConfigModule.of(
            InputLengthGuardrailConfig.class,
            "forage.guardrail.input.length.max.chars",
            "Maximum allowed character count for input messages",
            "Max Characters",
            "10000",
            "integer",
            false,
            ConfigTag.COMMON);

    public static final ConfigModule MIN_CHARS = ConfigModule.of(
            InputLengthGuardrailConfig.class,
            "forage.guardrail.input.length.min.chars",
            "Minimum required character count for input messages",
            "Min Characters",
            "1",
            "integer",
            false,
            ConfigTag.COMMON);

    static {
        initModules(InputLengthGuardrailConfigEntries.class, MAX_CHARS, MIN_CHARS);
    }
}
