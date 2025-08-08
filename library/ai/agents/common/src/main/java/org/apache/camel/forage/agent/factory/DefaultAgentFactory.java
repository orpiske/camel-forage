package org.apache.camel.forage.agent.factory;

import java.util.ServiceLoader;
import org.apache.camel.CamelContext;
import org.apache.camel.component.langchain4j.agent.api.AgentConfiguration;
import org.apache.camel.forage.core.ai.ChatMemoryFactory;
import org.apache.camel.forage.core.ai.ModelProvider;
import org.apache.camel.component.langchain4j.agent.api.Agent;
import org.apache.camel.component.langchain4j.agent.api.AgentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of AgentFactory that uses ServiceLoader to discover and create agents
 */
public class DefaultAgentFactory implements AgentFactory {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAgentFactory.class);

    private CamelContext camelContext;
    private static Agent agent;

    public DefaultAgentFactory() {
        LOG.trace("Creating DefaultAgentFactory");
    }

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

    private ChatMemoryFactory newChatMemoryFactory() {
        ServiceLoader<ChatMemoryFactory> serviceLoader = ServiceLoader.load(ChatMemoryFactory.class, camelContext.getApplicationContextClassLoader());

        return serviceLoader.findFirst().orElseThrow(() -> new IllegalStateException("No ChatMemoryFactory implementation found via ServiceLoader"));
    }

    @Override
    public synchronized Agent createAgent() throws Exception {
        if (agent != null) {
            return agent;
        }

        return doCreateAgent();
    }

    private synchronized Agent doCreateAgent() {
        LOG.trace("Creating Agent");
        agent = newAgent();


        if (agent instanceof ConfigurationAware configurationAware) {
            LOG.trace("Creating Agent (step 1)");
            ModelProvider modelProvider = newModelProvider();

            LOG.trace("Creating Agent (step 2)");
            ChatMemoryFactory chatMemoryFactory = newChatMemoryFactory();

            LOG.trace("Creating Agent (step 3)");
            AgentConfiguration agentConfiguration = new AgentConfiguration();
            agentConfiguration
                    .withChatModel(modelProvider.newModel())
                    .withChatMemoryProvider(chatMemoryFactory.newChatMemory());

            configurationAware.configure(agentConfiguration);
        }

        return agent;
    }
}