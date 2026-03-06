# Forage Agents

AI agent implementations for Apache Camel providing conversational chat functionality with different memory and configuration options.

## Overview

The agents module provides a complete ecosystem for AI agent functionality in Apache Camel, including agent implementations, factory patterns, and automatic discovery mechanisms. All agents are designed to work seamlessly with the broader Forage library ecosystem.

## Available Agents

### [forage-agent-factories](forage-agent-factories/README.md)
**Agent Factories**

The main entry point for creating AI agents with automatic component discovery and multi-agent orchestration.

- **Purpose**: ServiceLoader-based agent factories for automatic configuration and multi-agent coordination
- **Key Features**: Zero-configuration setup, component discovery, singleton pattern, multi-agent support
- **Use Case**: Primary factory for most applications, multi-agent systems
- **Dependencies**: Core interfaces, camel-langchain4j-agent-api

### [forage-agent](forage-agent/README.md)
**Composable Agent**

Flexible agent implementation that can work with or without memory based on configuration.

- **Purpose**: Configurable agent for both memory-aware and memoryless scenarios
- **Key Features**: Composable design, memory support (optional), RAG, guardrails
- **Use Case**: Universal agent for chatbots, API endpoints, conversational AI
- **Dependencies**: Agent factories, langchain4j

## Quick Start

### 1. Add Core Agent Dependencies

**Composable agent (works with or without memory):**
```xml
<dependency>
    <groupId>io.kaoto.forage</groupId>
    <artifactId>forage-agent</artifactId>
    <version>1.1-SNAPSHOT</version>
</dependency>
```

### 2. Add Required Dependencies

**Model provider (choose one):**
```xml
<!-- OpenAI -->
<dependency>
    <groupId>io.kaoto.forage</groupId>
    <artifactId>forage-model-open-ai</artifactId>
    <version>1.1-SNAPSHOT</version>
</dependency>

<!-- Google Gemini -->
<dependency>
    <groupId>io.kaoto.forage</groupId>
    <artifactId>forage-model-google-gemini</artifactId>
    <version>1.1-SNAPSHOT</version>
</dependency>

<!-- Ollama -->
<dependency>
    <groupId>io.kaoto.forage</groupId>
    <artifactId>forage-model-ollama</artifactId>
    <version>1.1-SNAPSHOT</version>
</dependency>
```

**Memory provider (optional for memory-enabled conversations):**
```xml
<dependency>
    <groupId>io.kaoto.forage</groupId>
    <artifactId>forage-memory-message-window</artifactId>
    <version>1.1-SNAPSHOT</version>
</dependency>
```

### 3. Use in Camel Routes

To check how it can be used in Camel routes, please read the specific agent documentation you want to use.

## Agent Comparison

| Feature | Composable Agent | Agent Factories |
|---------|------------------|-----------------|
| **Type** | Agent Implementation | Agent Factory |
| **Memory Support** | ✅ Configurable (with/without) | 🔧 Configures agents |
| **Multi-Agent Coordination** | ❌ Single agent | ✅ Multiple agents |
| **Use Case** | Universal (memory or memoryless) | Component discovery & multi-agent |
| **Resource Usage** | Variable (depends on config) | Minimal (factory only) |
| **Memory ID** | 🔧 Optional (when memory configured) | 🔧 Passes through |
| **Conversation History** | 🔧 Optional (when memory configured) | 🔧 Depends on agent |
| **Thread Safety** | ✅ Yes | ✅ Yes |
| **RAG Support** | ✅ Yes | 🔧 Configures |
| **Guardrails** | ✅ Yes | 🔧 Configures |

## Architecture

The agents follow a layered architecture:

```
┌─────────────────────────────────────┐
│           Camel Routes              │
├─────────────────────────────────────┤
│      langchain4j-agent Component    │
├─────────────────────────────────────┤
│       Agent Factories               │
│  └─ MultiAgentFactory               │
├─────────────────────────────────────┤
│  Agent Implementation               │
│  └─ SimpleAgent (Composable)        │
├─────────────────────────────────────┤
│  Core Services (via ServiceLoader)  │
│  ├─ ModelProvider                   │
│  ├─ ChatMemoryFactory (Optional)    │
│  └─ Other Extensions                │
└─────────────────────────────────────┘
```

## Configuration

### Automatic Configuration
All agents support automatic configuration through the agent factories:
- **Model Provider**: Automatically discovered and configured
- **Memory Provider**: Automatically discovered (optional)
- **RAG**: Configured through agent implementation
- **Guardrails**: Configured through agent implementation
- **Multi-Agent**: Configured through `MultiAgentFactory`

### Named/Prefixed Configuration
Starting with version 1.0, all configuration classes support named/prefixed configurations for multi-instance setups:

