package io.kaoto.forage.vectordb.qdrant;

import io.kaoto.forage.core.util.config.AbstractConfig;
import io.kaoto.forage.core.util.config.MissingConfigException;

import static io.kaoto.forage.vectordb.qdrant.QdrantConfigEntries.API_KEY;
import static io.kaoto.forage.vectordb.qdrant.QdrantConfigEntries.COLLECTION_NAME;
import static io.kaoto.forage.vectordb.qdrant.QdrantConfigEntries.HOST;
import static io.kaoto.forage.vectordb.qdrant.QdrantConfigEntries.PAYLOAD_TEXT_KEY;
import static io.kaoto.forage.vectordb.qdrant.QdrantConfigEntries.PORT;
import static io.kaoto.forage.vectordb.qdrant.QdrantConfigEntries.USE_TLS;

public class QdrantConfig extends AbstractConfig {

    public QdrantConfig() {
        this(null);
    }

    public QdrantConfig(String prefix) {
        super(prefix, QdrantConfigEntries.class);
    }

    @Override
    public String name() {
        return "forage-vectordb-qdrant";
    }

    public String collectionName() {
        return getRequired(COLLECTION_NAME, "Missing Qdrant collection name");
    }

    public String host() {
        return getRequired(HOST, "Missing Qdrant host");
    }

    public Integer port() {
        return get(PORT).map(Integer::parseInt).orElseThrow(() -> new MissingConfigException("Missing Qdrant port"));
    }

    public Boolean useTls() {
        return get(USE_TLS).map(Boolean::parseBoolean).orElse(false);
    }

    public String payloadTextKey() {
        return get(PAYLOAD_TEXT_KEY).orElse("text_segment");
    }

    public String apiKey() {
        return get(API_KEY).orElse(null);
    }
}
