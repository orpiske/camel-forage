package io.kaoto.forage.policy.factory;

import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.kaoto.forage.core.common.BeanFactory;

public class DefaultCamelForageRoutePolicyFactoryBean implements BeanFactory {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCamelForageRoutePolicyFactoryBean.class);

    private CamelContext camelContext;

    @Override
    public void configure() {
        LOG.warn("Creating route policy factory");
        RoutePolicyFactoryConfig config = new RoutePolicyFactoryConfig();

        if (!config.isEnabled()) {
            LOG.info("Route policy factory is disabled via configuration");
            return;
        }

        DefaultCamelForageRoutePolicyFactory routePolicyFactory = new DefaultCamelForageRoutePolicyFactory(config);
        camelContext.addRoutePolicyFactory(routePolicyFactory);
        LOG.debug("Route policy factory registered with CamelContext");
    }

    @Override
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public CamelContext getCamelContext() {
        return camelContext;
    }
}
