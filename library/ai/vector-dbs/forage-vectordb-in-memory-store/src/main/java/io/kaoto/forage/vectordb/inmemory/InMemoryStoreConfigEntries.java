package io.kaoto.forage.vectordb.inmemory;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

public final class InMemoryStoreConfigEntries extends ConfigEntries {
    public static final ConfigModule FILE_SOURCE = ConfigModule.of(
            InMemoryStoreConfig.class,
            "forage.in.memory.store.file.source",
            "Path to a file to be loaded into store.",
            "File source",
            null,
            "string",
            true,
            ConfigTag.COMMON);

    public static final ConfigModule MAX_SIZE = ConfigModule.of(
            InMemoryStoreConfig.class,
            "forage.in.memory.store.max.size",
            "The maximum size of the segment, defined in characters.",
            "Max size",
            null,
            "int",
            false,
            ConfigTag.COMMON);

    public static final ConfigModule OVERLAP_SIZE = ConfigModule.of(
            InMemoryStoreConfig.class,
            "forage.in.memory.store.overlap.size",
            "The maximum size of the overlap, defined in characters.",
            "Overlap size",
            null,
            "int",
            false,
            ConfigTag.COMMON);

    static {
        initModules(InMemoryStoreConfigEntries.class, FILE_SOURCE, MAX_SIZE, OVERLAP_SIZE);
    }
}
