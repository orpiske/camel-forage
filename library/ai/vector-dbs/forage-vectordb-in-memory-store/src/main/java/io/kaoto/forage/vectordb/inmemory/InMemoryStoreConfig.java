package io.kaoto.forage.vectordb.inmemory;

import io.kaoto.forage.core.util.config.AbstractConfig;
import dev.langchain4j.store.embedding.EmbeddingStore;

import static io.kaoto.forage.vectordb.inmemory.InMemoryStoreConfigEntries.FILE_SOURCE;
import static io.kaoto.forage.vectordb.inmemory.InMemoryStoreConfigEntries.MAX_SIZE;
import static io.kaoto.forage.vectordb.inmemory.InMemoryStoreConfigEntries.OVERLAP_SIZE;

/**
 * Configuration class for the {@link dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore}.
 *
 * <p>An {@link EmbeddingStore} that stores embeddings in memory.
 * Uses a brute force approach by iterating over all embeddings to find the best matches.
 *
 * <p>This store can be persisted using the {@code InMemoryEmbeddingStore#serializeToJson()} and
 * {@code InMemoryEmbeddingStore#serializeToFile(Path)} methods.
 *
 * <p>It can also be recreated from JSON or a file using the {@code InMemoryEmbeddingStore#fromJson(String)}
 * and {@code InMemoryEmbeddingStore#fromFile(Path)} methods.
 *
 * <p>An {@link dev.langchain4j.model.embedding.EmbeddingModel EmbeddingModel} has to be provided for the
 * successful construction of a retrieval augmentor as part of the agent configuration.
 *
 * <p><strong>Configuration Parameters:</strong>
 * <ul>
 *   <li><strong>FILE_SOURCE</strong> - Path to a file to be loaded into store via {@link dev.langchain4j.data.document.Document#from}.</li>
 *   <li><strong>MAX_SIZE</strong> - The maximum size of the segment, defined in characters.</li>
 *   <li><strong>OVERLAP_SIZE</strong> - The maximum size of the overlap, defined in characters. Only full sentences are considered for the overlap..</li>
 * </ul>
 *
 * @see AbstractConfig
 * @see io.kaoto.forage.core.util.config.ConfigStore
 * @see io.kaoto.forage.core.util.config.ConfigModule
 * @since 1.0
 */
public class InMemoryStoreConfig extends AbstractConfig {

    /**
     * Creates a new InMemory configuration using default (non-prefixed) properties.
     */
    public InMemoryStoreConfig() {
        this(null);
    }

    public InMemoryStoreConfig(String prefix) {
        super(prefix, InMemoryStoreConfigEntries.class);
    }

    @Override
    public String name() {
        return "forage-vectordb-in-memory";
    }

    /**
     * Returns the file-source parameter.
     *
     * <p>Path to a file to be loaded into store via {@link dev.langchain4j.data.document.Document#from}.</p>
     */
    public String fileSource() {
        return get(FILE_SOURCE).orElse(null);
    }

    /**
     * Returns the max-size parameter.
     *
     * <p>The maximum size of the segment, defined in characters.</p>
     */
    public Integer maxSize() {
        return get(MAX_SIZE).map(Integer::parseInt).orElse(null);
    }

    /**
     * Returns the overlap-size parameter.
     *
     * <p>The maximum size of the overlap, defined in characters. Only full sentences are considered for the overlap.</p>
     */
    public Integer overlapSize() {
        return get(OVERLAP_SIZE).map(Integer::parseInt).orElse(null);
    }
}
