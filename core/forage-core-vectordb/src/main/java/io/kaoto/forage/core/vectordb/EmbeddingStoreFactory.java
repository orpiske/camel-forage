package io.kaoto.forage.core.vectordb;

import org.apache.camel.CamelContext;
import dev.langchain4j.store.embedding.EmbeddingStore;

public interface EmbeddingStoreFactory {
    void setCamelContext(CamelContext camelContext);

    EmbeddingStore createEmbeddingStore();
}
