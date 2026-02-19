package io.kaoto.forage.core.cloud;

import io.kaoto.forage.core.common.BeanProvider;
import com.azure.messaging.eventhubs.EventHubProducerAsyncClient;

public interface EventHubProducerProvider extends BeanProvider<EventHubProducerAsyncClient> {

    @Override
    default EventHubProducerAsyncClient create() {
        return create(null);
    }

    @Override
    EventHubProducerAsyncClient create(String id);
}
