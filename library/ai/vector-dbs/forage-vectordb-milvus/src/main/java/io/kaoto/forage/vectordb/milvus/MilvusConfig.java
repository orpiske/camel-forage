package io.kaoto.forage.vectordb.milvus;

import io.kaoto.forage.core.util.config.AbstractConfig;
import io.kaoto.forage.core.util.config.MissingConfigException;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;

import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.AUTO_FLUSH_ON_INSERT;
import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.COLLECTION_NAME;
import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.CONSISTENCY_LEVEL;
import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.DATABASE_NAME;
import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.DIMENSION;
import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.HOST;
import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.ID_FIELD_NAME;
import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.INDEX_TYPE;
import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.METADATA_FIELD_NAME;
import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.METRIC_TYPE;
import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.PASSWORD;
import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.PORT;
import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.RETRIEVE_EMBEDDINGS_ON_SEARCH;
import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.TEXT_FIELD_NAME;
import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.TOKEN;
import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.URI;
import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.USERNAME;
import static io.kaoto.forage.vectordb.milvus.MilvusConfigEntries.VECTOR_FIELD_NAME;

/**
 * Configuration class for Milvus vector database connections within the Camel Forage framework.
 *
 * <p>This configuration class manages all settings required to connect to and operate with Milvus,
 * an open-source vector database built for scalable similarity search and AI applications.</p>
 *
 * <p>Configuration properties are loaded from multiple sources in order of precedence:</p>
 * <ol>
 *   <li>Environment variables (highest precedence)</li>
 *   <li>System properties</li>
 *   <li>Configuration files (forage-vectordb-milvus.properties)</li>
 *   <li>Default values (lowest precedence)</li>
 * </ol>
 *
 * <p>The configuration supports ID-scoped properties using the pattern {@code milvus.<id>.<property>}
 * which allows multiple Milvus configurations within the same application.</p>
 *
 * <p>Example configuration properties:</p>
 * <pre>
 * # Connection settings
 * milvus.host=localhost
 * milvus.port=19530
 * milvus.uri=http://localhost:19530
 *
 * # Authentication (choose one method)
 * milvus.token=your-api-token
 * # OR
 * milvus.username=user
 * milvus.password=pass
 *
 * # Collection settings
 * milvus.collection.name=my_vectors
 * milvus.dimension=1536
 * milvus.index.type=IVF_FLAT
 * milvus.metric.type=COSINE
 *
 * # Performance settings
 * milvus.consistency.level=EVENTUALLY
 * milvus.auto.flush.on.insert=false
 * milvus.retrieve.embeddings.on.search=false
 * </pre>
 *
 * @see MilvusProvider
 * @see MilvusConfigEntries
 *
 * @since 1.0
 */
public class MilvusConfig extends AbstractConfig {

    public MilvusConfig() {
        this(null);
    }

    public MilvusConfig(String prefix) {
        super(prefix, MilvusConfigEntries.class);

        // Validate connection configuration
        validateConnectionConfig();
    }

    @Override
    public String name() {
        return "forage-vectordb-milvus";
    }

    public String host() {
        return get(HOST).orElse(null);
    }

    public Integer port() {
        return get(PORT).map(Integer::parseInt).orElse(null);
    }

    public String collectionName() {
        return get(COLLECTION_NAME).orElse("default");
    }

    public Integer dimension() {
        return get(DIMENSION)
                .map(Integer::parseInt)
                .orElseThrow(() -> new MissingConfigException("Missing Milvus dimension"));
    }

    public IndexType indexType() {
        String indexType = get(INDEX_TYPE).orElse(null);

        if (indexType != null) {
            return IndexType.valueOf(indexType);
        } else {
            return IndexType.FLAT;
        }
    }

    public MetricType metricType() {
        String metricType = get(METRIC_TYPE).orElse(null);
        if (metricType != null) {
            return MetricType.valueOf(metricType);
        } else {
            return MetricType.COSINE;
        }
    }

    public String uri() {
        return get(URI).orElse(null);
    }

    public String token() {
        return get(TOKEN).orElse(null);
    }

    public String username() {
        return get(USERNAME).orElse("");
    }

    public String password() {
        return get(PASSWORD).orElse("");
    }

    public ConsistencyLevelEnum consistencyLevel() {
        String consistencyLevel = get(CONSISTENCY_LEVEL).orElse(null);

        if (consistencyLevel != null) {
            return ConsistencyLevelEnum.valueOf(consistencyLevel);
        } else {
            return ConsistencyLevelEnum.EVENTUALLY;
        }
    }

    public Boolean retrieveEmbeddingsOnSearch() {
        return get(RETRIEVE_EMBEDDINGS_ON_SEARCH).map(Boolean::parseBoolean).orElse(false);
    }

    public String databaseName() {
        return getRequired(DATABASE_NAME, "Missing Milvus database name");
    }

    public String idFieldName() {
        return get(ID_FIELD_NAME).orElse("id");
    }

    public String textFieldName() {
        return get(TEXT_FIELD_NAME).orElse("text");
    }

    public String metadataFieldName() {
        return get(METADATA_FIELD_NAME).orElse("metadata");
    }

    public String vectorFieldName() {
        return get(VECTOR_FIELD_NAME).orElse("vector");
    }

    public Boolean autoFlushOnInsert() {
        return get(AUTO_FLUSH_ON_INSERT).map(Boolean::parseBoolean).orElse(false);
    }

    /*
     * validateConnectionConfig checks to see if either the uri or the host/port
     * combination is set.  It is not necessary for both uri and host/port to be set
     * as only one of these is needed for a connection, but one of the two options
     * does need to be set.
     */
    private void validateConnectionConfig() {
        String uri = uri();
        String host = host();
        Integer port = port();

        if (uri == null && (host == null || port == null)) {
            throw new MissingConfigException(
                    "Milvus connection configuration incomplete: either 'uri' or both 'host' and 'port' must be provided");
        }
    }
}
