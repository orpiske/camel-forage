package org.apache.camel.forage.vectordb.pgvector;

import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.apache.camel.forage.core.vectordb.EmbeddingStoreProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provider for creating PgVector embedding stores
 */
public class PgVectorProvider implements EmbeddingStoreProvider {
    private static final Logger LOG = LoggerFactory.getLogger(PgVectorProvider.class);

    @Override
    public PgVectorEmbeddingStore create(String id) {
        LOG.trace("Creating PgVector Embedding Store");

        PgVectorConfig config = new PgVectorConfig(id);

        return PgVectorEmbeddingStore.builder()
                .host(config.host())
                .port(config.port())
                .user(config.user())
                .password(config.password())
                .database(config.database())
                .table(config.table())
                .dimension(config.dimension())
                .useIndex(config.useIndex())
                .indexListSize(config.indexListSize())
                .createTable(config.createTable())
                .dropTableFirst(config.dropTableFirst())
                .metadataStorageConfig(config.metadataStorageConfig())
                .build();
    }
}
