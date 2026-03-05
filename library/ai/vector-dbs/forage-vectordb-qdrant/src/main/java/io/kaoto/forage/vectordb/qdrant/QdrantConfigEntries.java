package io.kaoto.forage.vectordb.qdrant;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

public final class QdrantConfigEntries extends ConfigEntries {
    public static final ConfigModule COLLECTION_NAME = ConfigModule.of(
            QdrantConfig.class,
            "forage.qdrant.collection.name",
            "Name of the Qdrant collection",
            "Collection Name",
            null,
            "string",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule HOST = ConfigModule.of(
            QdrantConfig.class,
            "forage.qdrant.host",
            "Qdrant server host address",
            "Host",
            null,
            "string",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule PORT = ConfigModule.of(
            QdrantConfig.class,
            "forage.qdrant.port",
            "Qdrant server port number",
            "Port",
            null,
            "integer",
            true,
            ConfigTag.COMMON);
    public static final ConfigModule USE_TLS = ConfigModule.of(
            QdrantConfig.class,
            "forage.qdrant.use.tls",
            "Enable TLS for secure connections",
            "Use TLS",
            "false",
            "boolean",
            false,
            ConfigTag.SECURITY);
    public static final ConfigModule PAYLOAD_TEXT_KEY = ConfigModule.of(
            QdrantConfig.class,
            "forage.qdrant.payload.text.key",
            "Payload key for storing text content",
            "Payload Text Key",
            "text_segment",
            "string",
            false,
            ConfigTag.ADVANCED);
    public static final ConfigModule API_KEY = ConfigModule.of(
            QdrantConfig.class,
            "forage.qdrant.api.key",
            "API key for authentication",
            "API Key",
            null,
            "password",
            false,
            ConfigTag.SECURITY);

    static {
        initModules(QdrantConfigEntries.class, COLLECTION_NAME, HOST, PORT, USE_TLS, PAYLOAD_TEXT_KEY, API_KEY);
    }
}
