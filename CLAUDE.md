# Camel Forage - Project Guide for Claude

This document provides Claude with comprehensive information about the Camel Forage project structure, patterns, and conventions.

## Project Overview

**Camel Forage** is an opinionated library of beans for Apache Camel that provides pre-configured components for AI integrations and other bean-based components. The library simplifies Apache Camel configuration by providing ready-to-use beans that follow best practices, particularly focused on AI components.

## Technology Stack

- **Java**: 17+
- **Apache Camel**: 4.14.0
- **LangChain4j**: 1.2.0 (with beta 1.3.0-beta9 for some features)
- **Build Tool**: Maven
- **Code Formatting**: Spotless with Palantir Java Format
- **Testing**: JUnit 5, AssertJ

## Project Structure

```
camel-forage/
├── core/                                    # Core interfaces and utilities
│   ├── forage-core-ai/                     # AI core interfaces
│   ├── forage-core-common/                 # Common utilities (Config system)
│   └── forage-core-vectordb/               # Vector database interfaces
├── library/                                # Implementation modules
│   └── ai/
│       ├── agents/                         # AI agent implementations
│       │   ├── forage-agent-factory-default/
│       │   ├── forage-agent-memory-aware/
│       │   └── forage-agent-memoryless/
│       ├── chat-memory/                    # Chat memory providers
│       │   └── forage-memory-message-window/
│       ├── models/chat/                    # AI model providers
│       │   ├── forage-model-google-gemini/
│       │   ├── forage-model-ollama/
│       │   └── forage-model-open-ai/
│       └── vector-dbs/                     # Vector database providers
│           ├── forage-vectordb-milvus/
│           ├── forage-vectordb-pgvector/
│           ├── forage-vectordb-pinecone/
│           ├── forage-vectordb-qdrant/
│           └── forage-vectordb-weaviate/
├── docs/                                   # Documentation
│   ├── contributing-beans.md
│   └── structure.md
└── README.md
```

## Key Architectural Patterns

### 1. ServiceLoader Discovery Pattern

The project heavily uses Java's ServiceLoader mechanism for component discovery:

- **Agents**: `org.apache.camel.component.langchain4j.agent.api.Agent`
- **Model Providers**: `org.apache.camel.forage.core.ai.ModelProvider`
- **Memory Factories**: `org.apache.camel.forage.core.ai.ChatMemoryFactory`
- **Vector DB Providers**: `org.apache.camel.forage.core.vectordb.EmbeddingStoreProvider`

### 2. Factory Pattern

Core interfaces define factory contracts:
- `AgentFactory` - Creates AI agents
- `ModelProvider` - Creates chat models
- `ChatMemoryFactory` - Creates memory providers
- `EmbeddingStoreProvider` - Creates vector databases

### 3. Configuration System

All modules use a consistent configuration pattern:

#### Configuration Classes
All configuration classes implement `org.apache.camel.forage.core.util.config.Config` interface with:
- `String name()` - Unique module identifier
- `void register(String name, String value)` - Property registration method

#### Configuration Pattern
```java
public class ExampleConfig implements Config {
    private static final ConfigModule API_KEY = ConfigModule.of(ExampleConfig.class, "api-key");
    
    public ExampleConfig() {
        ConfigStore.getInstance().add(API_KEY, ConfigEntry.fromEnv("EXAMPLE_API_KEY"));
        ConfigStore.getInstance().add(ExampleConfig.class, this, this::register);
    }
    
    @Override
    public String name() {
        return "forage-module-example";
    }
    
    private ConfigModule resolve(String name) {
        if (API_KEY.name().equals(name)) return API_KEY;
        throw new IllegalArgumentException("Unknown config entry: " + name);
    }
    
    @Override
    public void register(String name, String value) {
        ConfigModule config = resolve(name);
        ConfigStore.getInstance().set(config, value);
    }
}
```

#### Configuration Sources (in precedence order)
1. Environment variables (highest precedence)
2. System properties
3. Configuration files (`<module-name>.properties`)
4. Default values (where applicable)

## Module Categories

### Core Modules

#### forage-core-ai
Contains fundamental AI interfaces:
- `ModelProvider` - For creating chat models
- `ChatMemoryFactory` - For creating memory providers

#### forage-core-common
Contains shared utilities:
- `Config` interface and configuration system
- `ConfigStore`, `ConfigModule`, `ConfigEntry` classes

#### forage-core-vectordb
Contains vector database interfaces:
- `EmbeddingStoreProvider` - For creating vector databases

### Library Modules

- Each module is organized in a topic (i.e: `ai`) and a sub-topic (i.e; `agents`, `models`, etc)


#### AI Agents
- **forage-agent-factory-default**: Default factory with ServiceLoader discovery
- **forage-agent-memory-aware**: Agent with conversation memory (uses `AiAgentWithMemoryService`)
- **forage-agent-memoryless**: Stateless agent (uses custom `AiAgentService`)

#### AI Models
- **forage-model-openai**: OpenAI integration (GPT models)
- **forage-model-google-gemini**: Google Gemini integration
- **forage-model-ollama**: Ollama local model integration

#### Chat Memory
- **forage-memory-message-window**: Message window memory with persistence

#### Vector Databases
- **forage-vectordb-milvus**: Milvus integration
- **forage-vectordb-pgvector**: PostgreSQL pgvector integration
- **forage-vectordb-pinecone**: Pinecone integration
- **forage-vectordb-qdrant**: Qdrant integration
- **forage-vectordb-weaviate**: Weaviate integration

