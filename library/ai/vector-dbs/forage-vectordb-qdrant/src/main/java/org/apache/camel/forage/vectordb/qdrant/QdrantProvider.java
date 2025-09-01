package org.apache.camel.forage.vectordb.qdrant;

import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import org.apache.camel.forage.core.vectordb.EmbeddingStoreProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provider for creating Qdrant embedding stores
 */
public class QdrantProvider implements EmbeddingStoreProvider {
    private static final Logger LOG = LoggerFactory.getLogger(QdrantProvider.class);

    @Override
    public QdrantEmbeddingStore create(String id) {
        LOG.trace("Creating qdrant embedding store");

        final QdrantConfig config = new QdrantConfig(id);

        return QdrantEmbeddingStore.builder()
                .collectionName(config.collectionName())
                .apiKey(config.apiKey())
                .host(config.host())
                .port(config.port())
                .useTls(config.useTls())
                .payloadTextKey(config.payloadTextKey())
                .build();
    }
}
