package org.apache.camel.forage.agent.simple;

public interface ForageAgentWithoutMemory {

    /**
     * Simple AI service interface without memory support
     */
    String chat(String userMessage);

    String chat(String userMessage, String systemMessage);
}
