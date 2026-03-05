package io.kaoto.forage.guardrails.output;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

/**
 * Configuration entries for output length guardrail.
 */
public final class OutputLengthGuardrailConfigEntries extends ConfigEntries {

    public static final ConfigModule MAX_CHARS = ConfigModule.of(
            OutputLengthGuardrailConfig.class,
            "forage.guardrail.output.length.max.chars",
            "Maximum allowed character count for output messages",
            "Max Characters",
            "50000",
            "integer",
            false,
            ConfigTag.COMMON);

    public static final ConfigModule MIN_CHARS = ConfigModule.of(
            OutputLengthGuardrailConfig.class,
            "forage.guardrail.output.length.min.chars",
            "Minimum required character count for output messages",
            "Min Characters",
            "1",
            "integer",
            false,
            ConfigTag.COMMON);

    public static final ConfigModule TRUNCATE_ON_OVERFLOW = ConfigModule.of(
            OutputLengthGuardrailConfig.class,
            "forage.guardrail.output.length.truncate.on.overflow",
            "Whether to truncate instead of failing on overflow",
            "Truncate on Overflow",
            "false",
            "boolean",
            false,
            ConfigTag.COMMON);

    static {
        initModules(OutputLengthGuardrailConfigEntries.class, MAX_CHARS, MIN_CHARS, TRUNCATE_ON_OVERFLOW);
    }
}
