package io.kaoto.forage.vectordb.redis;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

public final class RedisConfigEntries extends ConfigEntries {
    public static final ConfigModule HOST = ConfigModule.of(
            RedisConfig.class,
            "forage.redis.host",
            "Redis server host address",
            "Host",
            "localhost",
            "string",
            false,
            ConfigTag.COMMON);
    public static final ConfigModule PORT = ConfigModule.of(
            RedisConfig.class,
            "forage.redis.port",
            "Redis server port number",
            "Port",
            "6379",
            "integer",
            false,
            ConfigTag.COMMON);
    public static final ConfigModule USER = ConfigModule.of(
            RedisConfig.class,
            "forage.redis.user",
            "Username for authentication",
            "User",
            null,
            "string",
            false,
            ConfigTag.SECURITY);
    public static final ConfigModule PASSWORD = ConfigModule.of(
            RedisConfig.class,
            "forage.redis.password",
            "Password for authentication",
            "Password",
            null,
            "password",
            false,
            ConfigTag.SECURITY);
    public static final ConfigModule DIMENSION = ConfigModule.of(
            RedisConfig.class,
            "forage.redis.dimension",
            "Vector dimension size",
            "Dimension",
            null,
            "integer",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule PREFIX = ConfigModule.of(
            RedisConfig.class,
            "forage.redis.prefix",
            "Key prefix for vector storage",
            "Prefix",
            "embedding:",
            "string",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule INDEX_NAME = ConfigModule.of(
            RedisConfig.class,
            "forage.redis.index.name",
            "Name of the vector index",
            "Index Name",
            "embedding-index",
            "string",
            false,
            ConfigTag.COMMON);
    public static final ConfigModule METADATA_FIELDS = ConfigModule.of(
            RedisConfig.class,
            "forage.redis.metadata.fields",
            "Comma-separated list of metadata fields",
            "Metadata Fields",
            null,
            "string",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule DISTANCE_METRIC = ConfigModule.of(
            RedisConfig.class,
            "forage.redis.distance.metric",
            "Distance metric for similarity search (COSINE, L2, IP)",
            "Distance Metric",
            "COSINE",
            "string",
            false,
            ConfigTag.ADVANCED);

    static {
        initModules(
                RedisConfigEntries.class,
                HOST,
                PORT,
                USER,
                PASSWORD,
                DIMENSION,
                PREFIX,
                INDEX_NAME,
                METADATA_FIELDS,
                DISTANCE_METRIC);
    }
}
