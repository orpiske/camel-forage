package org.apache.camel.forage.agent.factory;

import org.apache.camel.Exchange;
import org.apache.camel.forage.core.exceptions.UndefinedAgentException;

/**
 * Factory class for creating AgentIdSource implementations based on string identifiers.
 * Supports creating different strategies for extracting agent IDs from exchanges.
 */
public class AgentIdSourceFactory {

    /**
     * Creates an AgentIdSource implementation based on the specified source type.
     *
     * @param config The MultiAgentConfig containing configuration for the source
     * @return An appropriate AgentIdSource implementation
     * @throws IllegalArgumentException if the source type is unknown or unsupported
     */
    public static AgentIdSource create(MultiAgentConfig config) {
        String sourceType = config.multiAgentIdSource();

        switch (sourceType.toLowerCase()) {
            case MultiAgentConfig.ROUTE_ID:
                return new RouteIdAgentIdSource();

            case MultiAgentConfig.HEADER:
                String headerName = config.multiAgentIdSourceHeader();
                if (headerName == null || headerName.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Header name must be configured via multi.agent.id.source.header when using header source type");
                }
                return new HeaderAgentIdSource(headerName);

            case MultiAgentConfig.PROPERTY:
                String propertyName = config.multiAgentIdSourceProperty();
                if (propertyName == null || propertyName.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Property name must be configured via multi.agent.id.source.property when using property source type");
                }
                return new PropertyAgentIdSource(propertyName);

            case MultiAgentConfig.VARIABLE:
                String variableName = config.multiAgentIdSourceVariable();
                if (variableName == null || variableName.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Variable name must be configured via multi.agent.id.source.variable when using variable source type");
                }
                return new VariableAgentIdSource(variableName);

            default:
                throw new IllegalArgumentException("Unknown agent ID source type: " + sourceType
                        + ". Supported types are: " + MultiAgentConfig.ROUTE_ID + ", " + MultiAgentConfig.HEADER + ", "
                        + MultiAgentConfig.PROPERTY + ", " + MultiAgentConfig.VARIABLE);
        }
    }

    /**
     * Checks if the given source type is supported by the factory.
     *
     * @param sourceType The source type to check
     * @return true if the source type is supported, false otherwise
     */
    public static boolean isSupported(String sourceType) {
        if (sourceType == null) {
            return false;
        }

        String lowerSourceType = sourceType.toLowerCase();
        return MultiAgentConfig.ROUTE_ID.equals(lowerSourceType)
                || MultiAgentConfig.HEADER.equals(lowerSourceType)
                || MultiAgentConfig.PROPERTY.equals(lowerSourceType)
                || MultiAgentConfig.VARIABLE.equals(lowerSourceType);
    }

    /**
     * Returns an array of all supported source types.
     *
     * @return Array of supported source type strings
     */
    public static String[] getSupportedTypes() {
        return new String[] {
            MultiAgentConfig.ROUTE_ID, MultiAgentConfig.HEADER, MultiAgentConfig.PROPERTY, MultiAgentConfig.VARIABLE
        };
    }

    /**
     * Throws an appropriate UndefinedAgentException based on the agent ID source type
     * configured in the MultiAgentConfig and the extracted agent ID value from the exchange.
     *
     * @param config   The MultiAgentConfig containing the source type configuration
     * @param exchange The Exchange from which to extract the agent ID value
     * @return
     * @throws UndefinedAgentException Always throws - this method never returns normally
     */
    public static UndefinedAgentException newUndefinedAgentException(MultiAgentConfig config, Exchange exchange)
            throws UndefinedAgentException {
        String sourceType = config.multiAgentIdSource();

        switch (sourceType.toLowerCase()) {
            case MultiAgentConfig.ROUTE_ID:
                String routeId = exchange.getFromRouteId();
                return UndefinedAgentException.fromRouteId(routeId);

            case MultiAgentConfig.HEADER:
                String headerName = config.multiAgentIdSourceHeader();
                String headerValue = null;
                if (headerName != null && !headerName.isEmpty()) {
                    headerValue = exchange.getIn().getHeader(headerName, String.class);
                }
                return UndefinedAgentException.fromHeader(headerName, headerValue);

            case MultiAgentConfig.PROPERTY:
                String propertyName = config.multiAgentIdSourceProperty();
                String propertyValue = null;
                if (propertyName != null && !propertyName.isEmpty()) {
                    propertyValue = exchange.getProperty(propertyName, String.class);
                }
                return UndefinedAgentException.fromProperty(propertyName, propertyValue);

            case MultiAgentConfig.VARIABLE:
                String variableName = config.multiAgentIdSourceVariable();
                String variableValue = null;
                if (variableName != null && !variableName.isEmpty()) {
                    variableValue = exchange.getVariable(variableName, String.class);
                }
                return UndefinedAgentException.fromVariable(variableName, variableValue);

            default:
                // Fallback to route ID if source type is unknown
                String fallbackRouteId = exchange.getFromRouteId();
                return UndefinedAgentException.fromRouteId(fallbackRouteId);
        }
    }
}
