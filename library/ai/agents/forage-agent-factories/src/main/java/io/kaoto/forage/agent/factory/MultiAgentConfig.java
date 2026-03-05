package io.kaoto.forage.agent.factory;

import java.util.List;
import io.kaoto.forage.core.util.config.AbstractConfig;
import io.kaoto.forage.core.util.config.ConfigHelper;

import static io.kaoto.forage.agent.factory.MultiAgentConfigEntries.MULTI_AGENT_ID_SOURCE;
import static io.kaoto.forage.agent.factory.MultiAgentConfigEntries.MULTI_AGENT_ID_SOURCE_HEADER;
import static io.kaoto.forage.agent.factory.MultiAgentConfigEntries.MULTI_AGENT_ID_SOURCE_PROPERTY;
import static io.kaoto.forage.agent.factory.MultiAgentConfigEntries.MULTI_AGENT_ID_SOURCE_VARIABLE;
import static io.kaoto.forage.agent.factory.MultiAgentConfigEntries.MULTI_AGENT_NAMES;

/**
 * Configuration class for multi-agent orchestration within the Forage framework.
 *
 * <p>This class manages configuration settings for multi-agent scenarios, including agent
 * discovery, naming, and agent ID extraction strategies. It provides the foundation for
 * sophisticated multi-agent routing and coordination.
 *
 * <p>This configuration is used by the MultiAgentFactory for dynamic agent selection and routing
 * based on various extraction strategies, enabling flexible multi-agent architectures.
 */
public class MultiAgentConfig extends AbstractConfig {

    /**
     * Source type constants for agent ID extraction strategies.
     */
    public static final String ROUTE_ID = "route";

    public static final String HEADER = "header";
    public static final String PROPERTY = "property";
    public static final String VARIABLE = "variable";

    public MultiAgentConfig() {
        this(null);
    }

    public MultiAgentConfig(String prefix) {
        super(prefix, MultiAgentConfigEntries.class);
    }

    @Override
    public String name() {
        return "forage-agent-factory";
    }

    public List<String> multiAgentNames() {
        return ConfigHelper.readAsList(MULTI_AGENT_NAMES.asNamed(prefix()));
    }

    public String multiAgentIdSource() {
        return get(MULTI_AGENT_ID_SOURCE).orElse(ROUTE_ID);
    }

    public String multiAgentIdSourceHeader() {
        return get(MULTI_AGENT_ID_SOURCE_HEADER).orElse(null);
    }

    public String multiAgentIdSourceProperty() {
        return get(MULTI_AGENT_ID_SOURCE_PROPERTY).orElse(null);
    }

    public String multiAgentIdSourceVariable() {
        return get(MULTI_AGENT_ID_SOURCE_VARIABLE).orElse(null);
    }
}
