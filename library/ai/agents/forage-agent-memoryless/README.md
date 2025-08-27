# Forage Agent Memoryless

A stateless AI agent implementation for Apache Camel that provides chat functionality without persistent memory support.

## Overview

The `forage-agent-memoryless` module provides a lightweight AI agent implementation (`MemorylessAgent`) that handles single-turn conversations without maintaining conversation history. This agent is ideal for applications where each interaction is independent and memory is not required.

## Features

- **Stateless Operation**: No conversation history or memory overhead
- **Tool Integration**: Supports Apache Camel tool providers for extended functionality
- **RAG Support**: Built-in support for Retrieval Augmented Generation (RAG)
- **Guardrails**: Configurable input and output guardrails for safety and content filtering
- **ServiceLoader Discovery**: Automatically discovered by the default agent factory
- **Lightweight**: Minimal resource usage without memory storage

## Quick Start

### 1. Add Dependency

```xml
<dependency>
    <groupId>org.apache.camel.forage</groupId>
    <artifactId>forage-agent-memoryless</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2. Use with Default Agent Factory

The memoryless agent is automatically discovered and used when you include this module:

```java
from("direct:query")
    .to("langchain4j-agent:transient-agent?agentFactory=#class:org.apache.camel.forage.agent.factory.DefaultAgentFactory")
    .log("Response: ${body}");
```

### 3. Required Dependencies

For a complete setup, you'll also need:

- A chat model provider (e.g., `forage-model-openai`, `forage-model-google-gemini`)
- The default agent factory (`forage-agent-factory-default`)

**Note**: Chat memory providers are not required for this agent.

## Configuration

The MemorylessAgent is configured automatically by the `DefaultAgentFactory` using ServiceLoader discovery. Configuration includes:

- **Chat Model**: Automatically discovered from available model providers
- **Retrieval Augmentor**: Optional RAG configuration
- **Guardrails**: Optional input/output filtering

**Note**: Chat memory providers are ignored even if available.

## Implementation Details

### Main Class

- **`MemorylessAgent`** (`org.apache.camel.forage.agent.memoryless.MemorylessAgent`)
  - Implements the `Agent` interface from camel-langchain4j-agent-api
  - Implements `ConfigurationAware` for automatic configuration
  - Uses custom `AiAgentService` interface without memory support

### Key Features

- **No Memory ID**: Memory IDs in requests are ignored
- **System Message Support**: Supports both user messages and system messages
- **Thread Safety**: Designed to be thread-safe for concurrent usage
- **Configurable Tools**: Integrates with Apache Camel's tool provider system
- **Custom Service Interface**: Defines `AiAgentService` interface without memory methods

## Memory Management

This agent intentionally does not use any memory:
- No conversation history is stored
- Each request is processed independently
- No memory ID support
- Lower resource usage

## Comparison with Other Agents

| Feature | Memory Aware | Memoryless |
|---------|--------------|------------|
| Conversation History | ✅ Yes | ❌ No |
| Memory ID Support | ✅ Yes | ❌ No |
| Use Case | Multi-turn conversations | Single interactions |
| Resource Usage | Higher (stores history) | Lower (stateless) |

## Use Cases

The memoryless agent is ideal for:

- **API Endpoints**: Simple question-answer scenarios
- **Batch Processing**: Processing independent items without context
- **Stateless Services**: Microservices that don't maintain state
- **High-Throughput Scenarios**: When memory overhead is a concern
- **Simple Queries**: One-off questions that don't require context

## Examples

### Basic Usage

```java
from("timer:questions?period=10s")
    .setBody(constant("What is the capital of France?"))
    .to("langchain4j-agent:query?agentFactory=#class:org.apache.camel.forage.agent.factory.DefaultAgentFactory")
    .log("Answer: ${body}");
```

### With System Message

```java
from("direct:translate")
    .setHeader("CamelLangChain4jAgent.systemMessage", constant("You are a French translator"))
    .to("langchain4j-agent:translator?agentFactory=#class:org.apache.camel.forage.agent.factory.DefaultAgentFactory")
    .log("Translation: ${body}");
```

### Batch Processing

```java
from("file:input?noop=true")
    .split(body().tokenize("\n"))
    .setHeader("CamelLangChain4jAgent.systemMessage", constant("Summarize this text in one sentence"))
    .to("langchain4j-agent:summarizer?agentFactory=#class:org.apache.camel.forage.agent.factory.DefaultAgentFactory")
    .to("file:output");
```

## Service Registration

This module registers the `MemorylessAgent` via ServiceLoader in:
```
META-INF/services/org.apache.camel.component.langchain4j.agent.api.Agent
```

## Requirements

- Java 17+
- Apache Camel 4.14.0+
- LangChain4j 1.2.0+
- A compatible chat model provider

## Dependencies

This module depends on:
- `forage-agent-factory-default` - For configuration interfaces
- `langchain4j` - For AI service building

## See Also

- [Forage Agent Memory Aware](../forage-agent-memory-aware/README.md) - Memory-enabled agent implementation
- [Forage Agent Factory Default](../forage-agent-factory-default/README.md) - Default agent factory
- [Agents Overview](../README.md) - Complete agents documentation