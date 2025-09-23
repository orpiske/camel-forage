# Camel Forage - Project Guide for Claude

This document provides Claude with comprehensive information about the Camel Forage project structure, patterns, and conventions.

## Project Overview

**Camel Forage** is a plugin extension for Apache Camel that provides opinionated bean factories for simplified component configuration across various domains including AI, JDBC, and other bean-based components. The library simplifies Apache Camel configuration by providing factory classes that create configured beans through properties-based configuration, eliminating the need for manual Java bean instantiation.

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
│       │   ├── forage-agent-factories/
│       │   └── forage-agent/
│       ├── chat-memory/                    # Chat memory providers
│       │   └── forage-memory-message-window/
│       ├── models/chat/                    # AI model providers
│       │   ├── forage-model-anthropic/
│       │   ├── forage-model-azure-openai/
│       │   ├── forage-model-dashscope/
│       │   ├── forage-model-google-gemini/
│       │   ├── forage-model-hugging-face/
│       │   ├── forage-model-local-ai/
│       │   ├── forage-model-mistral-ai/
│       │   ├── forage-model-ollama/
│       │   ├── forage-model-open-ai/
│       │   └── forage-model-watsonx-ai/
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
`- **Memory Factories**: `org.apache.camel.forage.core.ai.ChatMemoryBeanProvider`
- **Vector DB Providers**: `org.apache.camel.forage.core.vectordb.EmbeddingStoreProvider`

### 2. Bean Provider Pattern

All bean creation follows a consistent pattern through the `BeanProvider<T>` interface:

#### ForageBean Annotation Requirement
**IMPORTANT**: All provider classes must be annotated with `@ForageBean` for proper identification and catalog generation:

```java
@ForageBean(value = "provider-name", component = "supported-component", description = "Provider description")
public class ExampleProvider implements ProviderInterface {
    // Implementation
}
```

The annotation parameters:
- `value`: Unique identifier for the bean (e.g., "azure-openai", "qdrant", "message-window")
- `component`: Supported Camel component (e.g., "camel-langchain4j-agent", "camel-langchain4j-embeddings")
- `description`: Human-readable description of what the provider does

#### ForageFactory Annotation Requirement
**IMPORTANT**: All factory classes must be annotated with `@ForageFactory` for proper identification and catalog generation:

```java
@ForageFactory(
    value = "factory-name", 
    component = "supported-component", 
    description = "Factory description",
    factoryType = "CreatedObjectType")
public class ExampleFactory implements FactoryInterface {
    // Implementation
}
```

The annotation parameters:
- `value`: Unique identifier for the factory (e.g., "default-agent", "multi-agent", "redis-memory")
- `component`: Supported Camel component (e.g., "camel-langchain4j-agent", "camel-langchain4j-embeddings")
- `description`: Human-readable description of what the factory creates
- `factoryType`: Type of objects this factory creates (e.g., "Agent", "ChatMemoryProvider", "EmbeddingStore")

#### Core BeanProvider Interface
```java
public interface BeanProvider<T> {
    default T create() { return create(null); }
    T create(String id);
}
```

#### Specialized Provider Interfaces
Core interfaces extend `BeanProvider` for type safety:
- `AgentFactory` - Creates AI agents
- `ModelProvider extends BeanProvider<ChatModel>` - Creates chat models with optional prefix support
- `ChatMemoryFactory extends BeanProvider<ChatMemoryProvider>` - Creates memory providers with optional prefix support
- `EmbeddingStoreProvider extends BeanProvider<EmbeddingStore<TextSegment>>` - Creates vector databases with optional prefix support

#### Provider Method Pattern
All providers implement both methods:
- `create()` - Creates bean with default configuration
- `create(String id)` - Creates bean with named/prefixed configuration

### 3. Configuration System

All modules use a consistent configuration pattern:

#### Configuration Classes
All configuration classes implement `org.apache.camel.forage.core.util.config.Config` interface with:
- `String name()` - Unique module identifier
- `void register(String name, String value)` - Property registration method

#### Configuration Pattern (Current Implementation)
Starting with version 1.0, configuration classes use a two-class pattern supporting named/prefixed configurations:

**1. ConfigEntries Class:**
```java
public final class ExampleConfigEntries extends ConfigEntries {
    public static final ConfigModule API_KEY = ConfigModule.of(ExampleConfig.class, "example.api.key");
    
