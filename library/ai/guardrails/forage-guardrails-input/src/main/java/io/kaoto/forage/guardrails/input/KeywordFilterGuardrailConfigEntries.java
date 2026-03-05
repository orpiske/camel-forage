package io.kaoto.forage.guardrails.input;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

/**
 * Configuration entries for keyword filter guardrail.
 */
public final class KeywordFilterGuardrailConfigEntries extends ConfigEntries {

    public static final ConfigModule BLOCKED_WORDS = ConfigModule.of(
            KeywordFilterGuardrailConfig.class,
            "forage.guardrail.keyword.filter.blocked.words",
            "Comma-separated list of words to block",
            "Blocked Words",
            null,
            "string",
            false,
            ConfigTag.COMMON);

    public static final ConfigModule CASE_SENSITIVE = ConfigModule.of(
            KeywordFilterGuardrailConfig.class,
            "forage.guardrail.keyword.filter.case.sensitive",
            "Whether matching should be case-sensitive",
            "Case Sensitive",
            "false",
            "boolean",
            false,
            ConfigTag.COMMON);

    public static final ConfigModule WHOLE_WORD_MATCH = ConfigModule.of(
            KeywordFilterGuardrailConfig.class,
            "forage.guardrail.keyword.filter.whole.word.match",
            "Whether to match whole words only",
            "Whole Word Match",
            "true",
            "boolean",
            false,
            ConfigTag.COMMON);

    static {
        initModules(KeywordFilterGuardrailConfigEntries.class, BLOCKED_WORDS, CASE_SENSITIVE, WHOLE_WORD_MATCH);
    }
}
