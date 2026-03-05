package io.kaoto.forage.guardrails.input;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import io.kaoto.forage.core.util.config.AbstractConfig;

import static io.kaoto.forage.guardrails.input.KeywordFilterGuardrailConfigEntries.BLOCKED_WORDS;
import static io.kaoto.forage.guardrails.input.KeywordFilterGuardrailConfigEntries.CASE_SENSITIVE;
import static io.kaoto.forage.guardrails.input.KeywordFilterGuardrailConfigEntries.WHOLE_WORD_MATCH;

/**
 * Configuration class for keyword filter guardrail.
 */
public class KeywordFilterGuardrailConfig extends AbstractConfig {

    public KeywordFilterGuardrailConfig() {
        this(null);
    }

    public KeywordFilterGuardrailConfig(String prefix) {
        super(prefix, KeywordFilterGuardrailConfigEntries.class);
    }

    @Override
    public String name() {
        return "forage-guardrail-keyword-filter";
    }

    public Set<String> blockedWords() {
        return get(BLOCKED_WORDS)
                .map(s -> {
                    Set<String> words = new HashSet<>();
                    Arrays.stream(s.split(","))
                            .map(String::trim)
                            .filter(w -> !w.isEmpty())
                            .forEach(words::add);
                    return words;
                })
                .orElse(new HashSet<>());
    }

    public boolean caseSensitive() {
        return get(CASE_SENSITIVE).map(Boolean::parseBoolean).orElse(false);
    }

    public boolean wholeWordMatch() {
        return get(WHOLE_WORD_MATCH).map(Boolean::parseBoolean).orElse(true);
    }
}