    private static final Map<ConfigModule, ConfigEntry> CONFIG_MODULES = new ConcurrentHashMap<>();
    
    static {
        init();
    }
    
    static void init() {
        CONFIG_MODULES.put(API_KEY, ConfigEntry.fromModule());
    }
    
    public static Map<ConfigModule, ConfigEntry> entries() {
        return Collections.unmodifiableMap(CONFIG_MODULES);
    }
    
    public static Optional<ConfigModule> find(String prefix, String name) {
        return find(CONFIG_MODULES, prefix, name);
    }
    
    /**
     * Registers new known configuration if a prefix is provided (otherwise is ignored)
     * @param prefix the prefix to register
     */
    public static void register(String prefix) {
        if (prefix != null) {
            for (Map.Entry<ConfigModule, ConfigEntry> entry : entries().entrySet()) {
                ConfigModule configModule = entry.getKey().asNamed(prefix);
                CONFIG_MODULES.put(configModule, ConfigEntry.fromModule());
            }
        }
    }
    
    /**
     * Load override configurations (which are defined via environment variables and/or system properties)
     * @param prefix and optional prefix to use
     */
    public static void loadOverrides(String prefix) {
        load(CONFIG_MODULES, prefix);
    }
}
```

**2. Main Config Class:**
```java
public class ExampleConfig implements Config {
    private final String prefix;
    
    public ExampleConfig() {
        this(null);
    }
    
    public ExampleConfig(String prefix) {
        this.prefix = prefix;
        
        // First register new configuration modules. This happens only if a prefix is provided
        ExampleConfigEntries.register(prefix);

        // Then, loads the configurations from the properties file associated with this Config module
        ConfigStore.getInstance().load(ExampleConfig.class, this, this::register);

        // Lastly, load the overrides defined in system properties and environment variables
        ExampleConfigEntries.loadOverrides(prefix);
    }
    
    @Override
    public void register(String name, String value) {
        Optional<ConfigModule> config = ExampleConfigEntries.find(prefix, name);
        
        config.ifPresent(module -> ConfigStore.getInstance().set(module, value));
    }
    
    @Override
    public String name() {
        return "forage-module-example";
    }
    
    public String apiKey() {
        return ConfigStore.getInstance()
                .get(ExampleConfigEntries.API_KEY.asNamed(prefix))
                .orElseThrow(() -> new MissingConfigException("Missing API key"));
    }
}
```

#### Named/Prefixed Configuration Support
All configuration classes now support prefixed configurations for multi-instance setups:

```java
// Default configuration
ExampleConfig defaultConfig = new ExampleConfig();

// Named configurations
ExampleConfig agent1Config = new ExampleConfig("agent1");
ExampleConfig agent2Config = new ExampleConfig("agent2");
```

This allows different configurations:
```bash
# Environment variables
export EXAMPLE_API_KEY="default-key"              # Used by default config
export agent1.example.api.key="agent1-key"       # Used by "agent1" config
export agent2.example.api.key="agent2-key"       # Used by "agent2" config

# System properties
-Dexample.api.key=default-key                     # Default config
-Dagent1.example.api.key=agent1-key              # "agent1" config
-Dagent2.example.api.key=agent2-key              # "agent2" config
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
- **forage-agent-factories**: Agent factories with ServiceLoader discovery and multi-agent support
- **forage-agent**: Composable agent with optional memory support (uses `AiAgentWithMemoryService` or `AiAgentService`)

#### AI Models
- **forage-model-anthropic**: Anthropic Claude integration (Claude models) - *placeholder implementation*
- **forage-model-azure-openai**: Azure OpenAI integration (GPT models via Azure)
- **forage-model-dashscope**: Alibaba Dashscope integration (Qwen models) - *placeholder implementation*
- **forage-model-google-gemini**: Google Gemini integration
- **forage-model-hugging-face**: HuggingFace Inference API integration (various open-source models)
- **forage-model-local-ai**: LocalAI self-hosted OpenAI-compatible models integration
- **forage-model-mistral-ai**: MistralAI integration (Mistral models)
- **forage-model-ollama**: Ollama local model integration
- **forage-model-openai**: OpenAI integration (GPT models)
- **forage-model-watsonx-ai**: IBM Watsonx.ai integration (Llama, Granite, and other foundation models)

