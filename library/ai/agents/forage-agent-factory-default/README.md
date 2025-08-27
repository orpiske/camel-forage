# Forage Agent Factory Default

The default agent factory implementation for Apache Camel that provides automatic discovery and configuration of AI agents using ServiceLoader.

## Overview

The `forage-agent-factory-default` module provides the `DefaultAgentFactory` class, which serves as the main entry point for creating AI agents in the Camel Forage ecosystem. It automatically discovers and wires together agents, model providers, and chat memory factories using Java's ServiceLoader mechanism.

## Features

- **ServiceLoader Discovery**: Automatically discovers agents, model providers, and memory factories
- **Configuration Management**: Handles automatic configuration of discovered components
- **Singleton Pattern**: Ensures only one agent instance is created per factory
- **Flexible Architecture**: Works with any compatible agent implementation
- **Zero Configuration**: Works out-of-the-box with included modules

## Quick Start

### 1. Add Dependency

```xml
<dependency>
    <groupId>org.apache.camel.forage</groupId>
    <artifactId>forage-agent-factory-default</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2. Use in Camel Routes

```java
from("direct:chat")
    .to("langchain4j-agent:my-agent?agentFactory=#class:org.apache.camel.forage.agent.factory.DefaultAgentFactory")
    .log("Response: ${body}");
```

### 3. Complete Setup Example

For a working setup, include these dependencies:

```xml
<!-- Agent factory (this module) -->
<dependency>
    <groupId>org.apache.camel.forage</groupId>
    <artifactId>forage-agent-factory-default</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<!-- Agent implementation -->
<dependency>
    <groupId>org.apache.camel.forage</groupId>
    <artifactId>forage-agent-memory-aware</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<!-- Model provider -->
<dependency>
    <groupId>org.apache.camel.forage</groupId>
    <artifactId>forage-model-openai</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<!-- Memory provider (optional) -->
<dependency>
    <groupId>org.apache.camel.forage</groupId>
    <artifactId>forage-memory-message-window</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## How It Works

The `DefaultAgentFactory` uses ServiceLoader to discover and configure components:

### 1. Discovery Phase
- **Agents**: Discovers implementations of `org.apache.camel.component.langchain4j.agent.api.Agent`
- **Model Providers**: Discovers implementations of `org.apache.camel.forage.core.ai.ModelProvider`
- **Memory Factories**: Discovers implementations of `org.apache.camel.forage.core.ai.ChatMemoryFactory`

### 2. Configuration Phase
- Creates a `ModelProvider` instance for chat model functionality
- Creates a `ChatMemoryFactory` instance for conversation memory (optional)
- Builds an `AgentConfiguration` with the discovered components
- Configures the agent if it implements `ConfigurationAware`

### 3. Agent Creation
- Returns a configured, ready-to-use agent instance
- Uses singleton pattern to ensure one agent per factory instance

## Implementation Details

### Main Classes

- **`DefaultAgentFactory`** (`org.apache.camel.forage.agent.factory.DefaultAgentFactory`)
  - Implements `AgentFactory` from camel-langchain4j-agent-api
  - Manages component discovery and agent creation
  - Thread-safe singleton implementation

- **`ConfigurationAware`** (`org.apache.camel.forage.agent.factory.ConfigurationAware`)
  - Interface for agents that accept configuration
  - Allows automatic configuration of discovered agents

### ServiceLoader Integration

The factory discovers components through standard ServiceLoader files:
- `META-INF/services/org.apache.camel.component.langchain4j.agent.api.Agent`
- `META-INF/services/org.apache.camel.forage.core.ai.ModelProvider`
- `META-INF/services/org.apache.camel.forage.core.ai.ChatMemoryFactory`

## Configuration

The factory automatically configures agents with:

- **Chat Model**: First discovered model provider
- **Chat Memory Provider**: First discovered memory factory (optional)
- **Retrieval Augmentor**: Configured through the agent if supported
- **Guardrails**: Configured through the agent if supported

## Error Handling

The factory handles missing components gracefully:

- **No Agent**: Throws `IllegalStateException`
- **No Model Provider**: Throws `IllegalStateException`
- **No Memory Factory**: Continues without memory (for memoryless agents)

## Extending the Factory

### Creating Custom Agents

To create a custom agent that works with the factory:

1. Implement the `Agent` interface
2. Optionally implement `ConfigurationAware` for automatic configuration
3. Register via ServiceLoader in `META-INF/services/org.apache.camel.component.langchain4j.agent.api.Agent`

Example:
```java
public class MyAgent implements Agent, ConfigurationAware {
    private AgentConfiguration configuration;
    
    @Override
    public void configure(AgentConfiguration configuration) {
        this.configuration = configuration;
    }
    
    @Override
    public String chat(AiAgentBody body, ToolProvider toolProvider) {
        // Implementation
    }
}
```

### Creating Custom Model Providers

1. Implement the `ModelProvider` interface
2. Register via ServiceLoader in `META-INF/services/org.apache.camel.forage.core.ai.ModelProvider`

### Creating Custom Memory Factories

1. Implement the `ChatMemoryFactory` interface
2. Register via ServiceLoader in `META-INF/services/org.apache.camel.forage.core.ai.ChatMemoryFactory`

## Thread Safety

The `DefaultAgentFactory` is thread-safe and uses proper synchronization for agent creation. The singleton pattern ensures that multiple calls to `createAgent()` return the same configured instance.

## Requirements

- Java 17+
- Apache Camel 4.14.0+
- At least one compatible agent implementation
- At least one compatible model provider


## See Also

- [Forage Agent Memory Aware](../forage-agent-memory-aware/README.md) - Memory-enabled agent implementation
- [Forage Agent Memoryless](../forage-agent-memoryless/README.md) - Stateless agent implementation
- [Agents Overview](../README.md) - Complete agents documentation
- [Contributing Beans Guide](../../../../docs/contributing-beans.md) - How to create custom components