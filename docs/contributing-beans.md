# Contributing Beans Guide

This guide explains how to prepare an Apache Camel component for usage with the Camel Forage library and how to create new bean providers and factories.

## Overview

Adjusting a component for use with Camel Forage involves two main steps:

1. **Modify the Apache Camel component** to support a factory-based approach
2. **Create the corresponding Forage bean factory and provider**

## Part 1: Adjusting the Apache Camel Component

### 1.1 Add Factory Parameter

First, add a factory parameter to your Camel component's configuration. This allows users to specify a factory that will create the required beans.

**Example from `camel-langchain4j-assistant`:**

```java
@UriParam(description = "The agent factory to use for creating agents if no Agent is provided")
@Metadata(autowired = true)
private AgentFactory agentFactory;
```

### 1.2 Initialize Factory in Producer

In your producer's `doStart()` method, initialize the factory and provide it with the Camel context:

```java
@Override
protected void doStart() throws Exception {
    super.doStart();

    agentFactory = endpoint.getConfiguration().getAgentFactory();
    if (agentFactory != null) {
        agentFactory.setCamelContext(this.endpoint.getCamelContext());
    }
}
```

### 1.3 Use Factory in Processing Logic

When processing exchanges, use the factory to create beans if available, falling back to direct configuration if not:

```java
public void process(Exchange exchange) throws Exception {
    // ... other processing logic
    
    Agent agent;
    if (agentFactory != null) {
        agent = agentFactory.createAgent();
    } else {
        agent = endpoint.getConfiguration().getAgent();
    }
    
    // ... continue processing with the agent
}
```

## Part 2: Creating Forage Bean Factories and Providers

### 2.1 Creating a New Component Category

If you're creating an entirely new category of components (e.g., vector databases), you need to create both core and common modules:

#### Core Module Structure

Create a core component that contains the foundational interfaces:

**Example: `core/forage-vector-db`**

```
core/forage-vector-db/
├── src/main/java/org/apache/camel/forage/core/vector/db/
│   ├── VectorDBFactory.java          # Factory interface
│   └── VectorDBProvider.java         # Provider interface
└── pom.xml
```

**VectorDBFactory.java:**
```java
package org.apache.camel.forage.core.vector.db;

import org.apache.camel.CamelContext;

public interface VectorDBFactory {
    void setCamelContext(CamelContext camelContext);
    VectorDatabase createVectorDB();
}
```

**VectorDBProvider.java:**
```java
package org.apache.camel.forage.core.vector.db;

public interface VectorDBProvider {
    VectorDatabase newVectorDB();
}
```

#### Common Module Structure

Create a common component with default implementations:

**Example: `library/vector-dbs/forage-default-vector-db-factory`**

```
library/vector-dbs/forage-default-vector-db-factory/
├── src/main/java/org/apache/camel/forage/vector/db/
│   └── DefaultVectorDBFactory.java   # Default factory implementation
└── pom.xml
```

**DefaultVectorDBFactory.java:**
```java
package org.apache.camel.forage.vector.db;

import org.apache.camel.forage.core.vector.db.VectorDBFactory;
import org.apache.camel.forage.core.vector.db.VectorDBProvider;
import java.util.ServiceLoader;

public class DefaultVectorDBFactory implements VectorDBFactory {
    private CamelContext camelContext;
    
    @Override
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }
    
    @Override
    public VectorDatabase createVectorDB() {
        ServiceLoader<VectorDBProvider> providers = ServiceLoader.load(VectorDBProvider.class);
        VectorDBProvider provider = providers.findFirst()
            .orElseThrow(() -> new IllegalStateException("No VectorDBProvider found"));
        return provider.newVectorDB();
    }
}
```

### 2.2 Creating Provider Implementations

Create specific provider implementations for each technology:

**Example: `library/vector-dbs/forage-milvus`**

```
library/vector-dbs/forage-milvus/
├── src/main/java/org/apache/camel/forage/vector/db/milvus/
│   ├── MilvusProvider.java            # Provider implementation
│   └── MilvusConfig.java              # Configuration class
├── src/main/resources/META-INF/services/
│   └── org.apache.camel.forage.core.vector.db.VectorDBProvider
└── pom.xml
```

#### Provider Implementation

**MilvusProvider.java:**
```java
package org.apache.camel.forage.vector.db.milvus;

import org.apache.camel.forage.core.vector.db.VectorDBProvider;

public class MilvusProvider implements VectorDBProvider {
    private final MilvusConfig config;
    
    public MilvusProvider() {
        this.config = new MilvusConfig();
    }
    
    @Override
    public VectorDatabase newVectorDB() {
        return MilvusClient.builder()
            .host(config.host())
            .port(config.port())
            .username(config.username())
            .password(config.password())
            .build();
    }
}
```

#### Configuration Class

