package io.kaoto.forage.vectordb.weaviate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import io.kaoto.forage.core.util.config.AbstractConfig;
import io.kaoto.forage.core.util.config.MissingConfigException;

import static io.kaoto.forage.vectordb.weaviate.WeaviateConfigEntries.API_KEY;
import static io.kaoto.forage.vectordb.weaviate.WeaviateConfigEntries.AVOID_DUPS;
import static io.kaoto.forage.vectordb.weaviate.WeaviateConfigEntries.CONSISTENCY_LEVEL;
import static io.kaoto.forage.vectordb.weaviate.WeaviateConfigEntries.GRPC_PORT;
import static io.kaoto.forage.vectordb.weaviate.WeaviateConfigEntries.HOST;
import static io.kaoto.forage.vectordb.weaviate.WeaviateConfigEntries.METADATA_FIELD_NAME;
import static io.kaoto.forage.vectordb.weaviate.WeaviateConfigEntries.METADATA_KEYS;
import static io.kaoto.forage.vectordb.weaviate.WeaviateConfigEntries.OBJECT_CLASS;
import static io.kaoto.forage.vectordb.weaviate.WeaviateConfigEntries.PORT;
import static io.kaoto.forage.vectordb.weaviate.WeaviateConfigEntries.SCHEME;
import static io.kaoto.forage.vectordb.weaviate.WeaviateConfigEntries.SECURED_GRPC;
import static io.kaoto.forage.vectordb.weaviate.WeaviateConfigEntries.TEXT_FIELD_NAME;
import static io.kaoto.forage.vectordb.weaviate.WeaviateConfigEntries.USE_GRPC_FOR_INSERTS;

public class WeaviateConfig extends AbstractConfig {

    public WeaviateConfig() {
        this(null);
    }

    public WeaviateConfig(String prefix) {
        super(prefix, WeaviateConfigEntries.class);
    }

    @Override
    public String name() {
        return "forage-vectordb-weaviate";
    }

    public String apiKey() {
        return get(API_KEY).orElse(null);
    }

    public String scheme() {
        return getRequired(SCHEME, "Missing Weaviate scheme");
    }

    public String host() {
        return getRequired(HOST, "Missing Weaviate host");
    }

    public Integer port() {
        return get(PORT).map(Integer::parseInt).orElseThrow(() -> new MissingConfigException("Missing Weaviate port"));
    }

    public Boolean useGrpcForInserts() {
        return get(USE_GRPC_FOR_INSERTS)
                .map(Boolean::parseBoolean)
                .orElseThrow(() -> new MissingConfigException("Missing Weaviate use grpc for inserts"));
    }

    public Boolean securedGrpc() {
        return get(SECURED_GRPC)
                .map(Boolean::parseBoolean)
                .orElseThrow(() -> new MissingConfigException("Missing Weaviate secured grpc"));
    }

    public Integer grpcPort() {
        return get(GRPC_PORT)
                .map(Integer::parseInt)
                .orElseThrow(() -> new MissingConfigException("Missing Weaviate grpc port"));
    }

    public String objectClass() {
        return getRequired(OBJECT_CLASS, "Missing Weaviate object class");
    }

    public Boolean avoidDups() {
        return get(AVOID_DUPS).map(Boolean::parseBoolean).orElse(true);
    }

    public String consistencyLevel() {
        return get(CONSISTENCY_LEVEL).orElse("ALL");
    }

    public Collection<String> metadataKeys() {
        return get(METADATA_KEYS).map(Arrays::asList).orElse(Collections.emptyList());
    }

    public String textFieldName() {
        return get(TEXT_FIELD_NAME).orElse("text");
    }

    public String metadataFieldName() {
        return get(METADATA_FIELD_NAME).orElse("_metadata");
    }

    public String toString() {
        return "apiKey: %s, scheme %s, host %s, port %s, useGrpcForInserts %s, securedGrpc %s, objectClass %s, avoidDups %s, consistencyLevel %s"
                .formatted(
                        apiKey(),
                        scheme(),
                        host(),
                        port().toString(),
                        useGrpcForInserts().toString(),
                        securedGrpc().toString(),
                        objectClass(),
                        avoidDups().toString(),
                        consistencyLevel().toString());
    }
}
