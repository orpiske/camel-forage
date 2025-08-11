# Camel Forage

An opinionated library of beans for Apache Camel that provides pre-configured components for AI integrations and other bean-based components.

## Overview

Camel Forage simplifies the configuration of Apache Camel components by providing ready-to-use beans that follow best practices. The library is particularly focused on AI components, offering seamless integration with various AI models, chat memory providers, and agent factories.

## Features

- **Plug-and-play AI components** - Pre-configured beans for AI models, agents, and memory providers
- **Multiple AI provider support** - Support for Google Gemini, Ollama, and extensible for other providers
- **Chat memory management** - Built-in message window chat memory with persistent storage
- **Agent factory patterns** - Default agent factory with ServiceLoader-based discovery
- **Modular architecture** - Pick only the modules you need

## Quick Start

### 1. Add Dependencies

Add the desired modules to your project. For example, to use the default agent factory with Google Gemini:

```xml
<!--This component provides support for the Google Gemini family of models-->
<dependency>
    <groupId>org.apache.camel.forage</groupId>
    <artifactId>forage-model-google-gemini</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<!--This component provides support for the message window chat memory -->
<dependency>
    <groupId>org.apache.camel.forage</groupId>
    <artifactId>forage-memory-message-window</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<!--This component adds a simple agent implementation -->
<dependency>
    <groupId>org.apache.camel.forage</groupId>
    <artifactId>forage-agent-simple</artifactId>
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
- **forage-agent-factory-default** - Default agent factory and common agent utilities
- **forage-agent-simple** - Basic agent implementation

#### Models
- **forage-model-google-gemini** - Google Gemini chat model provider
- **forage-model-ollama** - Ollama chat model provider

#### Chat Memory
- **forage-memory-message-window** - Message window chat memory with persistent storage

#### Vector Databases
- **vector-dbs** - Vector database integrations (coming soon)

#### Embeddings
- **embeddings** - Embedding model providers (coming soon)

## Configuration

### Environment Variables

Some providers use environment variables for configuration:

#### Google Gemini
```bash
export GOOGLE_API_KEY="your-google-api-key"
export GOOGLE_MODEL_NAME="gemini-1.5-flash"
```

### Bean Configuration

You can also configure providers programmatically:

```java
@Bean
public ModelProvider customOllamaProvider() {
    OllamaProvider provider = new OllamaProvider();
    provider.setBaseUrl("http://your-ollama-server:11434");
    provider.setModelName("llama3");
    return provider;
}
```

> [NOTE]
> This is untested

## Architecture

Camel Forage uses a ServiceLoader-based discovery mechanism to automatically wire components together:

1. **ModelProvider** - Provides chat models (Ollama, Google Gemini, etc.)
2. **ChatMemoryFactory** - Creates chat memory providers for conversation persistence
3. **AgentFactory** - Orchestrates the creation of agents with models and memory
4. **Agent** - The actual agent implementation

The `DefaultAgentFactory` automatically discovers and combines these components using Java's ServiceLoader mechanism.

## Examples

### Basic AI Agent Route

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

## License

This project is licensed under the Apache License 2.0.