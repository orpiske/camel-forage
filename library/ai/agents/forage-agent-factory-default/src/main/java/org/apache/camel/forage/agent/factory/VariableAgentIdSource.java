package org.apache.camel.forage.agent.factory;

import org.apache.camel.Exchange;

/**
 * Agent ID source that extracts the agent ID from a configurable exchange variable.
 * The variable name is configured via the multi.agent.id.source.variable property.
 */
public class VariableAgentIdSource implements AgentIdSource {

    private final String variableName;

    public VariableAgentIdSource(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public String extract(Exchange exchange) {
        if (variableName == null || variableName.isEmpty()) {
            return null;
        }

        Object variableValue = exchange.getVariable(variableName);
        return variableValue != null ? variableValue.toString() : null;
    }

    public String getVariableName() {
        return variableName;
    }
}