```java
// Default configuration (uses standard environment variables)
OpenAIConfig defaultConfig = new OpenAIConfig();

// Named configurations (use prefixed environment variables and properties)
OpenAIConfig agentConfig = new OpenAIConfig("agent1");
OpenAIConfig apiConfig = new OpenAIConfig("api");
MilvusConfig vectorDbConfig = new MilvusConfig("rag-system");
```

#### Configuration Sources and Precedence

All configuration classes follow the same precedence order:

1. **Environment variables** (highest precedence)
2. **System properties** 
3. **Configuration files** (`<module-name>.properties`)
4. **Default values** (where applicable)

#### Multi-Instance Configuration Examples

```bash
# Environment variables
export FORAGE_OLLAMA_BASE_URL="http://localhost:11434"
export FORAGE_OLLAMA_MODEL_NAME="granite4:3b"

# Named configurations (with prefix)
export FORAGE_AGENT1_OLLAMA_BASE_URL="http://server1:11434"

# System properties
-Dforage.ollama.base.url=http://localhost:11434
-Dforage.ollama.model.name=granite4:3b
```

#### Configuration Files

Each module can also be configured via properties files:

```properties
# forage-agent-factory.properties (or application.properties)
forage.ollama.agent.model.kind=ollama
forage.ollama.agent.model.name=granite4:3b
forage.ollama.agent.base.url=http://localhost:11434
forage.ollama.agent.features=memory
forage.ollama.agent.memory.kind=message-window
forage.ollama.agent.memory.max.messages=20
```

#### Provider Factory Integration

The agent factories automatically use prefixed configurations when creating providers:

```java
// In Agent Factories
ModelProvider provider = new OpenAIProvider();
ChatModel model = provider.create("agent1");  // Uses OpenAIConfig("agent1")

ChatMemoryFactory memoryFactory = new RedisMemoryFactory();  
ChatMemoryProvider memory = memoryFactory.create("agent1");  // Uses RedisConfig("agent1")
```

### Manual Configuration
For advanced scenarios, you can create custom agent factories or configure agents directly.

## Examples

### Memory-Enabled Chat Bot

```java
public class ChatBotRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:conversation?period=30s")
            .setHeader("CamelLangChain4jAgent.memoryId", constant("user-123"))
            .setBody(constant("How are you today?"))
            .to("langchain4j-agent:chatbot?agent=#ollama")
            .log("Bot: ${body}");
    }
}
```

### Stateless API Endpoint

```java
public class ApiRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("rest:post:/ask")
            .to("langchain4j-agent:api?agent=#api")
            .log("API Response: ${body}");
    }
}
```

### Batch Processing

```java
from("file:questions?noop=true")
    .split(body().tokenize("\n"))
    .to("langchain4j-agent:processor?agent=#processor")
    .to("file:answers");
```

## ServiceLoader Integration

All agents and factories use Java's ServiceLoader for automatic discovery:

- **Agents**: Registered in `META-INF/services/org.apache.camel.component.langchain4j.agent.api.Agent`
- **Model Providers**: Registered in `META-INF/services/io.kaoto.forage.core.ai.ModelProvider`
- **Memory Factories**: Registered in `META-INF/services/io.kaoto.forage.core.ai.ChatMemoryBeanProvider`

## Extending the Agents

### Creating Custom Agents

1. Implement the `Agent` interface
2. Optionally implement `ConfigurationAware`
3. Register via ServiceLoader
4. Follow the patterns established by existing agents

### Creating Custom Factories

1. Implement the `AgentFactory` interface
2. Handle component discovery and configuration
3. Ensure thread safety for concurrent usage

## Error Handling

The agents provide comprehensive error handling:

- **Missing Components**: Clear error messages for missing dependencies
- **Configuration Errors**: Detailed logging for configuration issues
- **Runtime Errors**: Proper exception propagation with context

## Performance Considerations

- **With Memory**: Higher memory usage due to conversation storage
- **Without Memory**: Optimal for high-throughput scenarios
- **Multi-Agent**: Variable based on agent count and coordination
- **Factories**: Minimal overhead with singleton pattern

## Requirements

- Java 17+
- Apache Camel 4.14.0+
- LangChain4j 1.2.0+

## See Also

- [Multi-Agent Development Guide](../../../docs/multi-agent-guide.md) - Complete guide for creating multi-agent systems with Camel and Kaoto
- [Agent Factories Documentation](forage-agent-factories/README.md) - Factory implementations and multi-agent support
- [Composable Agent Documentation](forage-agent/README.md) - Detailed agent configuration
- [Model Providers](../models/chat/README.md) - Available AI model integrations
- [Memory Providers](../chat-memory/README.md) - Chat memory implementations
- [Contributing Beans Guide](../../../docs/contributing-beans.md) - Creating custom components
- [Main Documentation](../../../README.md) - Complete project overview