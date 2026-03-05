package io.kaoto.forage.guardrails.input;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import org.apache.camel.component.langchain4j.agent.api.guardrails.PiiDetectorGuardrail.PiiType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.kaoto.forage.core.util.config.AbstractConfig;

import static io.kaoto.forage.guardrails.input.PiiDetectorGuardrailConfigEntries.BLOCK_ON_DETECTION;
import static io.kaoto.forage.guardrails.input.PiiDetectorGuardrailConfigEntries.DETECT_TYPES;

/**
 * Configuration class for PII detector guardrail.
 */
public class PiiDetectorGuardrailConfig extends AbstractConfig {

    private static final Logger LOG = LoggerFactory.getLogger(PiiDetectorGuardrailConfig.class);

    public PiiDetectorGuardrailConfig() {
        this(null);
    }

    public PiiDetectorGuardrailConfig(String prefix) {
        super(prefix, PiiDetectorGuardrailConfigEntries.class);
    }

    @Override
    public String name() {
        return "forage-guardrail-pii-detector";
    }

    public Set<PiiType> detectTypes() {
        String typesStr = get(DETECT_TYPES).orElse("EMAIL,PHONE,SSN,CREDIT_CARD,IP_ADDRESS");

        Set<PiiType> types = EnumSet.noneOf(PiiType.class);
        Arrays.stream(typesStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(s -> {
                    try {
                        types.add(PiiType.valueOf(s.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        LOG.debug("Ignoring unrecognized type: {}", s);
                    }
                });

        return types.isEmpty() ? EnumSet.allOf(PiiType.class) : types;
    }

    public boolean blockOnDetection() {
        return get(BLOCK_ON_DETECTION).map(Boolean::parseBoolean).orElse(true);
    }
}
