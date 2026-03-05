package io.kaoto.forage.vectordb.pgvector;

import io.kaoto.forage.core.util.config.AbstractConfig;
import io.kaoto.forage.core.util.config.MissingConfigException;
import dev.langchain4j.store.embedding.pgvector.DefaultMetadataStorageConfig;
import dev.langchain4j.store.embedding.pgvector.MetadataStorageConfig;

import static io.kaoto.forage.vectordb.pgvector.PgVectorConfigEntries.CREATE_TABLE;
import static io.kaoto.forage.vectordb.pgvector.PgVectorConfigEntries.DATABASE;
import static io.kaoto.forage.vectordb.pgvector.PgVectorConfigEntries.DIMENSION;
import static io.kaoto.forage.vectordb.pgvector.PgVectorConfigEntries.DROP_TABLE_FIRST;
import static io.kaoto.forage.vectordb.pgvector.PgVectorConfigEntries.HOST;
import static io.kaoto.forage.vectordb.pgvector.PgVectorConfigEntries.INDEX_LIST_SIZE;
import static io.kaoto.forage.vectordb.pgvector.PgVectorConfigEntries.PASSWORD;
import static io.kaoto.forage.vectordb.pgvector.PgVectorConfigEntries.PORT;
import static io.kaoto.forage.vectordb.pgvector.PgVectorConfigEntries.TABLE;
import static io.kaoto.forage.vectordb.pgvector.PgVectorConfigEntries.USER;
import static io.kaoto.forage.vectordb.pgvector.PgVectorConfigEntries.USE_INDEX;

public class PgVectorConfig extends AbstractConfig {

    public PgVectorConfig() {
        this(null);
    }

    public PgVectorConfig(String prefix) {
        super(prefix, PgVectorConfigEntries.class);
    }

    @Override
    public String name() {
        return "forage-vectordb-pgvector";
    }

    public String host() {
        return getRequired(HOST, "Missing PGVector host");
    }

    public Integer port() {
        return get(PORT).map(Integer::parseInt).orElseThrow(() -> new MissingConfigException("Missing PGVector port"));
    }

    public String user() {
        return getRequired(USER, "Missing PGVector user");
    }

    public String password() {
        return getRequired(PASSWORD, "Missing PGVector password");
    }

    public String database() {
        return getRequired(DATABASE, "Missing PGVector database");
    }

    public String table() {
        return getRequired(TABLE, "Missing PGVector table");
    }

    public Integer dimension() {
        return get(DIMENSION)
                .map(Integer::parseInt)
                .orElseThrow(() -> new MissingConfigException("Missing PGVector dimension"));
    }

    public Boolean useIndex() {
        return get(USE_INDEX).map(Boolean::parseBoolean).orElse(false);
    }

    public Integer indexListSize() {
        return get(INDEX_LIST_SIZE).map(Integer::parseInt).orElse(100);
    }

    public Boolean createTable() {
        return get(CREATE_TABLE).map(Boolean::parseBoolean).orElse(true);
    }

    public Boolean dropTableFirst() {
        return get(DROP_TABLE_FIRST).map(Boolean::parseBoolean).orElse(false);
    }

    public MetadataStorageConfig metadataStorageConfig() {
        return DefaultMetadataStorageConfig.defaultConfig();
    }
}