## Naming Conventions

### Maven Artifacts
- Core: `forage-core-<category>`
- Libraries: `forage-<category>-<technology>`
- Special: `camel-forage-<technology>` (for direct Camel components)

### Package Structure
- Core: `org.apache.camel.forage.core.<category>`
- Library: `org.apache.camel.forage.<category>.<technology>`

### Configuration
- Environment variables: `<TECHNOLOGY>_<PROPERTY>` (e.g., `OPENAI_API_KEY`)
- System properties: `<technology>.<property>` (e.g., `openai.api.key`)
- Config modules: `<technology>.<property>` (e.g., `openai.api.key`)

## Common Implementation Tasks

### Creating a New Model Provider

1. **Create provider class**:
```java
public class NewModelProvider implements ModelProvider {
    private final NewModelConfig config;
    
    public NewModelProvider() {
        this.config = new NewModelConfig();
    }
    
    @Override
    public ChatModel newModel() {
        return NewModelClient.builder()
            .apiKey(config.apiKey())
            .modelName(config.modelName())
            .build();
    }
}
```

2. **Create configuration class** (following the pattern above)

3. **Register with ServiceLoader**:
   - File: `META-INF/services/org.apache.camel.forage.core.ai.ModelProvider`
   - Content: `org.apache.camel.forage.models.chat.newmodel.NewModelProvider`

### Creating a New Vector Database Provider

1. **Create provider class**:
```java
public class NewVectorDbProvider implements EmbeddingStoreProvider {
    private final NewVectorDbConfig config;
    
    public NewVectorDbProvider() {
        this.config = new NewVectorDbConfig();
    }
    
    @Override
    public EmbeddingStore<TextSegment> newEmbeddingStore() {
        return NewVectorDb.builder()
            .host(config.host())
            .port(config.port())
            .build();
    }
}
```

2. **Create configuration class** (following the pattern above)

3. **Register with ServiceLoader**:
   - File: `META-INF/services/org.apache.camel.forage.core.vectordb.EmbeddingStoreProvider`
   - Content: `org.apache.camel.forage.vectordb.newdb.NewVectorDbProvider`

## Configuration Examples

### OpenAI Configuration
```bash
# Environment variables
export OPENAI_API_KEY="sk-..."
export OPENAI_MODEL_NAME="gpt-4"

# System properties
-Dopenai.api.key=sk-...
-Dopenai.model.name=gpt-4

# forage-model-openai.properties
api-key=sk-...
model-name=gpt-4
```

### Ollama Configuration
```bash
# Environment variables
export OLLAMA_BASE_URL="http://localhost:11434"
export OLLAMA_MODEL_NAME="llama3"
export OLLAMA_TEMPERATURE="0.7"

# System properties
-Dollama.base.url=http://localhost:11434
-Dollama.model.name=llama3
-Dollama.temperature=0.7
```

## Usage Patterns

### Basic Agent Usage
```java
from("direct:chat")
    .to("langchain4j-agent:my-agent?agentFactory=#class:org.apache.camel.forage.agent.factory.DefaultAgentFactory")
    .log("Response: ${body}");
```

### Memory-Aware Agent
```java
from("direct:memory-chat")
    .setHeader(Headers.MEMORY_ID, constant(1))
    .to("langchain4j-agent:chat?agentFactory=#class:org.apache.camel.forage.agent.factory.DefaultAgentFactory")
    .log("Response: ${body}");
```

## Build Commands

```bash
# Clean build
mvn clean compile

# Run tests
mvn verify

# Apply code formatting
mvn spotless:apply

# Check code formatting
mvn spotless:check

# Full build with tests
mvn clean install
```

## Important Notes for Development

### Code Style
- Uses Palantir Java Format via Spotless
- Automatic formatting applied during compile phase
- No additional comments should be added unless explicitly requested

### Configuration Classes Must Include
1. `resolve(String name)` method mapping property names to ConfigModules
2. `register(String name, String value)` method implementing the Config interface
3. Constructor registration: `ConfigStore.getInstance().add(ConfigClass.class, this, this::register)`

### ServiceLoader Registration
- Always create `META-INF/services/<interface-name>` files
- List full class names of implementations
- Essential for automatic discovery by factories

### Error Handling
- Use `MissingConfigException` for required missing configuration
- Provide clear error messages with context
- Document all configuration parameters thoroughly

### Testing
- Use JUnit 5 and AssertJ
- Test ServiceLoader discovery mechanisms
- Verify configuration source precedence

## Dependencies to Include

When creating new modules, typical dependencies include:

```xml
<!-- Core dependencies -->
<dependency>
    <groupId>org.apache.camel.forage</groupId>
    <artifactId>forage-core-common</artifactId>
</dependency>

<!-- For AI modules -->
<dependency>
    <groupId>org.apache.camel.forage</groupId>
    <artifactId>forage-core-ai</artifactId>
</dependency>

<!-- For vector DB modules -->
<dependency>
    <groupId>org.apache.camel.forage</groupId>
    <artifactId>forage-core-vectordb</artifactId>
</dependency>

<!-- LangChain4j -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId>
</dependency>
```

This project follows a highly structured approach with clear separation of concerns, consistent patterns, and comprehensive configuration management. All new components should follow these established patterns for consistency and maintainability.
