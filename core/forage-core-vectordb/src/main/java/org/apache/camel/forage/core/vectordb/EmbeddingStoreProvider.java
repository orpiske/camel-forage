package org.apache.camel.forage.core.vectordb;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.apache.camel.forage.core.common.BeanProvider;

/**
 * Provider interface for creating AI embedding stores
 */
public interface EmbeddingStoreProvider extends BeanProvider<EmbeddingStore<TextSegment>> {}
