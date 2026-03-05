package io.kaoto.forage.cloud.eventhubs;

import io.kaoto.forage.core.util.config.AbstractConfig;

import static io.kaoto.forage.cloud.eventhubs.AzureEventHubsConfigEntries.CONSUMER_GROUP;
import static io.kaoto.forage.cloud.eventhubs.AzureEventHubsConfigEntries.CUSTOM_ENDPOINT_ADDRESS;
import static io.kaoto.forage.cloud.eventhubs.AzureEventHubsConfigEntries.EVENTHUB_NAME;
import static io.kaoto.forage.cloud.eventhubs.AzureEventHubsConfigEntries.FULLY_QUALIFIED_NAMESPACE;
import static io.kaoto.forage.cloud.eventhubs.AzureEventHubsConfigEntries.PREFETCH_COUNT;

public class AzureEventHubsConfig extends AbstractConfig {

    public AzureEventHubsConfig() {
        this(null);
    }

    public AzureEventHubsConfig(String prefix) {
        super(prefix, AzureEventHubsConfigEntries.class);
    }

    @Override
    public String name() {
        return "forage-azure-eventhubs";
    }

    public String consumerGroup() {
        return get(CONSUMER_GROUP).orElse(CONSUMER_GROUP.defaultValue());
    }

    public String eventHubName() {
        return getRequired(EVENTHUB_NAME, "Event Hub name is required but not configured");
    }

    public String fullyQualifiedNamespace() {
        return getRequired(FULLY_QUALIFIED_NAMESPACE, "Fully qualified namespace is required but not configured");
    }

    public int prefetchCount() {
        return get(PREFETCH_COUNT).map(Integer::parseInt).orElse(Integer.parseInt(PREFETCH_COUNT.defaultValue()));
    }

    public String customEndpointAddress() {
        return get(CUSTOM_ENDPOINT_ADDRESS).orElse(null);
    }
}
