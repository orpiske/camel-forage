package io.kaoto.forage.guardrails.input;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

/**
 * Configuration entries for code injection guardrail.
 */
public final class CodeInjectionGuardrailConfigEntries extends ConfigEntries {

    public static final ConfigModule STRICT = ConfigModule.of(
            CodeInjectionGuardrailConfig.class,
            "forage.guardrail.code.injection.strict",
            "Enable strict mode (fail on any single pattern match)",
            "Strict Mode",
            "false",
            "boolean",
            false,
            ConfigTag.COMMON);

    public static final ConfigModule DETECT_TYPES = ConfigModule.of(
            CodeInjectionGuardrailConfig.class,
            "forage.guardrail.code.injection.detect.types",
            "Comma-separated list of injection types to detect: SHELL_COMMAND, SQL_INJECTION, JAVASCRIPT, HTML_XSS, PATH_TRAVERSAL, COMMAND_CHAINING, TEMPLATE_INJECTION",
            "Detect Types",
            "SHELL_COMMAND,SQL_INJECTION,JAVASCRIPT,HTML_XSS,PATH_TRAVERSAL,COMMAND_CHAINING,TEMPLATE_INJECTION",
            "string",
            false,
            ConfigTag.COMMON);

    static {
        initModules(CodeInjectionGuardrailConfigEntries.class, STRICT, DETECT_TYPES);
    }
}
