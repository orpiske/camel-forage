package io.kaoto.forage.vectordb.weaviate;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

public final class WeaviateConfigEntries extends ConfigEntries {
    public static final ConfigModule API_KEY = ConfigModule.of(
            WeaviateConfig.class,
            "forage.weaviate.api.key",
            "API key for authentication",
            "API Key",
            null,
            "password",
            false,
            ConfigTag.SECURITY);
    public static final ConfigModule SCHEME = ConfigModule.of(
            WeaviateConfig.class,
            "forage.weaviate.scheme",
            "Connection scheme (http or https)",
            "Scheme",
            null,
            "string",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule HOST = ConfigModule.of(
            WeaviateConfig.class,
            "forage.weaviate.host",
            "Weaviate server host address",
            "Host",
            null,
            "string",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule PORT = ConfigModule.of(
            WeaviateConfig.class,
            "forage.weaviate.port",
            "Weaviate server port number",
            "Port",
            null,
            "integer",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule USE_GRPC_FOR_INSERTS = ConfigModule.of(
            WeaviateConfig.class,
            "forage.weaviate.use.grpc.for.inserts",
            "Use gRPC protocol for insert operations",
            "Use gRPC for Inserts",
            null,
            "boolean",
            true,
            ConfigTag.ADVANCED);
    public static final ConfigModule SECURED_GRPC = ConfigModule.of(
            WeaviateConfig.class,
            "forage.weaviate.secured.grpc",
            "Enable secured gRPC connections",
            "Secured gRPC",
            null,
            "boolean",
            true,
            ConfigTag.SECURITY);
    public static final ConfigModule GRPC_PORT = ConfigModule.of(
            WeaviateConfig.class,
            "forage.weaviate.grpc.port",
            "gRPC server port number",
            "gRPC Port",
            null,
            "integer",
            true,
            ConfigTag.ADVANCED);
    public static final ConfigModule OBJECT_CLASS = ConfigModule.of(
            WeaviateConfig.class,
            "forage.weaviate.object.class",
            "Weaviate object class name",
            "Object Class",
            null,
            "string",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule AVOID_DUPS = ConfigModule.of(
            WeaviateConfig.class,
            "forage.weaviate.avoid.dups",
            "Avoid duplicate entries",
            "Avoid Duplicates",
            "true",
            "boolean",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule CONSISTENCY_LEVEL = ConfigModule.of(
            WeaviateConfig.class,
            "forage.weaviate.consistency.level",
            "Consistency level for operations",
            "Consistency Level",
            "ALL",
            "string",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule METADATA_KEYS = ConfigModule.of(
            WeaviateConfig.class,
            "forage.weaviate.metadata.keys",
            "Comma-separated list of metadata keys",
            "Metadata Keys",
            null,
            "string",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule TEXT_FIELD_NAME = ConfigModule.of(
            WeaviateConfig.class,
            "forage.weaviate.text.field.name",
            "Name of the text field",
            "Text Field Name",
            "text",
            "string",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule METADATA_FIELD_NAME = ConfigModule.of(
            WeaviateConfig.class,
            "forage.weaviate.metadata.field.name",
            "Name of the metadata field",
            "Metadata Field Name",
            "_metadata",
            "string",
            false,
            ConfigTag.ADVANCED);

    static {
        initModules(
                WeaviateConfigEntries.class,
                API_KEY,
                SCHEME,
                HOST,
                PORT,
                USE_GRPC_FOR_INSERTS,
                SECURED_GRPC,
                GRPC_PORT,
                OBJECT_CLASS,
                AVOID_DUPS,
                CONSISTENCY_LEVEL,
                METADATA_KEYS,
                TEXT_FIELD_NAME,
                METADATA_FIELD_NAME);
    }
}
