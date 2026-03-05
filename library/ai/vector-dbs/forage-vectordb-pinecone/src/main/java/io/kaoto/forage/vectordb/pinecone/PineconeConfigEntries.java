package io.kaoto.forage.vectordb.pinecone;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

public final class PineconeConfigEntries extends ConfigEntries {
    public static final ConfigModule API_KEY = ConfigModule.of(
            PineconeConfig.class,
            "forage.pinecone.api.key",
            "Pinecone API key for authentication",
            "API Key",
            null,
            "password",
            true,
            ConfigTag.SECURITY);
    public static final ConfigModule INDEX = ConfigModule.of(
            PineconeConfig.class,
            "forage.pinecone.index",
            "Name of the Pinecone index",
            "Index",
            null,
            "string",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule NAME_SPACE = ConfigModule.of(
            PineconeConfig.class,
            "forage.pinecone.name.space",
            "Namespace within the index",
            "Namespace",
            "default",
            "string",
            false,
            ConfigTag.COMMON);
    public static final ConfigModule METADATA_TEXT_KEY = ConfigModule.of(
            PineconeConfig.class,
            "forage.pinecone.metadata.text.key",
            "Metadata key for storing text content",
            "Metadata Text Key",
            "text_segment",
            "string",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule CREATE_INDEX = ConfigModule.of(
            PineconeConfig.class,
            "forage.pinecone.create.index",
            "Create index if it does not exist",
            "Create Index",
            null,
            "boolean",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule ENVIRONMENT = ConfigModule.of(
            PineconeConfig.class,
            "forage.pinecone.environment",
            "Pinecone environment (deprecated)",
            "Environment",
            null,
            "string",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule PROJECT_ID = ConfigModule.of(
            PineconeConfig.class,
            "forage.pinecone.project.id",
            "Pinecone project ID (deprecated)",
            "Project ID",
            null,
            "string",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule DIMENSION = ConfigModule.of(
            PineconeConfig.class,
            "forage.pinecone.dimension",
            "Vector dimension size",
            "Dimension",
            null,
            "integer",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule CLOUD = ConfigModule.of(
            PineconeConfig.class,
            "forage.pinecone.cloud",
            "Cloud provider (e.g., aws, gcp, azure)",
            "Cloud",
            null,
            "string",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule REGION = ConfigModule.of(
            PineconeConfig.class,
            "forage.pinecone.region",
            "Cloud region for the index",
            "Region",
            null,
            "string",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule DELETION_PROTECTION = ConfigModule.of(
            PineconeConfig.class,
            "forage.pinecone.deletion.protection",
            "Enable deletion protection (ENABLED or DISABLED)",
            "Deletion Protection",
            "ENABLED",
            "string",
            false,
            ConfigTag.ADVANCED);

    static {
        initModules(
                PineconeConfigEntries.class,
                API_KEY,
                INDEX,
                NAME_SPACE,
                METADATA_TEXT_KEY,
                CREATE_INDEX,
                ENVIRONMENT,
                PROJECT_ID,
                DIMENSION,
                CLOUD,
                REGION,
                DELETION_PROTECTION);
    }
}
