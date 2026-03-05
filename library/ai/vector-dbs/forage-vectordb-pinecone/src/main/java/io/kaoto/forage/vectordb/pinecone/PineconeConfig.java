package io.kaoto.forage.vectordb.pinecone;

import org.openapitools.db_control.client.model.DeletionProtection;
import io.kaoto.forage.core.util.config.AbstractConfig;
import io.kaoto.forage.core.util.config.MissingConfigException;

import static io.kaoto.forage.vectordb.pinecone.PineconeConfigEntries.API_KEY;
import static io.kaoto.forage.vectordb.pinecone.PineconeConfigEntries.CLOUD;
import static io.kaoto.forage.vectordb.pinecone.PineconeConfigEntries.DELETION_PROTECTION;
import static io.kaoto.forage.vectordb.pinecone.PineconeConfigEntries.DIMENSION;
import static io.kaoto.forage.vectordb.pinecone.PineconeConfigEntries.INDEX;
import static io.kaoto.forage.vectordb.pinecone.PineconeConfigEntries.METADATA_TEXT_KEY;
import static io.kaoto.forage.vectordb.pinecone.PineconeConfigEntries.NAME_SPACE;
import static io.kaoto.forage.vectordb.pinecone.PineconeConfigEntries.REGION;

public class PineconeConfig extends AbstractConfig {

    public PineconeConfig() {
        this(null);
    }

    public PineconeConfig(String prefix) {
        super(prefix, PineconeConfigEntries.class);
    }

    @Override
    public String name() {
        return "forage-vectordb-pinecone";
    }

    public String apiKey() {
        return getRequired(API_KEY, "Missing Pinecone API key");
    }

    public String index() {
        return getRequired(INDEX, "Missing Pinecone index");
    }

    public String nameSpace() {
        return get(NAME_SPACE).orElse("default");
    }

    public String metadataTextKey() {
        return get(METADATA_TEXT_KEY).orElse("text_segment");
    }

    public Integer dimension() {
        return get(DIMENSION)
                .map(Integer::parseInt)
                .orElseThrow(() -> new MissingConfigException("Missing Pinecone dimension"));
    }

    public String cloud() {
        return getRequired(CLOUD, "Missing Pinecone cloud");
    }

    public String region() {
        return getRequired(REGION, "Missing Pinecone region");
    }

    public DeletionProtection deletionProtection() {
        return get(DELETION_PROTECTION).map(DeletionProtection::valueOf).orElse(DeletionProtection.ENABLED);
    }
}
