package org.apache.camel.forage.agent.memoryless;

import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import java.util.List;
import org.apache.camel.component.langchain4j.agent.api.Agent;
import org.apache.camel.component.langchain4j.agent.api.AgentConfiguration;
import org.apache.camel.component.langchain4j.agent.api.AiAgentBody;
import org.apache.camel.forage.agent.factory.ConfigurationAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transient implementation of an AI agent that provides basic chat functionality without memory
 */
public class MemorylessAgent implements Agent, ConfigurationAware {
    private static final Logger LOG = LoggerFactory.getLogger(MemorylessAgent.class);

    private AgentConfiguration configuration;

    public MemorylessAgent() {}

    @Override
    public void configure(AgentConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String chat(AiAgentBody aiAgentBody, ToolProvider toolProvider) {
        LOG.trace(
                "Chatting using transient agent (no memory) {}",
                Thread.currentThread().getId());
        AiAgentService agentService = createAiAgentService(toolProvider);

        return aiAgentBody.getSystemMessage() != null
                ? agentService.chat(aiAgentBody.getUserMessage(), aiAgentBody.getSystemMessage())
                : agentService.chat(aiAgentBody.getUserMessage());
    }

    /**
     * Create AI service with tools but without memory support
     */
    private AiAgentService createAiAgentService(ToolProvider toolProvider) {
        LOG.trace(
                "Creating AiAgentService without memory {}",
                Thread.currentThread().getId());
        var builder = AiServices.builder(AiAgentService.class).chatModel(configuration.getChatModel());

        // Apache Camel Tool Provider
        if (toolProvider != null) {
            builder.toolProvider(toolProvider);
        }

        // RAG
        if (configuration.getRetrievalAugmentor() != null) {
            builder.retrievalAugmentor(configuration.getRetrievalAugmentor());
        }

        // Input Guardrails
        if (configuration.getInputGuardrailClasses() != null
                && !configuration.getInputGuardrailClasses().isEmpty()) {
            builder.inputGuardrailClasses((List) configuration.getInputGuardrailClasses());
        }

        // Output Guardrails
        if (configuration.getOutputGuardrailClasses() != null
                && !configuration.getOutputGuardrailClasses().isEmpty()) {
            builder.outputGuardrailClasses((List) configuration.getOutputGuardrailClasses());
        }

        return builder.build();
    }

    /**
     * Simple AI service interface without memory support
     */
    public interface AiAgentService {
        String chat(String userMessage);

        String chat(String userMessage, String systemMessage);
    }
}
