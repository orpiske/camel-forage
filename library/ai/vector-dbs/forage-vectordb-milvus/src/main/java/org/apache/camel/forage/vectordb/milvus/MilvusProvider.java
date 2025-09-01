package org.apache.camel.forage.vectordb.milvus;

import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import org.apache.camel.forage.core.vectordb.EmbeddingStoreProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provider for creating Milvus Embedding Stores
 */
public class MilvusProvider implements EmbeddingStoreProvider {
    private static final Logger LOG = LoggerFactory.getLogger(MilvusProvider.class);

    @Override
    public MilvusEmbeddingStore create(String id) {
        MilvusConfig config = new MilvusConfig(id);

        LOG.trace("Creating Milvus Embedding Store");

        return MilvusEmbeddingStore.builder()
                .host(config.host())
                .port(config.port())
                .collectionName(config.collectionName())
                .dimension(config.dimension())
                .indexType(config.indexType())
                .metricType(config.metricType())
                .uri(config.uri())
                .token(config.token())
                .username(config.username())
                .password(config.password())
                .consistencyLevel(config.consistencyLevel())
                .retrieveEmbeddingsOnSearch(config.retrieveEmbeddingsOnSearch())
                .autoFlushOnInsert(config.autoFlushOnInsert())
                .build();
    }
}
