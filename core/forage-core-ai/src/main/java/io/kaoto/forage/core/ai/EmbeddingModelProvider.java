package io.kaoto.forage.core.ai;

import io.kaoto.forage.core.common.BeanProvider;
import dev.langchain4j.model.embedding.EmbeddingModel;

/**
 * Provider interface for creating AI embedding models
 */
public interface EmbeddingModelProvider extends BeanProvider<EmbeddingModel> {}
