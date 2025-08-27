# Camel Forage

An opinionated library of beans for Apache Camel that provides pre-configured components for AI integrations and other bean-based components.

## Overview

Camel Forage simplifies the configuration of Apache Camel components by providing ready-to-use beans that follow best practices. The library is particularly focused on AI components, offering seamless integration with various AI models, chat memory providers, and agent factories.

## Features

- **Plug-and-play AI components** - Pre-configured beans for AI models, agents, and memory providers
- **Multiple AI provider support** - Support for OpenAI, Google Gemini, Ollama, and extensible for other providers
- **Chat memory management** - Built-in message window chat memory with persistent storage
- **Agent factory patterns** - Default agent factory with ServiceLoader-based discovery
- **Modular architecture** - Pick only the modules you need

## Quick Start

### 1. Add Dependencies

Add the desired modules to your project. For example, to use the default agent factory with OpenAI:

```xml
<!--This component provides support for OpenAI models (GPT-3.5, GPT-4, etc.)-->
<dependency>
    <groupId>org.apache.camel.forage</groupId>
    <artifactId>camel-forage-model-open-ai</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<!--This component provides support for the message window chat memory -->
<dependency>
    <groupId>org.apache.camel.forage</groupId>
    <artifactId>forage-memory-message-window</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<!--This component adds a memory-aware agent implementation -->
<dependency>
    <groupId>org.apache.camel.forage</groupId>
    <artifactId>forage-agent-memory-aware</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2. Use in Camel Routes

Simply reference the bean class in your Camel route:

```java
from("direct:start")
    .to("langchain4j-agent:test-memory-agent?agentFactory=#class:org.apache.camel.forage.agent.factory.DefaultAgentFactory");
```

The `org.apache.camel.forage.agent.factory.DefaultAgentFactory` class is a default factory that builds AI agent automagically,
based on the dependencies available on the classpath.

## Available Modules

### Core Modules

- **forage-core-ai** - Core interfaces and abstractions for AI components
- **forage-core-agent** - Agent interfaces and base classes

### AI Modules

#### Agents
- **forage-agent-factory-default** - Default agent factory and common agent utilities ([Documentation](library/ai/agents/forage-agent-factory-default/README.md))
- **forage-agent-memory-aware** - Memory-aware agent implementation ([Documentation](library/ai/agents/forage-agent-memory-aware/README.md))
- **forage-agent-memoryless** - Stateless agent implementation ([Documentation](library/ai/agents/forage-agent-memoryless/README.md))

📋 **[Complete Agents Documentation](library/ai/agents/README.md)** - Comprehensive guide to all agent components

#### Models
- **camel-forage-model-open-ai** - OpenAI chat model provider ([Configuration Guide](library/ai/models/chat/camel-forage-model-open-ai/README.md))
- **forage-model-google-gemini** - Google Gemini chat model provider ([Configuration Guide](library/ai/models/chat/forage-model-google-gemini/README.md))
- **forage-model-ollama** - Ollama chat model provider ([Configuration Guide](library/ai/models/chat/forage-model-ollama/README.md))

#### Chat Memory
- **forage-memory-message-window** - Message window chat memory with persistent storage

#### Vector Databases
- **vector-dbs** - Vector database integrations (coming soon)

#### Embeddings
- **embeddings** - Embedding model providers (coming soon)

## Configuration

Camel Forage uses a flexible configuration system that supports multiple sources with a defined precedence hierarchy:

1. **Environment variables** (highest precedence)
2. **System properties** 
3. **Configuration files** (e.g., `forage-model-*.properties`)
4. **Default values** (where applicable)

### Model Configuration

Each AI model provider has its own configuration requirements and options. For detailed configuration instructions, including environment variables, system properties, and configuration files, please refer to the respective model documentation:

- **OpenAI**: See [OpenAI Configuration Guide](library/ai/models/chat/forage-model-open-ai/README.md)
- **Google Gemini**: See [Google Gemini Configuration Guide](library/ai/models/chat/forage-model-google-gemini/README.md)  
- **Ollama**: See [Ollama Configuration Guide](library/ai/models/chat/forage-model-ollama/README.md)

### Quick Configuration Examples

For immediate setup, here are minimal configuration examples:

#### OpenAI
```bash
export OPENAI_API_KEY="sk-your-api-key-here"
export OPENAI_MODEL_NAME="gpt-4"  # Optional, defaults to gpt-3.5-turbo
```

#### Google Gemini
```bash
export GOOGLE_API_KEY="your-google-api-key"
export GOOGLE_MODEL_NAME="gemini-pro"
```

#### Ollama
```bash
export OLLAMA_BASE_URL="http://localhost:11434"  # Optional, this is the default
export OLLAMA_MODEL_NAME="llama3"                # Optional, this is the default
```

## Architecture

Camel Forage uses a ServiceLoader-based discovery mechanism to automatically wire components together:

1. **ModelProvider** - Provides chat models (Ollama, Google Gemini, etc.)
2. **ChatMemoryFactory** - Creates chat memory providers for conversation persistence
3. **AgentFactory** - Orchestrates the creation of agents with models and memory
4. **Agent** - The actual agent implementation

The `DefaultAgentFactory` automatically discovers and combines these components using Java's ServiceLoader mechanism.

## Examples

### Basic AI Agent Route

Example for a memory-less agent.

```java
from("timer:ai?period=30000")
    .setBody(constant("Tell me a joke"))
    .to("langchain4j-agent:joke-agent?agentFactory=#class:org.apache.camel.forage.agent.factory.DefaultAgentFactory")
    .log("AI Response: ${body}");
```

### Custom Configuration

```java
@Component
public class MyRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:chat")
            .to("langchain4j-agent:chat-agent?agentFactory=#class:org.apache.camel.forage.agent.factory.DefaultAgentFactory")
            .log("Chat response: ${body}");
    }
}
```

## Requirements

- Java 17+
- Apache Camel 4.14.0+
- LangChain4j 1.2.0+

## Contributing

This project follows standard Maven conventions. To build:

```bash
mvn clean install
```

### Integrating Apache Camel Components

If you're developing Apache Camel components and want to integrate them with the Camel Forage library, or if you want to create new providers for AI models, vector databases, or other services, please refer to our comprehensive [Contributing Beans Guide](docs/contributing-beans.md).

This guide is essential reading for:
- **Component developers** who want to add factory-based configuration to their Apache Camel components
- **Library contributors** who want to create new AI model providers, vector database integrations, or other service providers
- **Maintainers** who need to understand the architecture and patterns used throughout the Forage library

## License

This project is licensed under the Apache License 2.0.