package io.kaoto.forage.cloud.eventhubs;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;
import com.azure.messaging.eventhubs.EventHubClientBuilder;

public final class AzureEventHubsConfigEntries extends ConfigEntries {

    public static final ConfigModule CONSUMER_GROUP = ConfigModule.of(
            AzureEventHubsConfig.class,
            "forage.azure.eventhubs.consumer.group",
            "The consumer group name for Event Hubs",
            "Consumer Group",
            EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME,
            "string",
            false,
            ConfigTag.COMMON);

    public static final ConfigModule EVENTHUB_NAME = ConfigModule.of(
            AzureEventHubsConfig.class,
            "forage.azure.eventhubs.eventhub.name",
            "The Event Hub name",
            "Event Hub Name",
            null,
            "string",
            true,
            ConfigTag.COMMON);

    public static final ConfigModule FULLY_QUALIFIED_NAMESPACE = ConfigModule.of(
            AzureEventHubsConfig.class,
            "forage.azure.eventhubs.fully.qualified.namespace",
            "The fully qualified namespace for Event Hubs (e.g., <namespace>.servicebus.windows.net)",
            "Fully Qualified Namespace",
            null,
            "string",
            true,
            ConfigTag.COMMON);

    public static final ConfigModule PREFETCH_COUNT = ConfigModule.of(
            AzureEventHubsConfig.class,
            "forage.azure.eventhubs.prefetch.count",
            "The number of events to prefetch from Event Hubs",
            "Prefetch Count",
            "100",
            "integer",
            false,
            ConfigTag.ADVANCED);

    public static final ConfigModule CUSTOM_ENDPOINT_ADDRESS = ConfigModule.of(
            AzureEventHubsConfig.class,
            "forage.azure.eventhubs.custom.endpoint.address",
            "Custom endpoint address for Event Hubs (optional, for custom Azure environments)",
            "Custom Endpoint",
            null,
            "string",
            false,
            ConfigTag.ADVANCED);

    static {
        initModules(
                AzureEventHubsConfigEntries.class,
                CONSUMER_GROUP,
                EVENTHUB_NAME,
                FULLY_QUALIFIED_NAMESPACE,
                PREFETCH_COUNT,
                CUSTOM_ENDPOINT_ADDRESS);
    }
}