#### Chat Memory
- **forage-memory-message-window**: Message window memory with persistence
- **forage-memory-infinispan**: Infinispan-based distributed memory storage
- **forage-memory-redis**: Redis-based memory storage for scalability

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
@ForageBean(value = "new-model", component = "camel-langchain4j-agent", description = "New AI model provider")
public class NewModelProvider implements ModelProvider {
    
    @Override
    public ChatModel create(String id) {
        NewModelConfig config = new NewModelConfig(id);
        
        return NewModelClient.builder()
            .apiKey(config.apiKey())
            .modelName(config.modelName())
            .build();
    }
}
```

2. **Create configuration classes** (following the two-class pattern above)

3. **Register with ServiceLoader**:
   - File: `META-INF/services/org.apache.camel.forage.core.ai.ModelProvider`
   - Content: `org.apache.camel.forage.models.chat.newmodel.NewModelProvider`

### Creating a New Vector Database Provider

1. **Create provider class**:
```java
@ForageBean(value = "new-vectordb", component = "camel-langchain4j-embeddings", description = "New vector database provider")
public class NewVectorDbProvider implements EmbeddingStoreProvider {
    
    @Override
    public EmbeddingStore<TextSegment> create(String id) {
        NewVectorDbConfig config = new NewVectorDbConfig(id);
        
        return NewVectorDb.builder()
            .host(config.host())
            .port(config.port())
            .build();
    }
}
```

2. **Create configuration classes** (following the two-class pattern above)

3. **Register with ServiceLoader**:
   - File: `META-INF/services/org.apache.camel.forage.core.vectordb.EmbeddingStoreProvider`
   - Content: `org.apache.camel.forage.vectordb.newdb.NewVectorDbProvider`

## Model Configuration

Each AI model provider has its own configuration requirements and options. For detailed configuration instructions, please refer to the individual model documentation:

### AI Model Providers

- **[Azure OpenAI](library/ai/models/chat/forage-model-azure-openai/README.md)** - Azure OpenAI GPT models
- **[OpenAI](library/ai/models/chat/forage-model-open-ai/README.md)** - OpenAI GPT models
- **[Google Gemini](library/ai/models/chat/forage-model-google-gemini/README.md)** - Google Gemini models
- **[LocalAI](library/ai/models/chat/forage-model-local-ai/README.md)** - Self-hosted OpenAI-compatible models
- **[Ollama](library/ai/models/chat/forage-model-ollama/README.md)** - Local model hosting via Ollama
- **[HuggingFace](library/ai/models/chat/forage-model-hugging-face/README.md)** - HuggingFace Inference API models
- **[Anthropic](library/ai/models/chat/forage-model-anthropic/README.md)** - Anthropic Claude models *(placeholder)*
- **[Dashscope](library/ai/models/chat/forage-model-dashscope/README.md)** - Alibaba Dashscope/Qwen models *(placeholder)*

Each model directory contains:
- Detailed configuration instructions
- Environment variable setup
- System property configuration
- Configuration file examples
- Supported models list
- Usage examples
- Troubleshooting guides

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

### ForageBean Annotation Requirements
**MANDATORY**: All provider classes must include:
1. Import: `import org.apache.camel.forage.core.annotations.ForageBean;`
2. Class annotation: `@ForageBean(value = "unique-name", component = "supported-component", description = "What it does")`
3. Standard components: "camel-langchain4j-agent", "camel-langchain4j-embeddings"
4. Descriptive value that matches the technology/provider name

### Configuration Classes Must Include
**For ConfigEntries classes:**
1. `ConfigModule` static fields for each configuration parameter
2. Static `CONFIG_MODULES` map using `ConcurrentHashMap` with `ConfigEntry` mappings
3. Static `init()` method called from static block for initialization
4. Static `entries()`, `find(prefix, name)`, `register(prefix)`, and `loadOverrides(prefix)` methods
5. Extend `ConfigEntries` abstract class
6. Proper prefix handling in `register()` method with null check and loop-based registration

**For main Config classes:**
1. `prefix` field to support named configurations
2. Default constructor calling `this(null)` and prefixed constructor
3. Three-step constructor initialization:
   - First: Call `ConfigEntries.register(prefix)`
   - Second: Call `ConfigStore.getInstance().load(ConfigClass.class, this, this::register)`
   - Third: Call `ConfigEntries.loadOverrides(prefix)`
4. `register(String name, String value)` method using `Optional<ConfigModule>` and `ifPresent()`
5. All getter methods using `configModule.asNamed(prefix)` when accessing ConfigStore

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
