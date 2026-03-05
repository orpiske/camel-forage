package io.kaoto.forage.vectordb.pgvector;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

public final class PgVectorConfigEntries extends ConfigEntries {
    public static final ConfigModule HOST = ConfigModule.of(
            PgVectorConfig.class,
            "forage.pgvector.host",
            "PostgreSQL server host address",
            "Host",
            null,
            "string",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule PORT = ConfigModule.of(
            PgVectorConfig.class,
            "forage.pgvector.port",
            "PostgreSQL server port number",
            "Port",
            null,
            "integer",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule USER = ConfigModule.of(
            PgVectorConfig.class,
            "forage.pgvector.user",
            "Database username",
            "User",
            null,
            "string",
            true,
            ConfigTag.SECURITY);
    public static final ConfigModule PASSWORD = ConfigModule.of(
            PgVectorConfig.class,
            "forage.pgvector.password",
            "Database password",
            "Password",
            null,
            "password",
            true,
            ConfigTag.SECURITY);
    public static final ConfigModule DATABASE = ConfigModule.of(
            PgVectorConfig.class,
            "forage.pgvector.database",
            "Database name",
            "Database",
            null,
            "string",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule TABLE = ConfigModule.of(
            PgVectorConfig.class,
            "forage.pgvector.table",
            "Table name for storing vectors",
            "Table",
            null,
            "string",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule DIMENSION = ConfigModule.of(
            PgVectorConfig.class,
            "forage.pgvector.dimension",
            "Vector dimension size",
            "Dimension",
            null,
            "integer",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule USE_INDEX = ConfigModule.of(
            PgVectorConfig.class,
            "forage.pgvector.use.index",
            "Enable vector index for faster search",
            "Use Index",
            "false",
            "boolean",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule INDEX_LIST_SIZE = ConfigModule.of(
            PgVectorConfig.class,
            "forage.pgvector.index.list.size",
            "Index list size for IVFFlat index",
            "Index List Size",
            "100",
            "integer",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule CREATE_TABLE = ConfigModule.of(
            PgVectorConfig.class,
            "forage.pgvector.create.table",
            "Automatically create table if it does not exist",
            "Create Table",
            "true",
            "boolean",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule DROP_TABLE_FIRST = ConfigModule.of(
            PgVectorConfig.class,
            "forage.pgvector.drop.table.first",
            "Drop table before creating (for testing)",
            "Drop Table First",
            "false",
            "boolean",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule METADATA_STORAGE_CONFIG = ConfigModule.of(
            PgVectorConfig.class,
            "forage.pgvector.metadata.storage.config",
            "Metadata storage configuration",
            "Metadata Storage Config",
            null,
            "string",
            false,
            ConfigTag.ADVANCED);

    static {
        initModules(
                PgVectorConfigEntries.class,
                HOST,
                PORT,
                USER,
                PASSWORD,
                DATABASE,
                TABLE,
                DIMENSION,
                USE_INDEX,
                INDEX_LIST_SIZE,
                CREATE_TABLE,
                DROP_TABLE_FIRST,
                METADATA_STORAGE_CONFIG);
    }
}
