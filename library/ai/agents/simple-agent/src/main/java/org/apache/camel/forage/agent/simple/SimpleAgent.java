package org.apache.camel.forage.agent.simple;

import dev.langchain4j.service.AiServices;
import java.util.List;
import org.apache.camel.component.langchain4j.agent.AiAgentBody;
import org.apache.camel.component.langchain4j.agent.api.Agent;
import dev.langchain4j.service.tool.ToolProvider;
import org.apache.camel.component.langchain4j.agent.api.AgentConfiguration;
import org.apache.camel.component.langchain4j.agent.api.AiAgentWithMemoryService;
import org.apache.camel.forage.agent.factory.ConfigurationAware;

/**
 * Simple implementation of an AI agent that provides basic chat functionality
 */
public class SimpleAgent implements Agent, ConfigurationAware {

    private AgentConfiguration configuration;

    public SimpleAgent() {}

    @Override
    public void configure(AgentConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String chat(AiAgentBody aiAgentBody, ToolProvider toolProvider) {
        AiAgentWithMemoryService agentService = createAiAgentService(toolProvider);

        return aiAgentBody.getSystemMessage() != null
                ? agentService.chat(aiAgentBody.getMemoryId(), aiAgentBody.getUserMessage(), aiAgentBody.getSystemMessage())
                : agentService.chat(aiAgentBody.getMemoryId(), aiAgentBody.getUserMessage());
    }

    /**
     * Create AI service with a single universal tool that handles multiple Camel routes and Memory Provider
     */
    private AiAgentWithMemoryService createAiAgentService(ToolProvider toolProvider) {
        var builder = AiServices.builder(AiAgentWithMemoryService.class)
                .chatModel(configuration.getChatModel())
                .chatMemoryProvider(configuration.getChatMemoryProvider());

        // Apache Camel Tool Provider
        if (toolProvider != null) {
            builder.toolProvider(toolProvider);
        }

        // RAG
        if (configuration.getRetrievalAugmentor() != null) {
            builder.retrievalAugmentor(configuration.getRetrievalAugmentor());
        }

        // Input Guardrails
        if (configuration.getInputGuardrailClasses() != null && !configuration.getInputGuardrailClasses().isEmpty()) {
            builder.inputGuardrailClasses((List) configuration.getInputGuardrailClasses());
        }

        // Output Guardrails
        if (configuration.getOutputGuardrailClasses() != null && !configuration.getOutputGuardrailClasses().isEmpty()) {
            builder.outputGuardrailClasses((List) configuration.getOutputGuardrailClasses());
        }

        return builder.build();
    }
}