**MilvusConfig.java:**
```java
package org.apache.camel.forage.vector.db.milvus;

import org.apache.camel.forage.core.util.config.Config;
import org.apache.camel.forage.core.util.config.ConfigEntry;
import org.apache.camel.forage.core.util.config.ConfigModule;
import org.apache.camel.forage.core.util.config.ConfigStore;

/**
 * Configuration class for Milvus vector database integration.
 * 
 * <p><strong>Configuration Parameters:</strong>
 * <ul>
 *   <li><strong>MILVUS_HOST</strong> - Milvus server host (default: "localhost")</li>
 *   <li><strong>MILVUS_PORT</strong> - Milvus server port (default: 19530)</li>
 *   <li><strong>MILVUS_USERNAME</strong> - Username for authentication (optional)</li>
 *   <li><strong>MILVUS_PASSWORD</strong> - Password for authentication (optional)</li>
 * </ul>
 */
public class MilvusConfig implements Config {
    
    private static final ConfigModule HOST = ConfigModule.of(MilvusConfig.class, "host");
    private static final ConfigModule PORT = ConfigModule.of(MilvusConfig.class, "port");
    private static final ConfigModule USERNAME = ConfigModule.of(MilvusConfig.class, "username");
    private static final ConfigModule PASSWORD = ConfigModule.of(MilvusConfig.class, "password");
    
    private static final String DEFAULT_HOST = "localhost";
    private static final Integer DEFAULT_PORT = 19530;
    
    public MilvusConfig() {
        ConfigStore.getInstance().add(HOST, ConfigEntry.fromEnv("MILVUS_HOST"));
        ConfigStore.getInstance().add(PORT, ConfigEntry.fromEnv("MILVUS_PORT"));
        ConfigStore.getInstance().add(USERNAME, ConfigEntry.fromEnv("MILVUS_USERNAME"));
        ConfigStore.getInstance().add(PASSWORD, ConfigEntry.fromEnv("MILVUS_PASSWORD"));
        ConfigStore.getInstance().add(MilvusConfig.class, this);
    }
    
    @Override
    public String name() {
        return "forage-vector-db-milvus";
    }
    
    public String host() {
        return ConfigStore.getInstance().get(HOST).orElse(DEFAULT_HOST);
    }
    
    public Integer port() {
        return ConfigStore.getInstance().get(PORT)
            .map(Integer::parseInt)
            .orElse(DEFAULT_PORT);
    }
    
    public String username() {
        return ConfigStore.getInstance().get(USERNAME).orElse(null);
    }
    
    public String password() {
        return ConfigStore.getInstance().get(PASSWORD).orElse(null);
    }
}
```

#### ServiceLoader Registration

Create the ServiceLoader registration file:

**src/main/resources/META-INF/services/org.apache.camel.forage.core.vector.db.VectorDBProvider:**
```
org.apache.camel.forage.vector.db.milvus.MilvusProvider
```

### 2.3 Maven Configuration

**Provider module pom.xml:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.apache.camel.forage</groupId>
        <artifactId>vector-dbs</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <artifactId>forage-vector-db-milvus</artifactId>
    <name>Camel Forage :: Library :: Vector DBs :: Milvus</name>

    <dependencies>
        <dependency>
            <groupId>org.apache.camel.forage</groupId>
            <artifactId>forage-core-vector-db</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.camel.forage</groupId>
            <artifactId>forage-core-common</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>io.milvus</groupId>
            <artifactId>milvus-sdk-java</artifactId>
            <version>2.3.4</version>
        </dependency>
    </dependencies>
</project>
```

## Part 3: Using the New Component

### 3.1 In Camel Routes

Reference the factory class in your Camel route:

```java
from("direct:vector-search")
    .to("vector-db:search?connectionFactory=#class:org.apache.camel.forage.vector.db.DefaultVectorDBFactory");
```

### 3.2 Classpath Dependencies

Make sure to include the specific provider on your classpath:

```xml
<dependency>
    <groupId>org.apache.camel.forage</groupId>
    <artifactId>forage-vector-db-milvus</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 3.3 Configuration

Configure using environment variables:

```bash
export MILVUS_HOST="milvus.example.com"
export MILVUS_PORT="19530"
export MILVUS_USERNAME="admin"
export MILVUS_PASSWORD="password123"
```

Or using system properties:

```bash
-Dmilvus.host=milvus.example.com
-Dmilvus.port=19530
-Dmilvus.username=admin
-Dmilvus.password=password123
```

Or using configuration files (`forage-vector-db-milvus.properties`):

```properties
host=milvus.example.com
port=19530
username=admin
password=password123
```

## Best Practices

### 1. Configuration Guidelines
- Always follow the Forage configuration pattern with `Config` classes
- Use meaningful environment variable names with a consistent prefix
- Provide sensible defaults where appropriate
- Document all configuration parameters comprehensively

### 2. ServiceLoader Registration
- Don't forget to create the ServiceLoader resource file
- Use the full interface name as the filename
- List all implementations in the file

### 3. Error Handling
- Provide clear error messages when configuration is missing
- Use `MissingConfigException` for required parameters
- Validate configuration values where appropriate

### 4. Testing
- Create unit tests for your provider and configuration classes
- Test the ServiceLoader discovery mechanism
- Verify that configuration sources work correctly

### 5. Documentation
- Add comprehensive Javadoc to all public classes and methods
- Include usage examples in class-level documentation
- Document configuration precedence clearly

## Example Directory Structure

Here's how your final module structure should look:

```
camel-forage/
├── core/
│   └── forage-core-vector-db/           # Core interfaces
├── library/
│   └── vector-dbs/
│       ├── forage-default-vector-db-factory/  # Common factory
│       ├── forage-vector-db-milvus/            # Milvus implementation
│       ├── forage-vector-db-pinecone/          # Pinecone implementation
│       └── forage-vector-db-weaviate/          # Weaviate implementation
└── docs/
    └── contributing-beans.md           # This guide
```

This structure allows for easy extension with new vector database providers while maintaining a consistent API and configuration approach across all implementations.