package io.kaoto.forage.vectordb.mariadb;

import io.kaoto.forage.core.util.config.AbstractConfig;
import dev.langchain4j.store.embedding.mariadb.MariaDBDistanceType;

import static io.kaoto.forage.vectordb.mariadb.MariaDbConfigEntries.CONTENT_FIELD_NAME;
import static io.kaoto.forage.vectordb.mariadb.MariaDbConfigEntries.CREATE_TABLE;
import static io.kaoto.forage.vectordb.mariadb.MariaDbConfigEntries.DIMENSION;
import static io.kaoto.forage.vectordb.mariadb.MariaDbConfigEntries.DISTANCE_TYPE;
import static io.kaoto.forage.vectordb.mariadb.MariaDbConfigEntries.DROP_TABLE_FIRST;
import static io.kaoto.forage.vectordb.mariadb.MariaDbConfigEntries.EMBEDDING_FIELD_NAME;
import static io.kaoto.forage.vectordb.mariadb.MariaDbConfigEntries.ID_FIELD_NAME;
import static io.kaoto.forage.vectordb.mariadb.MariaDbConfigEntries.PASSWORD;
import static io.kaoto.forage.vectordb.mariadb.MariaDbConfigEntries.TABLE;
import static io.kaoto.forage.vectordb.mariadb.MariaDbConfigEntries.URL;
import static io.kaoto.forage.vectordb.mariadb.MariaDbConfigEntries.USER;

/**
 * Configuration class for MariaDB vector database connections.
 *
 * <p>This class manages all configuration parameters required to connect to and configure
 * a MariaDB instance for vector storage and similarity search operations. It supports
 * multiple configuration sources and named/prefixed configurations for multi-instance setups.</p>
 *
 * <p>Configuration sources (in order of precedence):</p>
 * <ol>
 *   <li>Environment variables (highest precedence)</li>
 *   <li>System properties</li>
 *   <li>Configuration files (forage-vectordb-mariadb.properties)</li>
 *   <li>Default values (where applicable)</li>
 * </ol>
 *
 * <p>For named configurations, properties can be prefixed with the configuration name:
 * {@code <prefix>.mariadb.<property>}</p>
 *
 * @see MariaDbConfigEntries
 * @see MariaDbProvider
 */
public class MariaDbConfig extends AbstractConfig {

    private static final int DEFAULT_DIMENSION = 384;

    /**
     * Creates a new MariaDB configuration using default (non-prefixed) properties.
     */
    public MariaDbConfig() {
        this(null);
    }

    /**
     * Creates a new MariaDB configuration with an optional prefix for named configurations.
     *
     * <p>This constructor follows the standard three-phase initialization pattern:</p>
     * <ol>
     *   <li>Register prefixed configuration modules if prefix is provided</li>
     *   <li>Load configuration from properties files</li>
     *   <li>Load overrides from environment variables and system properties</li>
     * </ol>
     *
     * @param prefix optional prefix for scoped configuration properties
     */
    public MariaDbConfig(String prefix) {
        super(prefix, MariaDbConfigEntries.class);
    }

    @Override
    public String name() {
        return "forage-vectordb-mariadb";
    }

    /**
     * Gets the MariaDB JDBC URL for database connection.
     *
     * @return the JDBC URL (e.g., "jdbc:mariadb://localhost:3306/vectordb")
     * @throws io.kaoto.forage.core.util.config.MissingConfigException if URL is not configured
     */
    public String url() {
        return getRequired(URL, "Missing MariaDB JDBC URL");
    }

    /**
     * Gets the database username for authentication.
     *
     * @return the database username
     * @throws io.kaoto.forage.core.util.config.MissingConfigException if username is not configured
     */
    public String user() {
        return getRequired(USER, "Missing MariaDB user");
    }

    /**
     * Gets the database password for authentication.
     *
     * @return the database password, or null if not configured
     */
    public String password() {
        return get(PASSWORD).orElse(null);
    }

    /**
     * Gets the table name where embeddings will be stored.
     *
     * @return the table name (default: "embeddings")
     */
    public String table() {
        return get(TABLE).orElse("embeddings");
    }

    /**
     * Gets the distance calculation method for vector similarity.
     *
     * @return the distance type (default: COSINE)
     * @throws IllegalArgumentException if an invalid distance type is configured
     */
    public MariaDBDistanceType distanceType() {
        return get(DISTANCE_TYPE)
                .map(value -> MariaDBDistanceType.valueOf(value.toUpperCase()))
                .orElse(MariaDBDistanceType.COSINE);
    }

    /**
     * Gets the name of the ID field in the embeddings table.
     *
     * @return the ID field name (default: "id")
     */
    public String idFieldName() {
        return get(ID_FIELD_NAME).orElse("id");
    }

    /**
     * Gets the name of the embedding vector field in the embeddings table.
     *
     * @return the embedding field name (default: "embedding")
     */
    public String embeddingFieldName() {
        return get(EMBEDDING_FIELD_NAME).orElse("embedding");
    }

    /**
     * Gets the name of the content text field in the embeddings table.
     *
     * @return the content field name (default: "content")
     */
    public String contentFieldName() {
        return get(CONTENT_FIELD_NAME).orElse("content");
    }

    /**
     * Determines whether to automatically create the embeddings table if it doesn't exist.
     *
     * @return true to create table automatically (default: true)
     */
    public Boolean createTable() {
        return get(CREATE_TABLE).map(Boolean::parseBoolean).orElse(true);
    }

    /**
     * Determines whether to drop the table before creating it (useful for testing).
     *
     * @return true to drop table first (default: false)
     */
    public Boolean dropTableFirst() {
        return get(DROP_TABLE_FIRST).map(Boolean::parseBoolean).orElse(false);
    }

    /**
     * Gets the vector dimension size for the embeddings.
     *
     * @return the configured vector dimension, or the default of 384
     */
    public Integer dimension() {
        return get(DIMENSION).map(Integer::parseInt).orElse(DEFAULT_DIMENSION);
    }
}
