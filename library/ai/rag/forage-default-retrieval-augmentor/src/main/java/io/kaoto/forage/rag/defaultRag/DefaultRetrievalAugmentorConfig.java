package io.kaoto.forage.rag.defaultRag;

import io.kaoto.forage.core.util.config.AbstractConfig;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.RetrievalAugmentor;

import static io.kaoto.forage.rag.defaultRag.DefaultRetrievalAugmentorConfigEntries.MAX_RESULTS;
import static io.kaoto.forage.rag.defaultRag.DefaultRetrievalAugmentorConfigEntries.MIN_SCORE;

/**
 * Configuration class for the {@link DefaultRetrievalAugmentorProvider} (default implementation of {@link RetrievalAugmentor})
 *
 * <p>This configuration class manages the settings required to create a default implementation of
 * {@link RetrievalAugmentor}, which is intended to be suitable for the majority of use cases.
 *
 * <p>An {@link dev.langchain4j.store.embedding.EmbeddingStore} and
 * {@link EmbeddingModel} have to be provided for the successful construction of {@link RetrievalAugmentor}
 * as the part of the agent configuration.
 *
 * <p><strong>Configuration Parameters:</strong>
 * <ul>
 *   <li><strong>MAX_RESULTS</strong> - The maximum number of Contents to retrieve.</li>
 *   <li><strong>MIN_SCORE</strong> - The minimum relevance score for the returned Contents. Contents scoring below #minScore are excluded from the results.</li>
 * </ul>
 *
 * @see io.kaoto.forage.core.util.config.Config
 * @see io.kaoto.forage.core.util.config.ConfigStore
 * @see io.kaoto.forage.core.util.config.ConfigModule
 * @since 1.0
 */
public class DefaultRetrievalAugmentorConfig extends AbstractConfig {

    /**
     * Constructs a new DefaultRetrievalAugmentorConfig and registers configuration parameters with the ConfigStore.
     */
    public DefaultRetrievalAugmentorConfig() {
        this(null);
    }

    public DefaultRetrievalAugmentorConfig(String prefix) {
        super(prefix, DefaultRetrievalAugmentorConfigEntries.class);
    }

    /**
     *  Returns the unique identifier for this Ollama configuration module.
     */
    @Override
    public String name() {
        return "forage-rag-default";
    }

    /**
     * Returns the max-result parameter.
     *
     * <p>The maximum number of Contents to retrieve.</p>
     */
    public Integer maxResults() {
        return get(MAX_RESULTS).map(Integer::parseInt).orElse(null);
    }

    /**
     * Returns the min-score parameter.
     *
     * <p>The minimum relevance score for the returned Contents. Contents scoring below #minScore are excluded
     * from the results.</p>
     */
    public Double minScore() {
        return get(MIN_SCORE).map(Double::parseDouble).orElse(null);
    }
}
