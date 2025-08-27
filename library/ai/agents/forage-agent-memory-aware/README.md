# Forage Agent Memory Aware

A memory-aware AI agent implementation for Apache Camel that provides conversational chat functionality with persistent memory support.

## Overview

The `forage-agent-memory-aware` module provides a complete AI agent implementation (`SimpleAgent`) that maintains conversation history using chat memory providers. This agent is ideal for applications requiring multi-turn conversations where context from previous interactions is important.

## Features

- **Memory Support**: Maintains conversation history using configurable chat memory providers
- **Tool Integration**: Supports Apache Camel tool providers for extended functionality
- **RAG Support**: Built-in support for Retrieval Augmented Generation (RAG)
- **Guardrails**: Configurable input and output guardrails for safety and content filtering
- **ServiceLoader Discovery**: Automatically discovered by the default agent factory

## Quick Start

### 1. Add Dependency

```xml
<dependency>
    <groupId>org.apache.camel.forage</groupId>
    <artifactId>forage-agent-memory-aware</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2. Use with Default Agent Factory

The memory-aware agent is automatically discovered and used when you include this module:

```java
from("direct:chat")
    .setBody(constant("My name is Alice"))
    .setHeader(Headers.MEMORY_ID, constant(1))
    .to("langchain4j-agent:memory-agent?agentFactory=#class:org.apache.camel.forage.agent.factory.DefaultAgentFactory")
    .log("Response: ${body}");

from("timer:check?delay=5000&repeatCount=1")
         .setBody(constant("What is my name?"))
        .setHeader(Headers.MEMORY_ID, constant(1))
        .to("langchain4j-agent:test-memory-agent?agentFactory=#class:org.apache.camel.forage.agent.factory.DefaultAgentFactory")
        .log("${body}");
```

The second route should have a response similar to `Your name is Alice.`

### 3. Required Dependencies

For a complete setup, you'll also need:

- A chat model provider (e.g., `forage-model-openai`, `forage-model-google-gemini`)
- A chat memory provider (e.g., `forage-memory-message-window`)
- The default agent factory (`forage-agent-factory-default`)

## Configuration

The SimpleAgent is configured automatically by the `DefaultAgentFactory` using ServiceLoader discovery. Configuration includes:

- **Chat Model**: Automatically discovered from available model providers
- **Chat Memory Provider**: Automatically discovered from available memory factories
- **Retrieval Augmentor**: Optional RAG configuration
- **Guardrails**: Optional input/output filtering

## Implementation Details

### Main Class

- **`SimpleAgent`** (`org.apache.camel.forage.agent.simple.SimpleAgent`)
  - Implements the `Agent` interface from camel-langchain4j-agent-api
  - Implements `ConfigurationAware` for automatic configuration
  - Uses `AiAgentWithMemoryService` for memory-based conversations

### Key Features

- **Memory ID Support**: Each conversation can have a unique memory ID for isolation
- **System Message Support**: Supports both user messages and system messages
- **Thread Safety**: Designed to be thread-safe for concurrent usage
- **Configurable Tools**: Integrates with Apache Camel's tool provider system

## Memory Management

The agent uses the configured chat memory provider to:
- Store conversation history per memory ID
- Retrieve relevant context for responses
- Maintain conversation continuity across interactions

## Comparison with Other Agents

| Feature | Memory Aware | Memoryless |
|---------|--------------|------------|
| Conversation History | ✅ Yes | ❌ No |
| Memory ID Support | ✅ Yes | ❌ No |
| Use Case | Multi-turn conversations | Single interactions |
| Resource Usage | Higher (stores history) | Lower (stateless) |

## Service Registration

This module registers the `SimpleAgent` via ServiceLoader in:
```
META-INF/services/org.apache.camel.component.langchain4j.agent.api.Agent
```

## Requirements

- Java 17+
- Apache Camel 4.14.0+
- LangChain4j 1.2.0+
- A compatible chat model provider
- A compatible chat memory provider

## See Also

- [Forage Agent Memoryless](../forage-agent-memoryless/README.md) - Stateless agent implementation
- [Forage Agent Factory Default](../forage-agent-factory-default/README.md) - Default agent factory
- [Agents Overview](../README.md) - Complete agents documentation