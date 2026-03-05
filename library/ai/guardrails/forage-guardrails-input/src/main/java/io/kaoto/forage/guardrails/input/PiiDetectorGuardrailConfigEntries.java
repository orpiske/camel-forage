package io.kaoto.forage.guardrails.input;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

/**
 * Configuration entries for PII detector guardrail.
 */
public final class PiiDetectorGuardrailConfigEntries extends ConfigEntries {

    public static final ConfigModule DETECT_TYPES = ConfigModule.of(
            PiiDetectorGuardrailConfig.class,
            "forage.guardrail.pii.detect.types",
            "Comma-separated list of PII types to detect: EMAIL, PHONE, SSN, CREDIT_CARD, IP_ADDRESS",
            "Detect Types",
            "EMAIL,PHONE,SSN,CREDIT_CARD,IP_ADDRESS",
            "string",
            false,
            ConfigTag.COMMON);

    public static final ConfigModule BLOCK_ON_DETECTION = ConfigModule.of(
            PiiDetectorGuardrailConfig.class,
            "forage.guardrail.pii.block.on.detection",
            "Whether to block the message when PII is detected (true) or just warn (false)",
            "Block on Detection",
            "true",
            "boolean",
            false,
            ConfigTag.COMMON);

    static {
        initModules(PiiDetectorGuardrailConfigEntries.class, DETECT_TYPES, BLOCK_ON_DETECTION);
    }
}
