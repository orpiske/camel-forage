package io.kaoto.forage.rag.defaultRag;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;

public final class DefaultRetrievalAugmentorConfigEntries extends ConfigEntries {
    public static final ConfigModule MAX_RESULTS = ConfigModule.of(
            DefaultRetrievalAugmentorConfig.class,
            "forage.rag.max.results",
            "The maximum number of Contents to retrieve.",
            "Max results",
            null,
            "int",
            false,
            ConfigTag.COMMON);
    public static final ConfigModule MIN_SCORE = ConfigModule.of(
            DefaultRetrievalAugmentorConfig.class,
            "forage.rag.min.score",
            "The minimum relevance score for the returned Contents.",
            "Min score",
            null,
            "double",
            false,
            ConfigTag.COMMON);

    static {
        initModules(DefaultRetrievalAugmentorConfigEntries.class, MAX_RESULTS, MIN_SCORE);
    }
}
