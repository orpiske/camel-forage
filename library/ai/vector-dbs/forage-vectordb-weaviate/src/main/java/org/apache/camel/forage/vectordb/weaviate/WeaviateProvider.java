package org.apache.camel.forage.vectordb.weaviate;

import dev.langchain4j.store.embedding.weaviate.WeaviateEmbeddingStore;
import org.apache.camel.forage.core.vectordb.EmbeddingStoreProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provider for creating Google Gemini chat models
 */
public class WeaviateProvider implements EmbeddingStoreProvider {
    private static final Logger LOG = LoggerFactory.getLogger(WeaviateProvider.class);

    @Override
    public WeaviateEmbeddingStore create(String id) {
        LOG.trace("Creating weaviate embedding store");

        WeaviateConfig config = new WeaviateConfig(id);

        return WeaviateEmbeddingStore.builder()
                .apiKey(config.apiKey())
                .scheme(config.scheme())
                .host(config.host())
                .port(config.port())
                .useGrpcForInserts(config.useGrpcForInserts())
                .securedGrpc(config.securedGrpc())
                .grpcPort(config.grpcPort())
                .objectClass(config.objectClass())
                .avoidDups(config.avoidDups())
                .consistencyLevel(config.consistencyLevel())
                .metadataKeys(config.metadataKeys())
                .textFieldName(config.textFieldName())
                .metadataFieldName(config.metadataFieldName())
                .build();
    }
}
