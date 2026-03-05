package io.kaoto.forage.guardrails.input;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import org.apache.camel.component.langchain4j.agent.api.guardrails.CodeInjectionGuardrail.InjectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.kaoto.forage.core.util.config.AbstractConfig;

import static io.kaoto.forage.guardrails.input.CodeInjectionGuardrailConfigEntries.DETECT_TYPES;
import static io.kaoto.forage.guardrails.input.CodeInjectionGuardrailConfigEntries.STRICT;

/**
 * Configuration class for code injection guardrail.
 */
public class CodeInjectionGuardrailConfig extends AbstractConfig {

    private static final Logger LOG = LoggerFactory.getLogger(CodeInjectionGuardrailConfig.class);

    public CodeInjectionGuardrailConfig() {
        this(null);
    }

    public CodeInjectionGuardrailConfig(String prefix) {
        super(prefix, CodeInjectionGuardrailConfigEntries.class);
    }

    @Override
    public String name() {
        return "forage-guardrail-code-injection";
    }

    public boolean strict() {
        return get(STRICT).map(Boolean::parseBoolean).orElse(false);
    }

    public Set<InjectionType> detectTypes() {
        String typesStr = get(DETECT_TYPES)
                .orElse(
                        "SHELL_COMMAND,SQL_INJECTION,JAVASCRIPT,HTML_XSS,PATH_TRAVERSAL,COMMAND_CHAINING,TEMPLATE_INJECTION");

        Set<InjectionType> types = EnumSet.noneOf(InjectionType.class);
        Arrays.stream(typesStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(s -> {
                    try {
                        types.add(InjectionType.valueOf(s.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        LOG.debug("Ignoring unrecognized type: {}", s);
                    }
                });

        return types.isEmpty() ? EnumSet.allOf(InjectionType.class) : types;
    }
}
