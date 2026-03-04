package io.kaoto.forage.quarkus.jms;

import io.kaoto.forage.core.common.ForageModuleDescriptor;
import io.kaoto.forage.core.common.ForageQuarkusConfigSourceAdapter;
import io.kaoto.forage.jms.common.ConnectionFactoryConfig;
import io.kaoto.forage.jms.common.JmsModuleDescriptor;

/**
 * SmallRye {@link io.smallrye.config.ConfigSourceFactory} that translates Forage JMS properties
 * into Quarkus JMS properties at config bootstrap time.
 *
 * <p>Delegates all logic to {@link ForageQuarkusConfigSourceAdapter} using the {@link JmsModuleDescriptor}.
 *
 * <p>Registered via {@code META-INF/services/io.smallrye.config.ConfigSourceFactory}.
 *
 * @since 1.1
 */
public class ForageJmsConfigSourceFactory extends ForageQuarkusConfigSourceAdapter<ConnectionFactoryConfig> {

    @Override
    protected ForageModuleDescriptor<ConnectionFactoryConfig, ?> descriptor() {
        return new JmsModuleDescriptor();
    }
}
