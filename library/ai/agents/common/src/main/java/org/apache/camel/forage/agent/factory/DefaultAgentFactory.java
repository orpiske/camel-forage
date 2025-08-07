package org.apache.camel.forage.agent.factory;

import java.util.ServiceLoader;
import org.apache.camel.CamelContext;
import org.apache.camel.component.langchain4j.agent.api.AgentConfiguration;
import org.apache.camel.forage.core.ai.ModelProvider;
import org.apache.camel.component.langchain4j.agent.api.Agent;
import org.apache.camel.component.langchain4j.agent.api.AgentFactory;

/**
 * Default implementation of AgentFactory that uses ServiceLoader to discover and create agents
 */
public class DefaultAgentFactory implements AgentFactory {

    private CamelContext camelContext;

    @Override
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public CamelContext getCamelContext() {
        return camelContext;
    }

    private ModelProvider newModelProvider() {
        ServiceLoader<ModelProvider> modelLoader = ServiceLoader.load(ModelProvider.class, camelContext.getApplicationContextClassLoader());

        return modelLoader.findFirst().orElseThrow(() -> new IllegalStateException("No ModelProvider implementation found via ServiceLoader"));
    }

    private Agent newAgent() {
        ServiceLoader<Agent> serviceLoader = ServiceLoader.load(Agent.class, camelContext.getApplicationContextClassLoader());

        return serviceLoader.findFirst().orElseThrow(() -> new IllegalStateException("No Agent implementation found via ServiceLoader"));
    }

    @Override
    public Agent createAgent() throws Exception {
        ModelProvider modelProvider = newModelProvider();

        Agent agent = newAgent();

        if (agent instanceof ConfigurationAware configurationAware) {
            AgentConfiguration agentConfiguration = new AgentConfiguration();
            agentConfiguration
                    .withChatModel(modelProvider.newModel());

            configurationAware.configure(agentConfiguration);
        }

        return agent;
    }
}