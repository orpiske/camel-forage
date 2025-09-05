package org.apache.camel.forage.agent.factory;

import org.apache.camel.Exchange;

/**
 * Agent ID source that extracts the agent ID from a configurable exchange header.
 * The header name is configured via the multi.agent.id.source.header property.
 */
public class HeaderAgentIdSource implements AgentIdSource {

    private final String headerName;

    public HeaderAgentIdSource(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public String extract(Exchange exchange) {
        if (headerName == null || headerName.isEmpty()) {
            return null;
        }

        Object headerValue = exchange.getIn().getHeader(headerName);
        return headerValue != null ? headerValue.toString() : null;
    }

    public String getHeaderName() {
        return headerName;
    }
}
