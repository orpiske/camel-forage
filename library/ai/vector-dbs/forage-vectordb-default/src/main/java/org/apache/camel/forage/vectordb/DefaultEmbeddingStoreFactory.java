package org.apache.camel.forage.vectordb;

import dev.langchain4j.store.embedding.EmbeddingStore;
import java.util.ServiceLoader;
import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.forage.core.annotations.ForageFactory;
import org.apache.camel.forage.core.vectordb.EmbeddingStoreFactory;
import org.apache.camel.forage.core.vectordb.EmbeddingStoreProvider;

@ForageFactory(
        value = "default-embedding-store",
        component = "camel-langchain4j-embeddings",
        description = "Default embedding store factory with ServiceLoader discovery",
        factoryType = "EmbeddingStore")
public class DefaultEmbeddingStoreFactory implements EmbeddingStoreFactory, CamelContextAware {
    private CamelContext camelContext;

    @Override
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public CamelContext getCamelContext() {
        return camelContext;
    }

    @Override
    public EmbeddingStore createEmbeddingStore() {
        ServiceLoader<EmbeddingStoreProvider> providers = ServiceLoader.load(EmbeddingStoreProvider.class);
        EmbeddingStoreProvider provider =
                providers.findFirst().orElseThrow(() -> new IllegalStateException("No EmbeddingStoreProvider found"));
        return provider.create();
    }
}
